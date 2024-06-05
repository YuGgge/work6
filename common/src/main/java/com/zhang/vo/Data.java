package com.zhang.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data<T> {
    private List<T> items;
    private Long total;

    public Data() {
    }
    public Data(List<T> items, Long total) {
        this.items = items;
        this.total = total;
    }

    public Data(List<T> items) {
        this.items = items;
    }

}
