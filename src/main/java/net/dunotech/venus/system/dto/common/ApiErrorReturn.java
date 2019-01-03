package net.dunotech.venus.system.dto.common;

import java.io.Serializable;

public class ApiErrorReturn implements Serializable{

    private ApiErrorReturn(){}

    public static ApiErrorReturn newInstance(){
        return new ApiErrorReturn();
    }

    private String msg;

    private Integer code;

    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
