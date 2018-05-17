package com.springcloud.framework.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@ApiModel
public class ScrollRequest<F> implements Serializable {
    @ApiModelProperty("scrollId")
    private String scrollId = StringUtils.EMPTY;

    @ApiModelProperty("返回条目数量 默认值10")
    private Integer limit = 10;

    private F filter;
    private String group;
    private String sort;
    @ApiModelProperty("高亮标签-起始")
    private String preTag;
    @ApiModelProperty("高亮标签-结束")
    private String postTag;

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public F getFilter() {
        return filter;
    }

    public void setFilter(F filter) {
        this.filter = filter;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPreTag() {
        return preTag;
    }

    public void setPreTag(String preTag) {
        this.preTag = preTag;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

}
