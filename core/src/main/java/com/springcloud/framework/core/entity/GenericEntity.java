package com.springcloud.framework.core.entity;

import java.io.Serializable;

/**
 * Long类型id主键基础接口
 */
public abstract class GenericEntity extends BaseEntity implements Serializable {

    //主键ID
    private Long id;

    //唯一ID
    private Long kid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKid() {
        return kid;
    }

    public void setKid(Long kid) {
        this.kid = kid;
    }

}
