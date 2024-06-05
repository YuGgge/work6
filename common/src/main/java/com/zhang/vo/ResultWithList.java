package com.zhang.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhang.entity.Video;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhang
 * @date 2024/5/14
 * @Description
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//null不展示
public class ResultWithList<T> implements Serializable {

    private Base base;
    private Data<T> data;

    public ResultWithList(Base b) {
        this.base=b;
    }


    public static <T> ResultWithList<T> OK(List<T> items, Long total) {
        return new ResultWithList<>(new Base(10000,"success"), new Data<>(items,total));
    }


    public static <T> ResultWithList<T> OK(List<T> items) {
        return new ResultWithList<>(new Base(10000,"success"),new Data<>(items));
    }

    public static <T> ResultWithList<T> Fail() {
        return new ResultWithList<>(new Base(-1,"执行失败"));
    }


    public static <T> ResultWithList<T> Fail(String msg) {
        return new ResultWithList<>(new Base(-1,msg));
    }
}
