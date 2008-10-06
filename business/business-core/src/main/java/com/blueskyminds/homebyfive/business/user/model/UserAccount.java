package com.blueskyminds.homebyfive.business.user.model;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.security.Principal;

/**
 * Defines an account for a user.
 *
 * The structure is deliberably consistent with the requirements of Tomcat's DataSourceRealm
 *
 * This class is final so it can't be extended into an unintended entity relationship
 *
 * Date Started: 9/05/2008
 */
@Entity
@Table(name="auth_UserAccount")
public class UserAccount extends AbstractDomainObject implements Principal {

    private String username;
    private String hashedPassword;
    private Set<UserAccountRoleMap> userRoleMaps;

    public UserAccount(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        userRoleMaps = new HashSet<UserAccountRoleMap>();
    }

    public UserAccount() {
        userRoleMaps = new HashSet<UserAccountRoleMap>();
    }

    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @OneToMany(mappedBy="userAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected Set<UserAccountRoleMap> getUserRoleMaps() {
        return userRoleMaps;
    }

    protected void setUserRoleMaps(Set<UserAccountRoleMap> userRoleMaps) {
        this.userRoleMaps = userRoleMaps;
    }

    /**
     * Get the set of UserRole's mapped to this account
     *
     * @return
     */
    @Transient
    public Set<UserRole> getUserRoles() {
        return new HashSet<UserRole>(FilterTools.getTransformed(userRoleMaps, new Transformer<UserAccountRoleMap, UserRole>() {
            public UserRole transform(UserAccountRoleMap fromObject) {
                return fromObject.getUserRole();
            }
        }));
    }

    public boolean isUserInRole(final String role) {
        if (role != null) {
            final Set<UserRole> userRoles = getUserRoles();
            return FilterTools.matchesAny(userRoles, new Filter<UserRole>() {
                public boolean accept(UserRole object) {
                    return object.getName().equals(role);
                }
            });
        } else {
            return false;
        }
    }

    @Transient
    public String getName() {
        return username;
    }

    /**
     * Get an array containing the names of roles assigned to this User
     *
     * @return an array containing 0 or more role names
     */
    @Transient
    public String[] getRoles() {
        List<String> roleList = FilterTools.getTransformed(userRoleMaps, new Transformer<UserAccountRoleMap, String>() {
            public String transform(UserAccountRoleMap map) {
                return map.getUserRole().getName();
            }
        });
        String[] roles = new String[roleList.size()];
        return roleList.toArray(roles);
    }

    /** Modifiy the current set of roles to match the list provided. Roles will be added or removed accordingly */
    @Transient
    public void setRoles(List<UserRole> userRoles) {
        Set<UserAccountRoleMap> toRemove = new HashSet<UserAccountRoleMap>();
        Set<UserAccountRoleMap> toAdd = new HashSet<UserAccountRoleMap>();
        
        for (UserAccountRoleMap map : userRoleMaps) {
            boolean found = false;
            // search by rolename equality
            for (UserRole role : userRoles) {
                if (role.getName().equals(map.getUserRole().getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                toRemove.add(map);
            }
        }

        for (UserRole useRole : userRoles) {
            if (!isUserInRole(useRole.getName())) {
                toAdd.add(new UserAccountRoleMap(this, useRole));
            }
        }

        if (toRemove.size() > 0) {
            userRoleMaps.removeAll(toRemove);
        }
        if (toAdd.size() > 0) {
            userRoleMaps.addAll(toAdd);
        }
    }
}


