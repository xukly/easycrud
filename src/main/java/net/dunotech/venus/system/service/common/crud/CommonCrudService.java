package net.dunotech.venus.system.service.common.crud;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.dto.common.TablePageData;
import net.dunotech.venus.system.dto.common.UploadFileInfoDto;
import net.dunotech.venus.system.entity.common.ThisIsAnEntity;
import net.dunotech.venus.system.entity.oss.OssEntity;
import net.dunotech.venus.system.mapper.oss.OssMapper;
import net.dunotech.venus.system.service.annotation.ExtraProperty;
import net.dunotech.venus.system.service.event.*;
import net.dunotech.venus.system.service.exception.ArgumentNotValidException;
import net.dunotech.venus.system.service.exception.DataDeleteFailedException;
import net.dunotech.venus.system.service.exception.DataInsertFailedException;
import net.dunotech.venus.system.service.exception.DataUpdateFailedException;
import net.dunotech.venus.system.utils.*;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 共通crud service
 * @author dunoinfo
 * @since 2018/09/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommonCrudService {

    private static final String MAPPER_SUFFIX = "Mapper";

    private static final String SELECT_PREX = "select";

    private static final String BY_PAGE_SUFFIX = "ByPage";

    private static final String FILES_PREX = "files__";

    private static final String DEFAULT_ORDER = "defaultOrder";

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    private OssMapper ossMapper;

    private static final Logger log = LoggerFactory.getLogger(CommonCrudService.class);

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    /**
     * 一览查询
     *
     * @param param 请求数据
     * @param pageParam 分页参数
     * @param reportId 业务id
     * @return 一览DataTable信息
     */
    public TablePageData selectDataList(LinkedHashMap<String, Object> param, PageParam pageParam, String reportId) throws Exception {

        // 定义返回结果
        TablePageData result = new TablePageData();

        //初始化分页参数
        Page<?> page = PageParam.initPagination(pageParam.getPageIndex(),pageParam.getPageRows());

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);
        Map<String,Object> orderMap = CommonUtils.getParamWithPrexByOrderPrex(param);
        if(MapUtils.isNotEmpty(orderMap)){
            paramMap.putAll(orderMap);
        }else{
            paramMap.put(DEFAULT_ORDER,Constants.YES);
        }

        //监听事件：查询前的相关操作
        Map<String,Object> paramBefore = new HashMap<>();
        paramBefore.put(Constants.CRUD_PARAM,paramMap);
        CommonApplicationEvent aeb = new CommonApplicationEvent(this,paramBefore,Constants.ACTION_SELECT_PAGE,reportId,Constants.PHASE_BEFORE);
        applicationContext.publishEvent(aeb);

        // 执行SQL
        Object mapper = getEntityMapper(reportId);
        Method method = mapper.getClass().getDeclaredMethod(SELECT_PREX+reportId+BY_PAGE_SUFFIX, Page.class, Map.class);
        List<?> list = (List) method.invoke(mapper, page, paramMap);

        //监听事件：查询后的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_PARAM,paramMap);
        paramAfter.put(Constants.CRUD_RESULT,list);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_SELECT_PAGE,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);

        //获取完整分页信息
        pageParam.setTotalRows(page.getTotal());
        result.setPageParam(pageParam);
        result.setTableData(list);

        return result;
    }

    /**
     * 数据增加
     *
     * @param param param
     * @param reportId 业务id
     * @return 增加数据ID
     */
    public Map<String,Object> addData(Map<String,Object> param, String reportId) throws Exception {

        Map<String,Object> result = new HashMap<>();

        // 获取画面输入信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 实例化对象,通过反射赋值
        String entityFullName = getEntityFullNameByName(reportId);
        if(entityFullName==null){
            return result;
        }
        Object entity = Class.forName(entityFullName).newInstance();
        boolean hasExtra = false;
        String extraPropertyName = StringUtils.EMPTY;
        for (Field field : getFields(entity)) {
            Object value = paramMap.get(field.getName());
            if(value!= null && StringUtils.isNotEmpty(String.valueOf(value))) {
                ReflectHelper.setFieldValue(entity, field.getName(), value);
            }
            //通过注解@ExtraProperty判断自定义字段
            if(field.isAnnotationPresent(ExtraProperty.class)){
                hasExtra = true;
                extraPropertyName = field.getName();
            }
        }


        setExtraProperty(hasExtra,paramMap,entity,extraPropertyName);

        //监听事件：新增前的相关操作
        Map<String,Object> paramBefore = new HashMap<>();
        paramBefore.put(Constants.CRUD_ENTITY,entity);
        paramBefore.put(Constants.CRUD_PARAM,paramMap);
        CommonApplicationEvent aeb = new CommonApplicationEvent(this,paramBefore,Constants.ACTION_INSERT,reportId,Constants.PHASE_BEFORE);
        applicationContext.publishEvent(aeb);

        // 获取对应Mapper
        String message = validate(entity);
        if(StringUtils.isNotEmpty(message)){
            throw new ArgumentNotValidException(message);
        }
        BaseMapper mapper = getEntityMapper(reportId);
        int cnt = mapper.insert(entity);

        if(cnt==1){
            Map<String,Object> fileParam = CommonUtils.getParamByFilesPrex(param);
            addUploadAttachment(mapper,fileParam,entity);
        }

        //监听事件：新增前的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_ENTITY,entity);
        paramAfter.put(Constants.CRUD_PARAM,paramMap);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_INSERT,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);

        String entityId = "";
        if (cnt == 1) {
            entityId = String.valueOf(ReflectHelper.getFieldValue(entity, "id"));
        }else{
            throw new DataInsertFailedException("新增数据失败");
        }

        result.put("id",entityId);
        return result;
    }

    /**
     * 新增上传记录
     * @param baseMapper
     * @param fileParam
     * @param entity
     */
    private void addUploadAttachment(BaseMapper baseMapper,Map<String,Object> fileParam,Object entity){
        String id = String.valueOf(ReflectHelper.getFieldValue(entity,"id"));
        for(Map.Entry<String,Object> entry:fileParam.entrySet()){
            String url = "";
            List<LinkedHashMap> fileInfoDtos = (List<LinkedHashMap>)entry.getValue();
            for(LinkedHashMap map:fileInfoDtos){
                String urlElement = String.valueOf(map.get(Constants.UPLOAD_INFO_URL));
                String realName = String.valueOf(map.get(Constants.UPLOAD_INFO_REAL_NAME));
                String showName = String.valueOf(map.get(Constants.UPLOAD_INFO_SHOW_NAME));
                OssEntity ossEntity = new OssEntity();
//                ossEntity.setData();
                ossEntity.setParentId(id);
                ossEntity.setReportId(entry.getKey());
                ossEntity.setUrl(urlElement);
                ossEntity.setRealName(realName);
                ossEntity.setShowName(showName);
                int cnt = ossMapper.insert(ossEntity);
                if(cnt != 1){
                    throw new DataInsertFailedException("上传文件记录新增失败");
                }
                url+=urlElement+",";
            }
            if(url.length()>0){
                url = url.substring(0,url.length()-1);
                for (Field field : getFields(entity)) {
                    if(entry.getKey().equals(field.getName()))
                    ReflectHelper.setFieldValue(entity, field.getName(), url);
                }
            }else{
                for (Field field : getFields(entity)) {
                    if(entry.getKey().equals(field.getName()))
                        ReflectHelper.setFieldValue(entity, field.getName(), JacksonJsonUtil.beanToJson(entry.getValue()));
                }
            }
            int cnt = baseMapper.updateById(entity);
            if(cnt != 1){
                throw new DataUpdateFailedException("字段"+entry.getKey()+"更新失败");
            }
        }

    }

    /**
     * 数据删除（）
     *
     * @param id 数据唯一ID
     * @param reportId 业务id
     * @return 删除数据条数
     */
    public int delData(String id, String reportId) {
        //监听事件：删除前的相关操作
        Map<String,Object> paramBefore = new HashMap<>();
        paramBefore.put(Constants.CRUD_ID,id);
        CommonApplicationEvent aeb = new CommonApplicationEvent(this,paramBefore,Constants.ACTION_DELETE,reportId,Constants.PHASE_BEFORE);
        applicationContext.publishEvent(aeb);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);
        Object entity = mapper.selectById(id);
        int cnt = 0;
        if(entity != null) {
            ReflectHelper.setFieldValue(entity, "delFlg", Constants.YES);
            cnt = mapper.updateById(entity);
            if(cnt != 1){
                throw new DataDeleteFailedException("数据删除失败");
            }
        }

        //监听事件：删除后的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_ID,id);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_DELETE,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);

        return cnt;
    }

    /**
     * 批量数据删除
     *
     * @param ids 数据唯一ID集合
     * @return 删除数据条数
     */
    public int delDataAll(String ids, String reportId) {

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        int cnt = 0;

        if(StringUtils.isNotEmpty(ids)){
            String[] idArr = ids.split(",");
            List<String> idList = Lists.newArrayList(idArr);
           for(String id:idList){
               Object entity = mapper.selectById(id);
               if(entity != null) {
                   ReflectHelper.setFieldValue(entity, "delFlg", Constants.YES);
                   cnt += mapper.updateById(entity);
               }
           }
           if(cnt != idList.size()){
               throw new DataDeleteFailedException("数据批量更新失败");
           }
        }

        //监听事件：新增前的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_ID,ids);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_DELETEBATCH,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);
        return cnt;
    }

    /**
     * 根据id删除数据
     * @param id
     * @param reportId
     * @return
     */
    public int delDataById(String id,String reportId){
        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.deleteById(id);
    }

    /**
     * 根据id删除数据
     * @param ids
     * @param reportId
     * @return
     */
    public int delAllDataById(String ids,String reportId){
        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        String[] idArray = ids.split(",");
        List<String> idList = Lists.newArrayList(idArray);

        return mapper.deleteBatchIds(idList);
    }

    /**
     * 根据条件删除记录
     * @param param
     * @param reportId
     * @return
     */
    public int delDataByMap(Map<String,Object> param,String reportId){

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.deleteByMap(paramMap);
    }

    /**
     * 获取对象字段。前提
     *
     * @param obj 目标对象
     * @return 对象字段list
     */
    private List<Field> getFields(Object obj) {
        List<Field> result = new ArrayList<Field>();

        // 获取所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        if(fields.length==0){
            fields = obj.getClass().getSuperclass().getDeclaredFields();
        }
        for (Field field : fields) {

            // 获取字段所有的注解
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {

                // 找到TableField的注解
                if (annotation.annotationType() == TableField.class) {
                    TableField tableField = (TableField) annotation;

                    // 如果注解标识的字段在DB中存在，则作为赋值对象
                    if (tableField.exist()) {
                        result.add(field);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 数据查询
     *
     * @param id 数据唯一ID
     * @param reportId 报表ID
     * @return 增加数据条数
     */
    public Object selectData(String id, String reportId) throws Exception {

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        List<OssEntity> ossEntities = ossMapper.selectList(new EntityWrapper<OssEntity>().where("del_flg = {0}","0").and("parent_id = {0}",id));
        List<String> dataList = new ArrayList<>();
        Map<String,List<UploadFileInfoDto>> fileData = new HashMap<>();
        for(OssEntity ossEntity:ossEntities){
            UploadFileInfoDto uploadFileInfoDto = new UploadFileInfoDto(ossEntity.getShowName(),ossEntity.getRealName(),ossEntity.getRealName().substring(ossEntity.getRealName().lastIndexOf(".")),ossEntity.getUrl());
            if(dataList.contains(ossEntity.getReportId())){
                fileData.get(ossEntity.getReportId()).add(uploadFileInfoDto);
            }else{
                dataList.add(ossEntity.getReportId());
                List<UploadFileInfoDto> element = new ArrayList<>();
                element.add(uploadFileInfoDto);
                fileData.put(ossEntity.getReportId(),element);
            }
        }
        Object obj = mapper.selectById(id);
        for(Field field:getFields(obj)){
            Object o =  fileData.get(field.getName());

            if(o!=null){
                String value = JacksonJsonUtil.beanToJson(o);
                ReflectHelper.setFieldValue(obj, field.getName(), value);
            }
        }

        Map<String,String> extraPropertyMap = new HashMap<>();
        for (Field field : getFields(obj)) {
            if(field.isAnnotationPresent(ExtraProperty.class)){
                String extraProperty = String.valueOf(ReflectHelper.getFieldValue(obj,field.getName()));
                if(StringUtils.isNotEmpty(extraProperty) && !extraProperty.equals("null") ){
                    extraPropertyMap = (Map<String,String>)JacksonJsonUtil.jsonToBean(extraProperty,Map.class);
                }
            }
        }

        //监听事件：删除后的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_ID,id);
        paramAfter.put(Constants.CRUD_ENTITY,obj);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_SELECT_ID,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);

        Map<String,String> result = BeanUtils.describe(obj);
        result.putAll(extraPropertyMap);

        return obj;
    }

    /**
     * 获得上传的文件
     * @param id
     * @param key
     * @return
     */
    public List<UploadFileInfoDto> getUploadFiles(String id,String key){
        List<UploadFileInfoDto> uploadFileInfoDtos = new ArrayList<>();
        List<OssEntity> ossEntities = ossMapper.selectList(new EntityWrapper<OssEntity>().where("del_flg = {0}","0").and("parent_id = {0}",id).and("report_id = {0}",key));
        for(OssEntity ossEntity:ossEntities){
            UploadFileInfoDto uploadFileInfoDto = new UploadFileInfoDto(ossEntity.getShowName(),ossEntity.getRealName(),ossEntity.getRealName().substring(ossEntity.getRealName().lastIndexOf(".")),ossEntity.getUrl());
            uploadFileInfoDtos.add(uploadFileInfoDto);
        }
        return uploadFileInfoDtos;
    }

    /**
     * 根据多个id做批量查询
     * @param ids id集合
     * @param reportId 报表ID
     * @return 查询结果
     */
    public List selectBatchDatas(String ids,String reportId){
        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);
        String[] idArray = ids.split(",");
        List<String> idList = Lists.newArrayList(idArray);
        return mapper.selectBatchIds(idList);
    }

    /**
     * 根据参数获得相应记录的数量
     * @param param
     * @param reportId
     * @return
     */
    public Integer selectDataCount(Map<String,Object> param,String reportId){

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.selectCount(new EntityWrapper().allEq(paramMap));
    }

    /**
     * 根据参数获得相应记录
     * @param param
     * @param reportId
     * @return
     */
    public List selectDataList(Map<String,Object> param,String reportId){

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.selectList(new EntityWrapper().allEq(paramMap));
    }

    /**
     * 根据参数获得相应Map类型的记录
     * @param param
     * @param reportId
     * @return
     */
    public List<Map<String,Object>> selectDataMaps(Map<String,Object> param,String reportId){

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.selectMaps(new EntityWrapper().allEq(paramMap));

    }

    /**
     * 根据参数获得相应分页后的Map类型的记录
     * @param param
     * @param pageParam
     * @param reportId
     * @return
     */
    public TablePageData selectDataMapsPage(Map<String,Object> param,PageParam pageParam,String reportId){

        // 定义返回结果
        TablePageData result = new TablePageData();

        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        //初始化分页参数
        Page<?> page = PageParam.initPagination(pageParam.getPageIndex(),pageParam.getPageRows());

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        List<Map<String,Object>> list = mapper.selectMapsPage(page,new EntityWrapper().allEq(paramMap));

        //获取完整分页信息
        pageParam.setTotalRows(page.getTotal());
        result.setPageParam(pageParam);
        result.setTableData(list);

        return result;
    }

    /**
     * 根据参数获得相应记录
     * @param param
     * @param reportId
     * @return
     */
    public List selectDataByMap(Map<String,Object> param,String reportId){
        // 解析画面传入的信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        return mapper.selectByMap(paramMap);
    }

    /**
     * 数据更新
     *
     * @param param http请求参数
     * @param reportId 业务id
     * @param id 纪录唯一标识id
     * @return 更新数据条数
     */
    public int updateData(Map<String,Object> param, String reportId, String id) throws Exception {

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        // 获取原数据
        Object origData = mapper.selectById(id);

        // 获取画面输入信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        //监听事件：新增前的相关操作
        Map<String,Object> paramBefore = new HashMap<>();
        paramBefore.put(Constants.CRUD_ENTITY,origData);
        paramBefore.put(Constants.CRUD_PARAM,paramMap);
        CommonApplicationEvent aeb = new CommonApplicationEvent(this,paramBefore,Constants.ACTION_UPDATE,reportId,Constants.PHASE_BEFORE);
        applicationContext.publishEvent(aeb);

        // 通过反射赋值
        boolean hasExtra = false;
        String extraPropertyName = StringUtils.EMPTY;
        for (Field field : getFields(origData)) {

            Object newValue = paramMap.get(field.getName());
            if(newValue!= null && StringUtils.isNotEmpty(String.valueOf(newValue))) {
                ReflectHelper.setFieldValue(origData, field.getName(), newValue);
            }
            //通过注解@ExtraProperty判断自定义字段
            if(field.isAnnotationPresent(ExtraProperty.class)){
                hasExtra = true;
                extraPropertyName = field.getName();
            }
        }

        setExtraProperty(hasExtra,paramMap,origData,extraPropertyName);

        String message = validate(origData);
        if(StringUtils.isNotEmpty(message)){
            throw new ArgumentNotValidException(message);
        }
        int cnt = mapper.updateById(origData);

        if(cnt==1){
            Map<String,Object> fileParam = CommonUtils.getParamByFilesPrex(param);
            updateUploadAttachment(mapper,fileParam,origData,id);
        }else{
            throw new DataUpdateFailedException("更新数据未成功");
        }

        //监听事件：新增前的相关操作
        Map<String,Object> paramAfter = new HashMap<>();
        paramAfter.put(Constants.CRUD_ENTITY,origData);
        paramAfter.put(Constants.CRUD_PARAM,paramMap);
        CommonApplicationEvent aea = new CommonApplicationEvent(this,paramAfter,Constants.ACTION_UPDATE,reportId,Constants.PHASE_AFTER);
        applicationContext.publishEvent(aea);

        return cnt;
    }

    private void updateUploadAttachment(BaseMapper mapper,Map<String,Object> fileParam,Object entity,String id){
        List<OssEntity> ossEntities = ossMapper.selectList(new EntityWrapper<OssEntity>().where("del_flg = {0}","0").and("parent_id = {0}",id));
        for(OssEntity ossEntity:ossEntities){
            ossEntity.setDelFlg("1");
            ossMapper.updateById(ossEntity);
        }
        addUploadAttachment(mapper,fileParam,entity);
    }

    /**
     * 根据参数修改记录
     * @param param
     * @param reportId
     * @return
     * @throws Exception
     */
    public int update(Map<String,Object> param,String reportId) throws Exception{
        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);

        // 获取画面输入信息
        Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

        //获取作为参数的信息
        Map<String, Object> paramMaps = CommonUtils.getParamByCondsPrex(param);

        String entityFullName = getEntityFullNameByName(reportId);
        if(entityFullName==null){
            return 0;
        }
        Object entity = Class.forName(entityFullName).newInstance();
        for (Field field : getFields(entity)) {
            Object value = paramMap.get(field.getName());
            if(value != null) {
                ReflectHelper.setFieldValue(entity, field.getName(), value);
            }
        }

        return mapper.update(entity,new EntityWrapper().allEq(paramMaps));
    }

    /**
     * 批量数据更新
     *
     * @param paramList http请求参数
     * @param reportId 业务id
     * @return 更新数据条数
     */
    public int updateDatas(List<Map<String,Object>> paramList, String reportId) throws Exception {

        // 获取对应Mapper
        BaseMapper mapper = getEntityMapper(reportId);
        int cnt = 0;
        for(Map<String,Object> param:paramList) {
            Object id = param.get("id");
            if (id == null) {
                continue;
            }
            String idStr = String.valueOf(id);
            // 获取画面输入信息
            Map<String, Object> paramMap = CommonUtils.getParamByCondPrex(param);

            // 获取原数据
            Object origData = mapper.selectById(idStr);

            // 通过反射赋值
            for (Field field : getFields(origData)) {

                Object newValue = paramMap.get(field.getName());
                if (newValue != null) {
                    ReflectHelper.setFieldValue(origData, field.getName(), newValue);
                }
            }
            cnt += mapper.updateById(origData);
        }

        return cnt;
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

    /**
     * 获得实体类的全名
     * @param name
     * @return
     */
    private String getEntityFullNameByName(String name){
        Map<String,ThisIsAnEntity> result = applicationContext.getBeansOfType(ThisIsAnEntity.class);
        for(Map.Entry<String,ThisIsAnEntity> entityEntry:result.entrySet()){
            if(name.toLowerCase().equals(entityEntry.getKey().toLowerCase())){
                return entityEntry.getValue().getClass().getName();
            }
        }
        return null;
    }

    /**
     * 设置额外属性
     * @param hasExtra
     * @param paramMap
     * @param entity
     * @param extraPropertyName
     */
    private void setExtraProperty(boolean hasExtra,Map<String,Object> paramMap,Object entity,String extraPropertyName){
        List<Field> fields = getFields(entity);
        //添加自定义字段
        Map<String, Object> extraMap = new HashMap<>();
        if(hasExtra) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                boolean isExtraProperty = true;
                for (Field field : fields) {
                    if (entry.getKey().equals(field.getName())) {
                        isExtraProperty = false;
                    }
                }
                if (isExtraProperty) {
                    extraMap.put(entry.getKey(), entry.getValue());
                }
            }
            if(MapUtils.isNotEmpty(extraMap))
            ReflectHelper.setFieldValue(entity, extraPropertyName, JacksonJsonUtil.beanToJson(extraMap));
        }
    }

    /**
     * 列表排序
     */
    public ResultMsg updateNewOrder(String reportId,List<Map<String,Object>> orderParamDtos){
        BaseMapper mapper = getEntityMapper(reportId);
        for(Map<String,Object> orderParamDto:orderParamDtos){
            Object entity = mapper.selectById(String.valueOf(orderParamDto.get("id")));
            for(Field field:getFields(entity)){
                if(field.getName().equals(String.valueOf(orderParamDto.get("sortName")))){
                    ReflectHelper.setFieldValue(entity,field.getName(),orderParamDto.get("sortNo"));
                }
            }
            mapper.updateById(entity);
        }
        return ResultMsg.getResultMsg("更新成功",200);
    }

    public static <T> String validate(T t) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            messageList.add(constraintViolation.getMessage());
        }
        String message = StringUtils.EMPTY;
        for(String m:messageList){
            message+=m+",";
        }
        if(message.length()>0){
            message = message.substring(0,message.length()-1);
        }
        return message;

    }
}
