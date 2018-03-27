package com.springcloud.framework.config.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "properties_file_id")
    private Long propertiesFileId;
    private String application;
    private String profile;
    private String label;
    @Column(name = "properties_key")
    private String key;
    @Column(name = "properties_value")
    private String value;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "create_user_id")
    private String createUserId;
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    @Column(name = "last_update_user_id")
    private String lastUpdateUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertiesFileId() {
        return propertiesFileId;
    }

    public void setPropertiesFileId(Long propertiesFileId) {
        this.propertiesFileId = propertiesFileId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }
}
