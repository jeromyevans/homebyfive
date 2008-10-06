package com.blueskyminds.enterprise.user.model.users;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.enterprise.user.model.beans.Setting;

import javax.persistence.*;

/**
 * Date Started: 17/05/2008
 */
@Entity
@Table(name="UserApplicationSetting")
public class UserApplicationSetting  extends AbstractDomainObject implements Setting {

    private UserApplicationMap userApplicationMap;
    private String name;
    private String value;

    public UserApplicationSetting(UserApplicationMap userApplicationMap, String name, String value) {
        this.userApplicationMap = userApplicationMap;
        this.name = name;
        this.value = value;
    }

    public UserApplicationSetting() {
    }

    @ManyToOne
    @JoinColumn(name="UserApplicationMapId")
    public UserApplicationMap getUserApplicationMap() {
        return userApplicationMap;
    }

    public void setUserApplicationMap(UserApplicationMap userApplicationMap) {
        this.userApplicationMap = userApplicationMap;
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
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

