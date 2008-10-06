package com.blueskyminds.homebyfive.web.struts2.securityplugin.dao;

import com.blueskyminds.homebyfive.business.user.model.UserAccount;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.user.dao.UserAccountDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Date Started: 9/05/2008
 */
public class UserAccountDAOImpl extends AbstractDAO<UserAccount> implements UserAccountDAO {

    private static final String QUERY_USERACCOUNT_BY_USERPASS = "auth.userAccount.byUsernamePassword";
    private static final String QUERY_USERACCOUNT_BY_USERNAME = "auth.userAccount.byUsername";
    private static final String QUERY_USERNAMES = "auth.usernames.byUsername";

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_HASHED_PASSWORD = "hashedPassword";

    @Inject
    public UserAccountDAOImpl(EntityManager em) {
        super(em, UserAccount.class);
    }

    /**
     * Look up the UserAccount with matching username and hashed password
     *
     * @param username
     * @param hashedPassword
     * @return
     */
    public UserAccount lookupUserAccount(String username, String hashedPassword) {
        Query query = em.createNamedQuery(QUERY_USERACCOUNT_BY_USERPASS);
        query.setParameter(PARAM_USERNAME, username);
        query.setParameter(PARAM_HASHED_PASSWORD, hashedPassword);
        return firstIn(query.getResultList());
    }

    /**
     * Check if a username already exists
     *
     * The check is case-insenstive
     *
     * @param username
     * @return
     */
    public boolean usernameExists(String username) {
        if ((username != null) && (username.length() > 0)) {
            Query query = em.createNamedQuery(QUERY_USERNAMES);
            query.setParameter(PARAM_USERNAME, username.toLowerCase());
            List result = query.getResultList();
            return (result != null && result.size() > 0);
        } else {
            return true;
        }
    }

    /**
     * Lookup a useraccount by username
     *
     * @param username
     * @return
     */
    public UserAccount lookupUserAccount(String username) {
        Query query = em.createNamedQuery(QUERY_USERACCOUNT_BY_USERNAME);
        query.setParameter(PARAM_USERNAME, username);
        return firstIn(query.getResultList());
    }

    public UserAccount persist(UserAccount userAccount) {
        em.persist(userAccount);
        return userAccount;
    }

}
