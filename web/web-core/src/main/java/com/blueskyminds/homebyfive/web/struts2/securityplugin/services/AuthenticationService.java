package com.blueskyminds.homebyfive.web.struts2.securityplugin.services;

/**
 * Date Started: 8/05/2008
 */
public interface AuthenticationService {

    /**
     * Authenticate a user by confirming that they exist with the specified password and the account is valid.
     *
     * @param username
     * @param unhashedPassword
     * @return the token granted for this user, or null if access it not granted
     * @throws AuthorizationException if the authentication could not be performed
     */
    String authenticate(String username, String unhashedPassword) throws AuthorizationException;

    /**
     * Check if a token has been authenticated
     *
     * @param token
     * @return
     */
    boolean isAuthenticated(String token);

    /**
     * Invalidate a current token
     *
     * @param token
     */
    void invalidate(String token);

}