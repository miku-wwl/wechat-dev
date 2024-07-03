package com.weilai.exceptions;


import com.weilai.grace.result.ResponseStatusEnum;

/**
 * 自定义异常
 * 目的：统一的处理异常信息
 *      便于解耦，拦截器、service、controller等异常信息的解耦
 *      不会被service返回的类型而限制
 */
public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码为：" + responseStatusEnum.status() +
                "异常信息为：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }
    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
