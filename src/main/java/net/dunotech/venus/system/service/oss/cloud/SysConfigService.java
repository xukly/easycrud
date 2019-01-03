package net.dunotech.venus.system.service.oss.cloud;

import net.dunotech.venus.system.config.oss.CloudStorageConfig;
import net.dunotech.venus.system.utils.ReflectHelper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class SysConfigService {
    public CloudStorageConfig getConfigObject(Map key, Class<?> cls){
        try {
            Object obj=cls.newInstance();
            for (Field field : cls.getDeclaredFields()) {
                Object value = key.get(field.getName());
                if(value != null){
                    ReflectHelper.setFieldValue(obj, field.getName(), value);
                }
            }
            return (CloudStorageConfig)obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
