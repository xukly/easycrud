package net.dunotech.venus.system.config.mybatisPlus;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.Timestamp;

/**
 * @author dunoinfo
 * @date 2018/6/28.
 */
public class MyMetaObjectHandler extends MetaObjectHandler {

    /**
     * 字段为空自动填充,如果要使填充生效,一定在在实体类对应的字段上设置fill = FieldFill.INSERT字段！
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        Object createTime = getFieldValByName("createTime", metaObject);
        Object modifyTime = getFieldValByName("modifyTime", metaObject);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (createTime == null && metaObject.hasSetter("createTime")) {
            setFieldValByName("createTime", timestamp, metaObject);
        }
        if (modifyTime == null && metaObject.hasSetter("modifyTime")) {
            setFieldValByName("modifyTime", timestamp, metaObject);
        }

    }

    /**
     * 字段自动更新填充,如果要使填充生效,一定在在实体类对应的字段上设置fill = FieldFill.UPDATE字段！
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("modifyTime")){
            setFieldValByName("modifyTime", new Timestamp(System.currentTimeMillis()), metaObject);
        }

    }
}
