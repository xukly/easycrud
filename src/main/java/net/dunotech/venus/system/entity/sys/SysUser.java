package net.dunotech.venus.system.entity.sys;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import net.dunotech.venus.system.service.annotation.ExtraProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@TableName("sys_user")
@Component
public class SysUser extends IdEntity {

  @TableField(value = "id")
  @TableId(type = IdType.UUID)
  private String id;

  @TableField(value = "account")
  @Size(max = 16,message = "用户名不得超过16个字符")
  @NotEmpty(message = "用户名不能为空")
  private String account;

  @TableField(value = "password")
  @NotEmpty(message = "密码不能为空")
  private String password;

  @TableField(value = "user_type")
  private String userType;

  @TableField(value = "username")
  @Size(max = 16,message = "显示名称不得超过16个字符")
  @NotEmpty(message = "显示名称不能为空")
  private String username;

  @TableField(value = "name_pinyin")
  private String namePinyin;

  @TableField(value = "sex")
  private String sex;

  @TableField(value = "phone")
  @Pattern(regexp = "/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$/",message = "手机号格式错误")
  private String phone;

  @TableField(value = "email")
  @Email(message = "邮箱格式错误")
  private String email;

  @TableField(value = "id_card")
  @Pattern(regexp = "/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/",message = "身份证号格式错误")
  private String idCard;

  @TableField(value = "birthday")
  private String birthday;

  @TableField(value = "dept_id")
  private String deptId;

  @TableField(value = "position")
  private String position;

  @TableField(value = "address")
  private String address;

  @TableField(value = "enable")
  private String enable;

  @ExtraProperty
  @TableField(value = "extra_properties")
  private String extraProperties;

  @TableField(value = "avator")
  private String avator;

  @TableField(exist = false)
  private List<String> role;

  public String getEnable() {
    return enable;
  }

  public void setEnable(String enable) {
    this.enable = enable;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getNamePinyin() {
    return namePinyin;
  }

  public void setNamePinyin(String namePinyin) {
    this.namePinyin = namePinyin;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }


  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }


  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }


  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getExtraProperties() {
    return extraProperties;
  }

  public void setExtraProperties(String extraProperties) {
    this.extraProperties = extraProperties;
  }

  public List<String> getRole() {
    return role;
  }

  public void setRole(List<String> role) {
    this.role = role;
  }

  public String getAvator() {
    return avator;
  }

  public void setAvator(String avator) {
    this.avator = avator;
  }
}
