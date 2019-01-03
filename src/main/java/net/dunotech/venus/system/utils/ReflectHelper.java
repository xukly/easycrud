/*
 * ====================================================================
 * OA系统
 * ====================================================================
 */

package net.dunotech.venus.system.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;

/**
 * 利用反射对dto设值
 *
 * @author dunoinfo
 * @date 2016/11/25
 */
public class ReflectHelper {

    private static Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

    private ReflectHelper() {

    }

    public static Object getFieldValue(Object obj, String fieldName) {

        if (obj == null) {
            return null;
        }

        Field targetField = getTargetField(obj.getClass(), fieldName);

        try {
            return FieldUtils.readField(targetField, obj, true);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Field getTargetField(Class<?> targetClass, String fieldName) {
        Field field = null;

        try {
            if (targetClass == null) {
                return field;
            }

            if (Object.class.equals(targetClass)) {
                return field;
            }

            field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
            if (field == null) {
                field = getTargetField(targetClass.getSuperclass(), fieldName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return field;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        if (null == obj) {
            return;
        }
        Field targetField = getTargetField(obj.getClass(), fieldName);
        if (null == targetField) {
            logger.error("字段[{}]不存在", fieldName);
            return;
        }

        Object setValue = value;
        if (value == null || StringUtils.EMPTY.equals(value)) {
//            if (targetField.getType() == Double.class) {
//                setValue = new Double("0");
//            }
//            if (targetField.getType() == Integer.class) {
//                setValue = new Integer("0");
//            }
//            if (targetField.getType() == String.class) {
//                setValue = "";
//            }
        } else {
            if (targetField.getType() == Double.class && value.getClass() != Double.class) {
                setValue = Double.valueOf(String.valueOf(value));
            }
            if (targetField.getType() == Integer.class && value.getClass() != Integer.class) {
                setValue = Integer.valueOf(String.valueOf(value));
            }
            if(targetField.getType() == Long.class && value.getClass() != Long.class){
                setValue = Long.valueOf(String.valueOf(value));
            }
            if(targetField.getType() == Timestamp.class && value.getClass() != Timestamp.class){
                setValue = new Timestamp(DateUtil.format(String.valueOf(value),"yyyy-MM-dd HH:mm:ss").getTime());
            }
            if(targetField.getType() == String.class){
                setValue = String.valueOf(value);
            }
        }
        try {
            FieldUtils.writeField(targetField, obj, setValue);
        } catch (Exception e) {
            logger.error("反射设值出错。key:" + fieldName + ",value:" + value);
            logger.error(e.getMessage(), e);
        }
    }

    public static Object getValueByGetMethod(Object obj, String fieldName) {
        Object o = null;
        try {
            Class clazz = obj.getClass();
            Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
            for (Field field : fields) {
                if (fieldName.equals(field.getName())) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method getMethod = pd.getReadMethod();// 获得get方法
                    o = getMethod.invoke(obj);// 执行get方法返回一个Object
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return o;
    }
}
