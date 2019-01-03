package net.dunotech.venus.system.handle;

import net.dunotech.venus.system.service.exception.*;
import net.dunotech.venus.system.utils.JacksonJsonUtil;
import net.dunotech.venus.system.utils.ResultMsg;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class RestExceptionHandle {

    private Logger log = LoggerFactory.getLogger(RestExceptionHandle.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus
    public String runtimeExceptionHandle(Exception e){
        e.printStackTrace();
        String errorMsg;
        Integer code = 500;
        if(e instanceof ArgumentNotValidException){
            code = 10100;
            errorMsg = "校验失败：";
            errorMsg = errorMsg+e.getMessage();
        }else if(e instanceof UserAlreadyExistException){
            code = 10000;
            errorMsg = "用户新增失败："+e.getMessage();
        }else if(e instanceof OssConfigException){
            code = 10201;
            errorMsg = e.getMessage();
        }else if(e instanceof OssUploadException){
            code = 10202;
            errorMsg = e.getMessage();
        }else if(e instanceof DataInsertFailedException){
            code = 10301;
            errorMsg = e.getMessage();
        }else if(e instanceof DataUpdateFailedException){
            code = 10302;
            errorMsg = e.getMessage();
        }else if(e instanceof DataDeleteFailedException){
            code = 10303;
            errorMsg = e.getMessage();
        }else if(e instanceof LocateUploadException){
            code = 10400;
            errorMsg = e.getMessage();
        }else if(e instanceof InputParamInvalidateException){
            code = 10500;
            errorMsg = e.getMessage();
        }else if(e instanceof DataExportException){
            code = 10600;
            errorMsg = e.getMessage();
        }else if(e instanceof DataImportException){
            code = 10700;
            errorMsg = e.getMessage();
        }else if(e instanceof MultipartException){
            if(e.getCause().getCause() instanceof FileUploadBase.FileSizeLimitExceededException){
                code = 10800;
                errorMsg = "上传文件大小超过允许最大值";
            }else {
                errorMsg = e.getMessage();
            }
        }else{
            errorMsg = e.getMessage();
        }
        e.printStackTrace();
        return JacksonJsonUtil.beanToJson(ResultMsg.getErrorReturn(e,errorMsg,code));
    }
}
