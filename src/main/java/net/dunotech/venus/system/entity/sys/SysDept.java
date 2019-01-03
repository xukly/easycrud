package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import net.dunotech.venus.system.service.annotation.ExtraProperty;
import org.springframework.stereotype.Component;

@TableName("sys_dept")
@Component
public class SysDept extends IdEntity {

  @TableId(type = IdType.UUID)
  @TableField(value = "id")
  private String id;

  @TableField(value = "dept_name")
  private String deptName;

  @TableField(value = "parent_id")
  private String parentId;

  @TableField(value = "sort_no")
  private Integer sortNo;

  @TableField(value = "remark")
  private String remark;

  @TableField(value = "enable")
  private String enable;

  @ExtraProperty
  @TableField(value = "extra_properties")
  private String extraProperties;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }


  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }



  public String getEnable() {
    return enable;
  }

  public void setEnable(String enable) {
    this.enable = enable;
  }


  public String getExtraProperties() {
    return extraProperties;
  }

  public void setExtraProperties(String extraProperties) {
    this.extraProperties = extraProperties;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }
}
