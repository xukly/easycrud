package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_permission_url")
@Component
public class SysPermissionUrl extends IdEntity {

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "permission_id")
  private String permissionId;

  @TableField(value = "url_name")
  private String urlName;

  @TableField(value = "url_code")
  private String urlCode;

  @TableField(value = "url")
  private String url;

  @TableField(value = "url_type")
  private String urlType;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }


  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }


  public String getUrlCode() {
    return urlCode;
  }

  public void setUrlCode(String urlCode) {
    this.urlCode = urlCode;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public String getUrlType() {
    return urlType;
  }

  public void setUrlType(String urlType) {
    this.urlType = urlType;
  }

}
