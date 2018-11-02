package com.yb.springsecurity.jwt.common;

/**
 * @author yangbiao
 * @Description:返回数据的封装类
 * @date 2018/11/2
 */
public class ResultInfo<T> {
    private static final String MESSAGE_SUCCESS = "success";
    private static final Integer STATUS_SUCCESS = 200;
    private static final Integer STATUS_BADREQUEST = 400;

    private String message;
    private Integer status;
    private T data;

    public static <T> ResultInfo<T> success(T t){
        ResultInfo<T> info = new ResultInfo<>();
        info.setStatus(STATUS_SUCCESS);
        info.setMessage(MESSAGE_SUCCESS);
        info.setData(t);
        return info;
    }

    /**
     * 这个方法基本不会用的,我通常是抛出异常,在controller层统一处理异常的类里再统一定义
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> error(String errorMessage){
        ResultInfo<T> info = new ResultInfo<>();
        info.setStatus(STATUS_BADREQUEST);
        info.setMessage(errorMessage);
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
