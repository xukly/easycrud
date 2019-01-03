package net.dunotech.venus.system.mapper.select;

import net.dunotech.venus.system.dto.common.CodeElement;

import java.util.List;
import java.util.Map;

/**
 * 默认查询接口方法
 * @author dunoinfo
 * @since 2018/09/30
 */
public interface DefaultSelectMapper {

    /**
     * 字典表获取数据方法
     * @param param
     * @return
     */
    List<CodeElement> selectOptionsFromDict(Map<String, Object> param);
}
