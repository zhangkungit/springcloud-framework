package com.springcloud.framework.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2018/4/9
 * @description
 */
@ApiModel
public class ScrollData<T> implements Serializable {

    private static final long serialVersionUID = -4706066123609169456L;

    @ApiModelProperty("分页结果scrollId，后续请求直接传scrollId")
    private String scrollId;

    @ApiModelProperty("分页结果")
    private List<T> data;

    @ApiModelProperty("总条目数量")
    private Long total;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
