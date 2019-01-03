package net.dunotech.venus.system.entity.sys;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import net.dunotech.venus.system.entity.common.IdEntity;
import org.springframework.stereotype.Component;

@TableName("sys_dictionary")
@Component
public class SysDictionary extends IdEntity {

    @TableField(value = "id")
    @TableId(type = IdType.UUID)
    private String id;

    @TableField(value = "dic_code")
    private String dicCode;

    @TableField(value = "dic_num")
    private String dicNum;

    @TableField(value = "value_code")
    private String valueCode;

    @TableField(value = "value_name")
    private String valueName;

    @TableField(value = "parent_id")
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicNum() {
        return dicNum;
    }

    public void setDicNum(String dicNum) {
        this.dicNum = dicNum;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
