package net.dunotech.venus.system.dto.common;

public class UploadFileInfoDto {

    private String showName;

    private String realName;

    private String suffix;

    private String url;

    public UploadFileInfoDto(String showName, String realName, String suffix, String url) {
        this.showName = showName;
        this.realName = realName;
        this.suffix = suffix;
        this.url = url;
    }

    public UploadFileInfoDto(){
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
