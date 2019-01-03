package net.dunotech.venus.system.service.common.select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import net.dunotech.venus.system.dto.common.CodeElement;
import net.dunotech.venus.system.mapper.select.DefaultSelectMapper;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共通下拉框值获取service
 * @author dunoinfo
 * @since 2018/09/30
 */
@Service
public class CommonSelectService {

    private static final String QUERY_FIELD_NAME = "queryFieldName";

    private static final String ENTITY_NAME = "entityName";

    private static final String FLEX_SET_CODE = "flexSetCode";

    private static final String FIELD = "field";

    private static final String VALUE = "value";

    private static final String SELECT_PREX = "select";

    private static final String SELECT_SUFFIX = "CodeList";

    private static final String MAPPER_SUFFIX = "Mapper";

    @Autowired
    private DefaultSelectMapper defaultSelectMapper;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * 查询字典表，获取下拉框值
     * @param flexSetCode
     * @param param
     * @return
     */
    public List<CodeElement> selectOptionsFromDictionary(String flexSetCode,Map<String,Object> param){
        Map<String,Object> queryParam = getQueryParam(param);
        queryParam.put(FLEX_SET_CODE,flexSetCode);
        queryParam.remove(ENTITY_NAME);
        return defaultSelectMapper.selectOptionsFromDict(queryParam);

    }

    /**
     * 查询自定义表获取下拉框值
     * @param entityName
     * @param param
     * @return
     * @throws Exception
     */
    public List selectOptionsCustom(String entityName,Map<String,Object> param) throws Exception{
        Map<String,Object> queryParam = getQueryParam(param);
        BaseMapper baseMapper = getEntityMapper(entityName);
        Method method = baseMapper.getClass().getDeclaredMethod(SELECT_PREX+entityName+SELECT_SUFFIX, Map.class);
        List<?> list = (List) method.invoke(baseMapper, queryParam);
        return list;
    }

    /**
     * 原始map转成查询可用map
     * @param param
     * @return
     */
    private Map<String,Object> getQueryParam(Map<String,Object> param){
        Map<String,Object> queryParam = new HashMap<>();
        for(Map.Entry<String,Object> entry:param.entrySet()){
            if(entry.getKey().equals(QUERY_FIELD_NAME)){
                String fieldQuery = changeMapToQuerySql((Map<String,String>)entry.getValue());
                queryParam.put("fieldQuery",fieldQuery);
            }else{
                queryParam.put(entry.getKey(),entry.getValue());
            }
        }
        return queryParam;
    }

    /**
     * 将map部分键值对转为sql
     * @param queryField
     * @return
     */
    private String changeMapToQuerySql(Map<String,String> queryField){
        String fieldValue = queryField.get(FIELD);
        String value = queryField.get(VALUE);
        String[] array = fieldValue.split(",");
        String querySql = StringUtils.EMPTY;
        for(int i = 0;i<array.length;i++){
            if(i != 0){
                querySql+=" or t1."+array[i]+" like concat('%','"+value+"','%')";
            }else{
                querySql+="t1."+array[i]+" like concat('%','"+value+"','%')";
            }
        }
        return querySql;
    }

    /**
     * 获取对应Mapper
     * @param entityClass 实体类名
     * @return 实体类名所对应表的操作mapper
     */
    private BaseMapper getEntityMapper(String entityClass){
        // 获取对应Mapper
        entityClass = entityClass.substring(0,1).toLowerCase()+entityClass.substring(1);
        String mapperName = entityClass + MAPPER_SUFFIX;
        return (BaseMapper) applicationContext.getBean(mapperName);
    }
}
