package net.dunotech.venus.system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fangzhongwei
 * 
 */
public final class RestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RestUtil.class);


    public static String getRestData(HttpServletRequest request, HttpServletResponse response) {
        try {
            return new RestServer(request, response).getRestData();
        } catch (RestException e) {
            LOG.error("Get request rest data failed.", e);
            return null;
        }
    }

}
