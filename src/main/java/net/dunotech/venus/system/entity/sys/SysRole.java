package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.util.List;

@TableName("sys_role")
@Component
public class SysRole extends IdEntity {

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "role_name")
  @Size(max = 16,message = "角色名称不得超过16个字符")
  private String roleName;

  @TableField(value = "remark")
  private String remark;

  @TableField(exist = false)
  private List<String> permissionList;

  @TableField(exist = false)
  private List<String> deptList;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public List<String> getPermissionList() {
    return permissionList;
  }

  public void setPermissionList(List<String> permissionList) {
    this.permissionList = permissionList;
  }

  public List<String> getDeptList() {
    return deptList;
  }

  public void setDeptList(List<String> deptList) {
    this.deptList = deptList;
  }
}
