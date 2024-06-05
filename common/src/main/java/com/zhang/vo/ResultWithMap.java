package com.zhang.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/5/14
 * @Description
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//null不展示
public class ResultWithMap<T> implements Serializable {

    private Base base;
    private Map<String, T> map;

    public static <T> ResultWithMap<T> OK(Map<String,T> Map) {
        return new ResultWithMap<>(new Base(10000,"success"),Map);
    }

    public static ResultWithMap<String> Fail() {
        return new ResultWithMap<>(new Base(-1,"fail"),null);
    }
}
