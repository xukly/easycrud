package net.dunotech.venus.system.utils;

/**
 * @author fangzhongwei
 *
 */
public final class SystemConstants {

    private SystemConstants() {
    }

    public static final String UTF8_ENCODING = "UTF-8";

    /**
     * HTTP请求方法
     */
    public static final String HTTP_GET = "GET";

    public static final String HTTP_POST = "POST";

    public static final String HTTP_PUT = "PUT";

    public static final String HTTP_DELETE = "DELETE";

    public static final String INTERROGATION_MARK = "?";

    /**
     * http异步ajax请求头中的名称
     */
    public static final String HTTP_AJAX_REQUEST_HEADER_NAME = "X-Requested-With";

    /**
     * http异步ajax请求头中的值
     */
    public static final String HTTP_AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";

    /**
     * Rest请求设置request超时时间，单位S
     */
    public static final int REST_CONNCTION_TIMEOUT = 15;

    /**
     * Rest请求设置response超时时间，单位S
     */
    public static final int REST_SOCKET_TIMEOUT = 15;

    /**
     * 页面错误标签key
     */
    public static final String UI_ERRORS = "uiErrors";

    /**
     * 默认文档类型
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 提示信息文件绑定名
     */
    public static final String MSG_RES_BUNDLE_BASE_NAME = "/i18n/i18n";

    /**
     * 文件上传ContentType前缀
     */
    public static final String MULTIPART_CONTENT_TYPE_PREFIX = "multipart/";

    /**
     * RpcContext中存储pagingDto的key
     */
    public static final String PAGEINGDTO_KEY = "pagingDto";
    /**
     * RpcContext中存储总页数的key
     */
    public static final String TOTAL_PAGE_KEY = "totalPage";
    /**
     * session中存储的导航对象
     */
    public static final String SESSION_NAVIGATION = "sessionNavigation";

    /**
     * session中存储的登陆用户权限信息
     */
    public static final String SESSION_AUTH_CODES = "sessionAuthCodes";

    public static final String IP = "IP";

    /**
     * 分隔符
     */
    public static final String PARAM_SEPARATOR = "|";

    /**
     * 点号分隔符
     */
    public static final char DOT = '.';

    /**
     * 逗号分隔符
     */
    public static final char COMMA = ',';

    /**
     * 换行
     */
    public static final String BR = "<br/>";

    /**
     * &分隔符
     */
    public static final char SPLIT_AND = '&';

    /**
     * =分隔符
     */
    public static final char SPLIT_EQUAL = '=';

    /**
     * _分隔符
     */
    public static final char SPLIT_UNDERLINE = '_';

    /**
     * 斜线分隔符
     */
    public static final char SPLIT_VIRGULE = '/';

    /**
     * -分隔符
     */
    public static final char HYPHEN = '-';

    /**
     * 左方括号
     */
    public static final char LEFT_SQUARE_BRACKETS = '[';

    /**
     * 右方括号
     */
    public static final char RIGHT_SQUARE_BRACKETS = ']';

    /**
     *  导入提示框最大条数为5
     */
    public static final int  PROMPT_FIVE = 5;

}
