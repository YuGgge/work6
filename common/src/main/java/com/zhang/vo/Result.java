package com.zhang.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@lombok.Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//null不展示
public class Result<T> implements Serializable {

    private Object base;
    private T data;


    public Result(Object base) {
        this.base = base;
    }

    public Result(Object base, T data) {
        this.base = base;
        this.data = data;
    }

    public static <T> Result<T> OK(T data) {
        return new Result<T>(new Base(10000,"success"),data);
    }


    public static <T> Result<T> OK() {
        return new Result<T>(new Base(10000,"success"));
    }



    public static <T> Result<T> Fail() {
        return new Result<T>(new Base(-1,"执行失败"));
    }


    public static <T> Result<T> Fail(String msg) {
        return new Result<T>(new Base(-1,msg));
    }
}



