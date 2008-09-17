package com.blueskyminds.struts2.securityplugin.startup;

import com.google.inject.AbstractModule;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.blueskyminds.struts2.securityplugin.services.AuthenticationService;
import com.blueskyminds.struts2.securityplugin.services.UserAccountServiceImpl;
import com.blueskyminds.struts2.securityplugin.services.UserAccountService;
import com.blueskyminds.struts2.securityplugin.services.AuthenticationServiceImpl;
import com.blueskyminds.struts2.securityplugin.dao.UserAccountDAOImpl;
import com.blueskyminds.struts2.securityplugin.dao.UserRoleDAOImpl;
import com.blueskyminds.struts2.securityplugin.session.SessionRegistry;
import com.blueskyminds.struts2.securityplugin.session.SessionRegistryInitializer;
import com.blueskyminds.struts2.securityplugin.token.SessionMapTokenRegistry;
import com.blueskyminds.struts2.securityplugin.token.TokenRegistry;

/**
 * Sets up bindings to default implementations
 *
 * Date Started: 10/05/2008
 */
public class DefaultAuthenticationModule extends AbstractModule {

    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(UserAccountService.class).to(UserAccountServiceImpl.class);
        bind(UserAccountDAOImpl.class);
        bind(UserRoleDAOImpl.class);
        bind(TokenRegistry.class).to(SessionMapTokenRegistry.class); // note: xwork also uses one if these
        bind(SessionRegistry.class).asEagerSingleton();
        bind(SessionRegistryInitializer.class);
    }

    public static DefaultAuthenticationModule build() {
        return new DefaultAuthenticationModule();
    }
}
