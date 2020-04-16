package com.wj.manager.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "QueryParams",description = "查询条件的封装")
public class QueryParams<T> implements Serializable {

    @ApiModelProperty(name = "page",value = "当前页",example = "1")
    private Integer page;

    @ApiModelProperty(name = "size",value = "每页记录数",example = "10")
    private Integer size;

    @ApiModelProperty(name = "keyword",value = "关键字",example = "张三")
    private String keyword;

    @ApiModelProperty
    private T entity;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPage() {
        //要把null放前面，不然，如果page为null，执行null<=0 会报错
        if(this.page == null || this.page <= 0 ){
            this.page = 1;
        }
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        if(this.size == null || this.size <= 0 ){
            this.size = 10;
        }
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
