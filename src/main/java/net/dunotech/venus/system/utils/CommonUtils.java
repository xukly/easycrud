package net.dunotech.venus.system.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommonUtils {

    public static final String COND_PREX = "cond__";

    public static final String CONDS_PREX = "conds__";

    public static final String FILES_PREX = "files__";

    public static final String ORDER_PREX = "order__";

    /**
     * 根据前缀（cond__）后的相应的参数
     * @param param
     * @return
     */
    public static Map<String,Object> getParamByCondPrex(Map<String,Object> param){
        return getParamByPrex(param,COND_PREX);
    }

    /**
     * 根据前缀（conds__）后的相应的参数
     * @param param
     * @return
     */
    public static Map<String,Object> getParamByCondsPrex(Map<String,Object> param){
        return getParamByPrex(param,CONDS_PREX);
    }

    /**
     * 根据前缀（files__）后的相应的参数
     * @param param
     * @return
     */
    public static Map<String,Object> getParamByFilesPrex(Map<String,Object> param){
        return getParamByPrex(param,FILES_PREX);
    }

    /**
     * 根据前缀（order__）后的相应的参数
     * @param param
     * @return
     */
    public static Map<String,Object> getParamByOrderPrex(Map<String,Object> param){
        return getParamByPrex(param,ORDER_PREX);
    }

    /**
     * 根据前缀（order__）后的相应的参数(包含前缀)
     * @param param
     * @return
     */
    public static Map<String,Object> getParamWithPrexByOrderPrex(Map<String,Object> param){
        return getParamWithPrexByPrex(param,ORDER_PREX);
    }

    /**
     * 根据前缀后的相应的参数
     * @param param
     * @param prex 前缀
     * @return
     */
    private static Map<String,Object> getParamByPrex(Map<String,Object> param,String prex){
        Map<String,Object> result = new LinkedHashMap<>();
        for(Map.Entry<String,Object> entry:param.entrySet()){
            if(entry.getKey().contains(prex)){
                result.put(entry.getKey().substring(prex.length()),entry.getValue());
            }
        }
        return result;
    }

    /**
     * 根据前缀后的相应的参数(包含前缀)
     * @param param
     * @param prex 前缀
     * @return
     */
    private static Map<String,Object> getParamWithPrexByPrex(Map<String,Object> param,String prex){
        Map<String,Object> result = new LinkedHashMap<>();
        for(Map.Entry<String,Object> entry:param.entrySet()){
            if(entry.getKey().contains(prex)){
                result.put(entry.getKey(),entry.getValue());
            }
        }
        return result;
    }

    /**
     * 获得ip
     * @param request
     * @return
     */
    public static String getIpByRequest(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值,第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
