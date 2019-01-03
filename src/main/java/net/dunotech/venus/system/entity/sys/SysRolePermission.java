package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_role_permission")
@Component
public class SysRolePermission extends IdEntity{

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "role_id")
  private String roleId;

  @TableField(value = "permission_id")
  private String permissionId;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }


  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }


}
