package net.dunotech.venus.properties;

import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "venus.oss.qcloud")
public class VenusOssQcloudProperties {

    private String domain = StringUtils.EMPTY;

    private String prefix = StringUtils.EMPTY;

    private String accessKey = StringUtils.EMPTY;

    private String secretId = StringUtils.EMPTY;

    private String secretKey = StringUtils.EMPTY;

    private String bucketName = StringUtils.EMPTY;

    private String region = StringUtils.EMPTY;

    private String appId = StringUtils.EMPTY;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
