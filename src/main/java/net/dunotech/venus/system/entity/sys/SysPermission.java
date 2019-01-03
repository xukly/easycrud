package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_permission")
@Component
public class SysPermission extends IdEntity {

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "permission_name")
  private String permissionName;

  @TableField(value = "permission_type")
  private String permissionType;

  @TableField(value = "parent_id")
  private String parentId;

  @TableField(value = "sort_no")
  private long sortNo;

  @TableField(value = "remark")
  private String remark;

  @TableField(value = "ic")
  private String ic;

  public String getIc() {
    return ic;
  }

  public void setIc(String ic) {
    this.ic = ic;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }


  public String getPermissionType() {
    return permissionType;
  }

  public void setPermissionType(String permissionType) {
    this.permissionType = permissionType;
  }


  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  public long getSortNo() {
    return sortNo;
  }

  public void setSortNo(long sortNo) {
    this.sortNo = sortNo;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


}
