package com.blueskyminds.struts2.securityplugin.services;

import com.blueskyminds.homebyfive.business.user.model.UserAccount;
import com.blueskyminds.homebyfive.business.user.model.UserRole;
import com.blueskyminds.homebyfive.business.user.dao.UserRoleDAO;
import com.blueskyminds.homebyfive.business.user.services.UserAccountService;
import com.blueskyminds.homebyfive.business.user.services.UserAccountServiceException;
import com.blueskyminds.struts2.securityplugin.dao.UserAccountDAOImpl;
import com.blueskyminds.homebyfive.framework.core.tools.CryptoTools;
import com.blueskyminds.homebyfive.framework.core.tools.CryptoException;
import com.google.inject.Inject;

import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 9/05/2008
 */
public class UserAccountServiceImpl implements UserAccountService {

    private UserAccountDAOImpl userAccountDAO;
    private UserRoleDAO userRoleDAO;

    @Inject
    public UserAccountServiceImpl(UserAccountDAOImpl userAccountDAO, UserRoleDAO userRoleDAO) {
        this.userAccountDAO = userAccountDAO;
        this.userRoleDAO = userRoleDAO;
    }

    public UserAccountServiceImpl() {
    }

    public UserAccount create(String username, String unhashedPassword) throws UserAccountServiceException {
        UserAccount userAccount;
        try {
            userAccount = new UserAccount(username, CryptoTools.hashSHA(unhashedPassword));
            userAccount = userAccountDAO.persist(userAccount);
        } catch (CryptoException e) {
            throw new UserAccountServiceException(e);
        }
        return userAccount;
    }

    /**
     * Check if a username already exists
     *
     * @param username
     * @return
     */
    public boolean usernameExists(String username) {
        return userAccountDAO.usernameExists(username);
    }


    /**
     * Lookup a UserAccount by id
     *
     * @param id
     * @return
     * @throws UserAccountServiceException
     */
    public UserAccount lookup(Long id) throws UserAccountServiceException {
        return userAccountDAO.findById(id);
    }

    public UserAccount lookup(String username) throws UserAccountServiceException {
        return userAccountDAO.lookupUserAccount(username);
    }
    
    /**
     * Update the roles of a user account to match the roles list.  Roles will be added/removed accordingly
     * @param userAccount
     * @param roles
     * @throws UserAccountServiceException
     */
    public void modifyRoles(UserAccount userAccount, String[] roles) throws UserAccountServiceException {
        List<UserRole> userRoles = new LinkedList<UserRole>();
        for (String role : roles) {
            UserRole userRole = userRoleDAO.lookupByName(role);
            if (userRole != null) {
                userRoles.add(userRole);
            }
        }
        userAccount.setRoles(userRoles);
        userAccountDAO.persist(userAccount);
    }

    /**
     * Create a new role
     *
     * @param name
     */
    public UserRole createRole(String name) {       
        return userRoleDAO.create(name);
    }

    public List<UserRole> listRoles() {
        return userRoleDAO.listRoles();
    }

}
