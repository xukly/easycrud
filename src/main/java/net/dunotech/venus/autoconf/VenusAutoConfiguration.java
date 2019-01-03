package net.dunotech.venus.autoconf;

import net.dunotech.venus.properties.VenusOssAliProperties;
import net.dunotech.venus.properties.VenusOssQcloudProperties;
import net.dunotech.venus.properties.VenusOssQiniuProperties;
import net.dunotech.venus.properties.VenusServiceProperties;
import net.dunotech.venus.system.config.oss.CloudServiceConstants;
import net.dunotech.venus.system.config.oss.CloudStorageConfig;
import net.dunotech.venus.system.entity.common.IdEntity;
import net.dunotech.venus.system.service.common.crud.CommonCrudService;
import net.dunotech.venus.system.service.common.select.CommonSelectService;
import net.dunotech.venus.system.service.oss.cloud.OSSFactory;
import net.dunotech.venus.system.service.oss.cloud.OssHandleService;
import net.dunotech.venus.system.utils.SpringContextUtil;
import net.dunotech.venus.system.service.sys.SysUserService;
import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {VenusServiceProperties.class, VenusOssAliProperties.class, VenusOssQiniuProperties.class, VenusOssQcloudProperties.class})
@ConditionalOnClass(value = {JwtTokenUtils.class,CloudStorageConfig.class}) // 需要被配置的类
@ConditionalOnProperty(value = "enable", matchIfMissing = true)
public class VenusAutoConfiguration {

    @Autowired
    private VenusServiceProperties venusServiceProperties;

    @Autowired
    private VenusOssAliProperties venusOssAliProperties;

    @Autowired
    private VenusOssQiniuProperties venusOssQiniuProperties;

    @Autowired
    private VenusOssQcloudProperties venusOssQcloudProperties;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return IdEntity
     */
    @Bean
    @ConditionalOnMissingBean(IdEntity.class)
    public IdEntity idEntity() {
       return new IdEntity();
    }

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return CommonCrudService
     */
    @Bean
    @ConditionalOnMissingBean(CommonCrudService.class)
    public CommonCrudService commonCrudService() {
        return new CommonCrudService();
    }

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return CommonSelectService
     */
    @Bean
    @ConditionalOnMissingBean(CommonSelectService.class)
    public CommonSelectService commonSelectService() {
        return new CommonSelectService();
    }

    @Bean
    @ConditionalOnMissingBean(SpringContextUtil.class)
    public SpringContextUtil SpringContextUtil() {
        return new SpringContextUtil();
    }

    @Bean
    @ConditionalOnMissingBean(OssHandleService.class)
    public OssHandleService OssHandleService() {
        return new OssHandleService();
    }
    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return SysUserService
     */
    @Bean
    @ConditionalOnMissingBean(SysUserService.class)
    public SysUserService sysUserService() {
        SysUserService sysUserService = new SysUserService();
        sysUserService.setDefaultPassword(venusServiceProperties.getDefaultPassword());
        return sysUserService;
    }

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return CloudStorageConfig
     */
    @Bean
    @ConditionalOnMissingBean(CloudStorageConfig.class)
    public CloudStorageConfig cloudStorageConfig() {
        CloudStorageConfig cloudStorageConfig = new CloudStorageConfig();
        if (venusServiceProperties.getOssType().equals(String.valueOf(CloudServiceConstants.QINIU))) {
            cloudStorageConfig.setType(CloudServiceConstants.QINIU);
            cloudStorageConfig.setQiniuAccessKey(venusOssQiniuProperties.getAccessKey());
            cloudStorageConfig.setQiniuSecretKey(venusOssQiniuProperties.getSecretKey());
            cloudStorageConfig.setQiniuPrefix(venusOssQiniuProperties.getPrefix());
            cloudStorageConfig.setQiniuDomain(venusOssQiniuProperties.getDomain());
            cloudStorageConfig.setQiniuBucketName(venusOssQiniuProperties.getBucketName());
        } else if (venusServiceProperties.getOssType().equals(String.valueOf(CloudServiceConstants.ALIYUN))) {
            cloudStorageConfig.setType(CloudServiceConstants.ALIYUN);
            cloudStorageConfig.setAliyunAccessKeyId(venusOssAliProperties.getAccessKeyId());
            cloudStorageConfig.setAliyunAccessKeySecret(venusOssAliProperties.getAccessKeySecret());
            cloudStorageConfig.setAliyunBucketName(venusOssAliProperties.getBucketName());
            cloudStorageConfig.setAliyunDomain(venusOssAliProperties.getDomain());
            cloudStorageConfig.setAliyunEndPoint(venusOssAliProperties.getEndPoint());
            cloudStorageConfig.setAliyunPrefix(venusOssAliProperties.getPrefix());
        } else if (venusServiceProperties.getOssType().equals(String.valueOf(CloudServiceConstants.QCLOUD))) {
            cloudStorageConfig.setType(CloudServiceConstants.QCLOUD);
            cloudStorageConfig.setQcloudSecretKey(venusOssQcloudProperties.getSecretKey());
            cloudStorageConfig.setQcloudSecretId(venusOssQcloudProperties.getSecretId());
            cloudStorageConfig.setQcloudRegion(venusOssQcloudProperties.getRegion());
            cloudStorageConfig.setQcloudPrefix(venusOssQcloudProperties.getPrefix());
            cloudStorageConfig.setQcloudDomain(venusOssQcloudProperties.getDomain());
            cloudStorageConfig.setQcloudBucketName(venusOssQcloudProperties.getBucketName());
            cloudStorageConfig.setQcloudAppId(Integer.parseInt(venusOssQcloudProperties.getAppId()));
        }
        return cloudStorageConfig;
    }

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return JwtTokenUtils
     */
    @Bean
    @ConditionalOnMissingBean(JwtTokenUtils.class)
    public JwtTokenUtils jwtTokenUtils() {
        JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();
        jwtTokenUtils.setExpirationRemember(Long.parseLong(venusServiceProperties.getExpirationRemember()));
        jwtTokenUtils.setExpirationNotRemember(Long.parseLong(venusServiceProperties.getExpiration()));
        jwtTokenUtils.setRedisUtils(redisUtils);
        return jwtTokenUtils;
    }

    /**
     * 给bean注入参数，同时返回一个bean实例
     * 同时注解表名，返回是一个bean实例
     * 当容器中没有这个bean实例的时候，就返回一个自动注入好参数的bean实例回去
     * @return OSSFactory
     */
    @Bean
    @ConditionalOnMissingBean(OSSFactory.class)
    public OSSFactory ossFactory() {
        OSSFactory ossFactory = new OSSFactory();
        ossFactory.setConfig(cloudStorageConfig());
        return ossFactory;
    }
}
