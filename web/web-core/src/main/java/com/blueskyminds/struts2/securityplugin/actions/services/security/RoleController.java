package com.blueskyminds.struts2.securityplugin.actions.services.security;

import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.DefaultHttpHeaders;
import com.blueskyminds.homebyfive.business.user.model.UserRole;
import com.blueskyminds.homebyfive.business.user.services.UserAccountService;
import com.opensymphony.xwork2.ModelDriven;
import com.google.inject.Inject;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * CRUD of a UserRole
 *
 * Date Started: 6/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@RolesAllowed("authAdmin")
public class RoleController implements ModelDriven<Object> {

    private UserAccountService userAccountService;
    private UserRole userRole = new UserRole();
    private List<UserRole> list;

    public HttpHeaders index() {
        list = userAccountService.listRoles();
        return new DefaultHttpHeaders("index");
    }

    /**
     * Create a new role
     * @return
     */
    public HttpHeaders create() {
        if (userRole != null) {
            userRole= userAccountService.createRole(userRole.getName());
            if (userRole != null) {
                return new DefaultHttpHeaders("create").setLocationId(userRole);
            }
        }
        return new DefaultHttpHeaders("error");
    }

    public Object getModel() {
        return list != null ? list : userRole;
    }

    @Inject
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
