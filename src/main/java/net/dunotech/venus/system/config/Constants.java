package net.dunotech.venus.system.config;

/**
 * 共通静态变量类
 * @author dunoinfo
 * @since 2018/09/30
 */
public class Constants {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String SELECT_SUCCESS = "获取成功";

    public static final String SELECT_FAILURE = "获取失败";

    public static final String INSERT_SUCCESS = "新增成功";

    public static final String INSERT_FAILURE = "新增失败";

    public static final String UPDATE_SUCCESS = "更新成功";

    public static final String UPDATE_FAILURE = "更新失败";

    public static final String DELETE_SUCCESS = "删除成功";

    public static final String DELETE_FAILURE = "删除失败";

    public static final Integer RESPONSE_STATUS_CODE_SUCCESS = 200;
    public static final Integer RESPONSE_STATUS_CODE_FAILED_COMMON = 10000;

    public static final String SUCCESS_CORE = "1";
    public static final String FAILURECORE = "0";

    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 监听操作action
     */
    public static final String ACTION_INSERT = "insert";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_DELETEBATCH = "deleteBatch";
    public static final String ACTION_SELECT_PAGE = "selectPage";
    public static final String ACTION_SELECT_ID = "selectId";

    /**
     * 监听操作阶段
     */
    public static final String PHASE_BEFORE = "before";
    public static final String PHASE_AFTER = "after";

    /**
     * 监听参数key
     */
    public static final String CRUD_PARAM = "crudParam";
    public static final String CRUD_ENTITY = "entity";
    public static final String CRUD_RESULT = "result";
    public static final String CRUD_ID = "id";

    /**
     * 日志操作类型
     */
    public static final String LOG_ACTION_INSERT = "1";
    public static final String LOG_ACTION_MODIFY = "2";
    public static final String LOG_ACTION_DELETE = "3";
    public static final String LOG_ACTION_SELECT = "4";
    public static final String LOG_ACTION_LOGIN = "5";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "000000";

    /**
     * 上传参数key
     */
    public static final String UPLOAD_INFO_URL = "url";
    public static final String UPLOAD_INFO_REAL_NAME = "realName";
    public static final String UPLOAD_INFO_SHOW_NAME = "showName";
}
