package com.weilai.exceptions;


import com.weilai.grace.result.ResponseStatusEnum;
/**
 * 优雅的处理异常，统一进行封装
 */
public class GraceException {

    public static void display(ResponseStatusEnum statusEnum) {
        throw new MyCustomException(statusEnum);
    }

}
