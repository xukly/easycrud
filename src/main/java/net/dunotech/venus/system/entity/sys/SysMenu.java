package net.dunotech.venus.system.entity.sys;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_menu")
@Component
public class SysMenu extends IdEntity {

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "menu_name")
  private String menuName;

  @TableField(value = "menu_type")
  private String menuType;

  @TableField(value = "parent_id")
  private String parentId;

  @TableField(value = "iconcls")
  private String iconcls;

  @TableField(value = "request")
  private String request;

  @TableField(value = "expand")
  private String expand;

  @TableField(value = "sort_no")
  private long sortNo;

  @TableField(value = "is_show")
  private String isShow;

  @TableField(value = "remark")
  private String remark;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }


  public String getMenuType() {
    return menuType;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }


  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  public String getIconcls() {
    return iconcls;
  }

  public void setIconcls(String iconcls) {
    this.iconcls = iconcls;
  }


  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }


  public String getExpand() {
    return expand;
  }

  public void setExpand(String expand) {
    this.expand = expand;
  }


  public long getSortNo() {
    return sortNo;
  }

  public void setSortNo(long sortNo) {
    this.sortNo = sortNo;
  }


  public String getIsShow() {
    return isShow;
  }

  public void setIsShow(String isShow) {
    this.isShow = isShow;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
