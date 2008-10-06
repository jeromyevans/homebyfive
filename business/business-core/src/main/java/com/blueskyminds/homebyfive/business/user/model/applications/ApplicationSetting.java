package com.blueskyminds.homebyfive.business.user.model.applications;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;

/**
 * Defines a setting for an application
 * Date Started: 16/05/2008
 */
@Entity
@Table(name="ApplicationSetting")
public class ApplicationSetting extends AbstractDomainObject {

    private Application application;
    private String name;
    private String defaultValue;

    public ApplicationSetting(Application application, String name, String defaultValue) {
        this.application = application;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public ApplicationSetting() {
    }

    @ManyToOne
    @JoinColumn(name="ApplicationId")
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name="Value")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
