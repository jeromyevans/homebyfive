package com.blueskyminds.homebyfive.business.user.model;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * Maps a Role to a UserAccount
 *
 * The implementation includes a copy of the username and role name for compatibility the Tomcat's DataSourceRealm
 *
 * Date Started: 9/05/2008
 */
@Entity
@Table(name="auth_UserAccountRoleMap")
public class UserAccountRoleMap extends AbstractEntity {

    private UserAccount userAccount;
    private String username;
    private UserRole userRole;
    private String rolename;

    public UserAccountRoleMap(UserAccount userAccount, UserRole userRole) {
        this.userAccount = userAccount;
        this.userRole = userRole;
    }

    protected UserAccountRoleMap() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserAccountId")
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;        
    }

    @Basic
    protected String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="UserRoleId")
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Basic
    protected String getRolename() {
        return rolename;
    }

    protected void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @PrePersist
    void prePersist() {
        if (userRole != null) {
            this.rolename = userRole.getName();
        } else {
            this.rolename = null;
        }

        if (userAccount != null) {
            this.username = userAccount.getName();
        } else {
            this.username = null;
        }
    }
}
