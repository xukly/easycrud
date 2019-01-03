package net.dunotech.venus.system.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * @author fangzhongwei
 * 
 */
public class RestServer {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    public RestServer(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String getRestData() throws RestException {
        if (null == request) {
            return null;
        }

        String httpMethod = request.getMethod();
        String ret = null;
        if (SystemConstants.HTTP_GET.equalsIgnoreCase(httpMethod) || SystemConstants.HTTP_DELETE.equalsIgnoreCase(httpMethod)) {
            ret = request.getQueryString();
        } else {
            ret = getBodyData();
        }

        return RestCodec.decodeBase64(ret);
    }


    private String getBodyData() throws RestException {
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        // String line;
        char[] inputBuff = new char[1024];
        int len = -1;
        try {
            reader = request.getReader();
            while (-1 != (len = reader.read(inputBuff))) {
                buffer.append(inputBuff, 0, len);
            }
            // 解决Ajax的post请求参数长度大于reader的缓冲, 获取参数错误
            // while (null != (line = reader.readLine())) {
            // buffer.append(line);
            // }
            return buffer.toString();
        } catch (IOException e) {
            throw RestException.withError(e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw RestException.withError(e);
                }
            }
        }
    }
}
