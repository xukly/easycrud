package net.dunotech.venus.system.entity.common;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 公共实体继承类，存放数据库公共字段
 * @author dunoinfo
 * @since  2018/09/28
 */
public class IdEntity implements Serializable,ThisIsAnEntity{

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 最近修改时间
     */
    @TableField(value = "modify_time",fill = FieldFill.INSERT_UPDATE)
    private Timestamp modifyTime;

    /**
     * 删除逻辑（0、正常；1、删除）
     */
    @TableField(value = "del_flg")
    private String delFlg;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }
}
