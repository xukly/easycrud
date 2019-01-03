package net.dunotech.venus.properties;

import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "venus.oss.aliyun")
public class VenusOssAliProperties {

    private String domain = StringUtils.EMPTY;

    private String endPoint = StringUtils.EMPTY;

    private String accessKeyId = StringUtils.EMPTY;

    private String accessKeySecret = StringUtils.EMPTY;

    private String bucketName = StringUtils.EMPTY;

    private String prefix = StringUtils.EMPTY;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
