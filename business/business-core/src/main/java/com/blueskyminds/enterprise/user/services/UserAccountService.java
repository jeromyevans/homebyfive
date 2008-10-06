package com.blueskyminds.enterprise.user.services;

import com.blueskyminds.enterprise.user.model.UserAccount;
import com.blueskyminds.enterprise.user.model.UserRole;

import java.util.List;

/**
 * Date Started: 9/05/2008
 */
public interface UserAccountService {

    UserAccount create(String username, String unhashedPassword) throws UserAccountServiceException;

    /**
     * Check if a username already exists
     *
     * @param username
     * @return
     */
    boolean usernameExists(String username);

    /**
     * Lookup a UserAccount by id
     * @param id
     * @return
     * @throws UserAccountServiceException
     */
    UserAccount lookup(Long id) throws UserAccountServiceException;

    /**
     * Lookup a UserAccount by username
     * @param username
     * @return
     * @throws UserAccountServiceException
     */
    UserAccount lookup(String username) throws UserAccountServiceException;

    /**
     * Update the roles of a user account to match the roles list.  Roles will be added/removed accordingly
     * @param userAccount
     * @param roles
     * @throws UserAccountServiceException
     */
    void modifyRoles(UserAccount userAccount, String[] roles) throws UserAccountServiceException;

     /**
     * Create a new role
     *
     * @param name
     */
    UserRole createRole(String name);

    List<UserRole> listRoles();
     
}
