package com.blueskyminds.struts2.securityplugin.services;

import com.blueskyminds.struts2.securityplugin.dao.UserAccountDAO;
import com.blueskyminds.struts2.securityplugin.model.UserAccount;
import com.blueskyminds.struts2.securityplugin.token.TokenRegistry;
import com.blueskyminds.homebyfive.framework.core.tools.CryptoTools;
import com.blueskyminds.homebyfive.framework.core.tools.CryptoException;
import com.blueskyminds.enterprise.user.model.token.TokenProvider;
import com.google.inject.Inject;

/**
 * Date Started: 9/05/2008
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserAccountDAO userAccountDAO;
    private TokenRegistry tokenRegistry;

     /**
     * Authenticate a user by confirming that they exist with the specified password and the account is valid.
     *
     * @param username
     * @param unhashedPassword
     * @return their UserId or null if denied
     * @throws com.blueskyminds.struts2.securityplugin.services.AuthorizationException the authentication could not be performed
     */
    public String authenticate(String username, String unhashedPassword) throws AuthorizationException {
        try {
            UserAccount userAccount = userAccountDAO.lookupUserAccount(username, CryptoTools.hashSHA(unhashedPassword));
            if (userAccount != null) {
                if (userAccount.isValid()) {

                    // generate their authentication token and place it in the registry if available
                    String token = TokenProvider.nextToken();
                    if (tokenRegistry != null) {
                        tokenRegistry.put(token, userAccount.getId());
                    }

                    return token;
                }
            }
        } catch (CryptoException e) {
            throw new AuthorizationException(e);
        }
        return null;
    }
   
    /**
     * Asserts that the token is in the current session
     *
     * @param token
     * @return
     */
    @Deprecated
    public boolean isAuthenticated(String token) {
//        Map sessionMap = ActionContext.getContext().getSession();
//        return sessionMap.get(token) != null;
        return false;
    }
    
    @Deprecated
    public void invalidate(String token) {
     //   SessionRegistry.getInstance().invalidate();
    }

    @Inject
    public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Inject
    public void setTokenRegistry(TokenRegistry tokenRegistry) {
        this.tokenRegistry = tokenRegistry;
    }
}