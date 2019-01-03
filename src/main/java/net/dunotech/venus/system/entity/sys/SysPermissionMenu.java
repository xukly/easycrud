package net.dunotech.venus.system.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_permission_menu")
@Component
public class SysPermissionMenu extends IdEntity{

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "menu_id")
  private String menuId;

  @TableField(value = "permission_id")
  private String permissionId;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }


  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

}
