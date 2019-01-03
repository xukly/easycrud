package net.dunotech.venus.system.entity.sys;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@TableName("sys_message")
@Component
public class SysMessage extends IdEntity {

    @TableField(value = "id")
    @TableId(type = IdType.UUID)
    private String id;

    @TableField(value = "content")
    private String content;

    @TableField(value = "is_read")
    private String isRead;

    @TableField(value = "url")
    private String url;

    @TableField(value = "account")
    private String account;

    @TableField(value = "read_time")
    private Timestamp readTime;

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
