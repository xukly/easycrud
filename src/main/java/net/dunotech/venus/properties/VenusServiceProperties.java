package net.dunotech.venus.properties;

import net.dunotech.venus.system.config.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "venus")
public class VenusServiceProperties {

    // 过期时间是10800秒，既是3个小时
    private static final String EXPIRATION = "10800";

    // 选择了记住我之后的过期时间为7天
    private static final String EXPIRATION_REMEMBER = "604800";

    private static final String DEFAULT_PASSWORD = Constants.DEFAULT_PASSWORD;

    private static final String OSS_TYPE = "2";

    private String expiration = EXPIRATION;

    private String expirationRemember = EXPIRATION_REMEMBER;

    private String defaultPassword = DEFAULT_PASSWORD;

    private String ossType = OSS_TYPE;

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getExpirationRemember() {
        return expirationRemember;
    }

    public void setExpirationRemember(String expirationRemember) {
        this.expirationRemember =  expirationRemember;
    }

    public String getOssType() {
        return ossType;
    }

    public void setOssType(String ossType) {
        this.ossType = ossType;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
