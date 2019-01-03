package net.dunotech.venus.system.entity.oss;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@TableName("oss_entity")
@Component
public class OssEntity extends IdEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "id")
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * URL地址
     */
    @TableField(value = "url")
    private String url;
    /**
     * 业务id
     */
    @TableField(value = "report_id")
    private String reportId;
    /**
     * 上传文件的数据
     */
    @TableField(value = "data")
    private String data;

    @TableField(value = "parent_id")
    private String parentId;

    @TableField(value = "real_name")
    private String realName;

    @TableField(value = "show_name")
    private String showName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
}
