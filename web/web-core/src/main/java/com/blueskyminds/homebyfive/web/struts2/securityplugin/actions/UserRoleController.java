package com.blueskyminds.homebyfive.web.struts2.securityplugin.actions;

import org.apache.struts2.config.Namespace;
import org.apache.struts2.rest.HttpHeaders;

/**
 * Date Started: 9/05/2008
 */
@Namespace("/services/users/{userAccountId}")
public class UserRoleController {

    private String id;
    private Long userAccountId;
    private Long userRoleId;
    
    /** Assign a new role to the specified user */
    public HttpHeaders create() {
        return null;
    }

    /**
     * Remove a role for the specified user
     * @return
     */
    public HttpHeaders destory() {
        return null;
    }
}
