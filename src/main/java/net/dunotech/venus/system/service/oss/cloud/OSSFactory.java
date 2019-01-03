package net.dunotech.venus.system.service.oss.cloud;


import net.dunotech.venus.system.config.oss.CloudServiceConstants;
import net.dunotech.venus.system.config.oss.CloudStorageConfig;
import net.dunotech.venus.system.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;


/**
 * 文件上传Factory
 */
public class OSSFactory {
    private static Logger logger = LoggerFactory.getLogger(OSSFactory.class);
    @Autowired
    protected WebApplicationContext applicationContext;

    private static CloudStorageConfig config;

    public static CloudStorageService build(){
        //获取云存储配置信息
//        CloudStorageConfig common = sysConfigService.getConfigObject(CloudServiceConstants.CLOUD_STORAGE_CONFIG_KEY,CloudStorageConfig.class);
        if(config.getType() == CloudServiceConstants.QINIU){
            return new QiniuCloudStorageService(config);
        }else if(config.getType() == CloudServiceConstants.ALIYUN){
            return new AliyunCloudStorageService(config);
        }else if(config.getType() == CloudServiceConstants.QCLOUD){
            return new QcloudCloudStorageService(config);
        }

        return null;
    }

    public void setConfig(CloudStorageConfig config) {
        OSSFactory.config = config;
    }
}
