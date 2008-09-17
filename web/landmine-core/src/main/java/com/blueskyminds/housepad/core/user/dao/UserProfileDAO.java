package com.blueskyminds.housepad.core.user.dao;

import com.google.inject.Inject;
import com.blueskyminds.housepad.core.user.model.users.UserProfile;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Date Started: 14/05/2008
 */
public class UserProfileDAO extends AbstractDAO<UserProfile> {

    private static final String QUERY_USER_PROFILE_BY_USERID = "housepad.userProfile.byUserId";
    private static final String QUERY_USER_PROFILE_BY_VERIFICATION = "housepad.userProfile.byVerification";
    private static final String PARAM_USER_ID = "userId";
    private static final String PARAM_VERIFICATION = "verification";


    @Inject
    public UserProfileDAO(EntityManager em) {
        super(em, UserProfile.class);
    }

    public UserProfile persist(UserProfile userProfile) {
        em.persist(userProfile);
        return userProfile;
    }

    /**
     * Lookup a UserProfile by the UserId
     * @param userId
     * @return
     */
    public UserProfile lookupUserProfile(Long userId) {
        Query query = em.createNamedQuery(QUERY_USER_PROFILE_BY_USERID);
        query.setParameter(PARAM_USER_ID, userId);
        return firstIn(query.getResultList());
    }


    /**
     * Lookup a UserProfile by the Verification
     *
     * @param verification
     * @return
     */
    public UserProfile lookupUserProfile(String verification) {
        Query query = em.createNamedQuery(QUERY_USER_PROFILE_BY_VERIFICATION);
        query.setParameter(PARAM_VERIFICATION, verification);
        return firstIn(query.getResultList());
    }
   
}
