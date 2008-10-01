package com.blueskyminds.struts2.securityplugin.model;

import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.framework.tools.Named;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;

/**
 * Date Started: 9/05/2008
 */
@Entity
@Table(name="auth_UserRole")
public class UserRole extends AbstractDomainObject implements Named {

    private String name;

    public UserRole(String name) {
        this.name = name;
    }

    public UserRole() {
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
