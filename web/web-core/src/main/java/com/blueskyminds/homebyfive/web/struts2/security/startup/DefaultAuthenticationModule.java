package com.blueskyminds.homebyfive.web.struts2.security.startup;

import com.google.inject.AbstractModule;
import com.blueskyminds.homebyfive.web.struts2.security.services.AuthenticationService;
import com.blueskyminds.homebyfive.web.struts2.security.services.UserAccountServiceImpl;
import com.blueskyminds.homebyfive.business.user.services.UserAccountService;
import com.blueskyminds.homebyfive.web.struts2.security.services.AuthenticationServiceImpl;
import com.blueskyminds.homebyfive.web.struts2.security.dao.UserAccountDAOImpl;
import com.blueskyminds.homebyfive.web.struts2.security.dao.UserRoleDAOImpl;
import com.blueskyminds.homebyfive.business.user.dao.UserAccountDAO;
import com.blueskyminds.homebyfive.business.user.dao.UserRoleDAO;
import com.blueskyminds.homebyfive.web.struts2.security.session.SessionRegistry;
import com.blueskyminds.homebyfive.web.struts2.security.session.SessionRegistryInitializer;
import com.blueskyminds.homebyfive.web.struts2.security.token.SessionMapTokenRegistry;
import com.blueskyminds.homebyfive.web.struts2.security.token.TokenRegistry;

/**
 * Sets up bindings to default implementations
 *
 * Date Started: 10/05/2008
 */
public class DefaultAuthenticationModule extends AbstractModule {

    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(UserAccountService.class).to(UserAccountServiceImpl.class);
        bind(UserAccountDAO.class).to(UserAccountDAOImpl.class);
        bind(UserRoleDAO.class).to(UserRoleDAOImpl.class);
        bind(TokenRegistry.class).to(SessionMapTokenRegistry.class); // note: xwork also uses one if these
        bind(SessionRegistry.class).asEagerSingleton();
        bind(SessionRegistryInitializer.class);
    }

    public static DefaultAuthenticationModule build() {
        return new DefaultAuthenticationModule();
    }
}
