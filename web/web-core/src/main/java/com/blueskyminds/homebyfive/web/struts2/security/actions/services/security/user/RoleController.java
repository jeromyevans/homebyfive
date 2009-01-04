package com.blueskyminds.homebyfive.web.struts2.security.actions.services.security.user;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.blueskyminds.homebyfive.business.user.services.UserAccountService;
import com.blueskyminds.homebyfive.business.user.model.UserAccount;
import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import javax.annotation.security.RolesAllowed;

/**
 * Assign/remove UserRole's form a UserAccount
 *
 * Date Started: 7/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Namespace("/services/security/{username}")
@Results({
        @Result(name = "index", type = "freemarker", location = "/results/services/security/user/role-index.ftl"),
        @Result(name = "editNew", type = "freemarker", location = "/results/services/security/user/role-editNew.ftl"),
        @Result(name = "create", type = "freemarker", location = "/results/services/security/user/role-create.ftl")
        })
@RolesAllowed("authAdmin")
public class RoleController implements ModelDriven<String[]>, Preparable {

    private String username;
    private UserAccountService userAccountService;
    private String[] roles;
    private UserAccount userAccount;

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method is called to allow the action to prepare itself.
     *
     * @throws Exception thrown if a system level exception occurs.
     */
    public void prepare() throws Exception {
        userAccount = userAccountService.lookup(username);
    }

    public HttpHeaders index() throws Exception {
        if (userAccount != null) {
            roles = userAccount.getRoles();
            return new DefaultHttpHeaders("index");
        } else {
            return new DefaultHttpHeaders("error");
        }
    }

    public HttpHeaders editNew() throws Exception {
        if (userAccount != null) {
            roles = userAccount.getRoles();
            return new DefaultHttpHeaders("editNew");
        } else {
            return new DefaultHttpHeaders("error");
        }
    }

    /** Assign roles to the user account */
    @Transactional
    public HttpHeaders create() throws Exception {
        if (userAccount != null) {
            userAccountService.modifyRoles(userAccount, roles);
            return new DefaultHttpHeaders("create");
        } else {
            return new DefaultHttpHeaders("error");
        }
    }

    public String[] getModel() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Inject
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
