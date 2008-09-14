package com.blueskyminds.struts2.securityplugin.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.blueskyminds.struts2.securityplugin.model.UserAccount;
import com.blueskyminds.struts2.securityplugin.services.UserAccountService;
import com.blueskyminds.struts2.securityplugin.services.AuthorizationException;
import com.blueskyminds.struts2.securityplugin.services.UserAccountServiceException;
import com.google.inject.Inject;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.DefaultHttpHeaders;

import javax.servlet.http.HttpServletResponse;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;

/**
 * Date Started: 9/05/2008
 */
@DenyAll
public class UserAccountController implements ModelDriven<Object>, Preparable {

    private UserAccountService userAccountService;
    private UserAccount userAccount;
    private String username;
    private String password;

    public void prepare() throws Exception {
    }

    public void prepareCreate() {
        userAccount = new UserAccount();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Create a new user account
     * @return
     */
    @RolesAllowed("authAdmin")
    public HttpHeaders create() throws UserAccountServiceException {
        userAccount = userAccountService.create(username, password);
        if (userAccount != null) {
            return new DefaultHttpHeaders("success").setLocationId(userAccount.getId());
        } else {
            return new DefaultHttpHeaders("unauthorized");
        }
    }

    public Object getModel() {
        return userAccount;
    }

    @Inject
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
