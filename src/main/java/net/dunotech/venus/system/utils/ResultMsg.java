package net.dunotech.venus.system.utils;


import net.dunotech.venus.system.dto.common.ApiErrorReturn;

import java.util.HashMap;
import java.util.Map;

public class ResultMsg implements java.io.Serializable {
	/**   
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)    
	*/
	private static final long serialVersionUID = 1L;

	private String msg; //返回执行结果信息
	private Integer code;//返回状态码：success二选一
	private Object data;//返回数据集:objData二选一

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 提供静态工厂创建实例，获得一个返回结果集
	 * @param msg
	 * @param data
	 * @param code
	 * @return
	 */
	public static ResultMsg getResultMsg(String msg,Object data,Integer code) {
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setCode(code);
		resultMsg.setMsg(msg);
		resultMsg.setData(data);
		return resultMsg;
	}

	/**
	 * 提供静态工厂创建实例，获得一个返回结果集
	 *
	 * @param <T>
	 * @param msg
	 * @param
	 * @returnd
	 */
	public static ResultMsg getResultMsg(String msg,Integer code) {
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setCode(code);
		resultMsg.setMsg(msg);
		return resultMsg;
	}

	public static ApiErrorReturn getErrorReturn(Throwable throwable,String msg,Integer code){
		ApiErrorReturn apiErrorReturn = ApiErrorReturn.newInstance();

		String detail = StringUtils.EMPTY;
		Map<String,Object> data = new HashMap<>();
		for(int i = 0;i<throwable.getStackTrace().length;i++){
			detail+=throwable.getStackTrace()[i].toString()+"\n";
		}
		data.put("detail",detail);
		data.put("error",throwable.getMessage());
		data.put("time",System.currentTimeMillis());
		apiErrorReturn.setData(JacksonJsonUtil.beanToJson(data));
		apiErrorReturn.setCode(code);
		apiErrorReturn.setMsg(msg);
		return apiErrorReturn;
	}
	
}
