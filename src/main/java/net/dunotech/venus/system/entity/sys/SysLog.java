package net.dunotech.venus.system.entity.sys;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@TableName("sys_log")
@Component
public class SysLog implements Serializable {

  @TableId(type = IdType.AUTO)
  @TableField(value = "id")
  private Long id;

  @TableField(value = "user_id")
  private String userId;

  @TableField(value = "username")
  private String username;

  @TableField(value = "user_action")
  private String userAction;

  @TableField(value = "create_time")
  private Timestamp createTime;

  @TableField(value = "description")
  private String description;

  @TableField(value = "result")
  private String result;

  @TableField(value = "user_ip")
  private String userIp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getUserAction() {
    return userAction;
  }

  public void setUserAction(String userAction) {
    this.userAction = userAction;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
