package com.blueskyminds.enterprise.user.dao;

import com.blueskyminds.enterprise.user.model.users.UserGroup;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Date Started: 17/05/2008
 */
public class UserGroupDAO extends AbstractDAO<UserGroup> {

    private static final String QUERY_USER_GROUP_BY_KEY = "housepad.userGroup.byKey";
    private static final String PARAM_KEY = "keyValue";

    @Inject
    public UserGroupDAO(EntityManager entityManager) {
        super(entityManager, UserGroup.class);
    }

    /**
     * Lookup a UserGroup by its key
     * @param key
     * @return
     */
    public UserGroup lookupUserGroup(String key) {
         Query query = em.createNamedQuery(QUERY_USER_GROUP_BY_KEY);
         query.setParameter(PARAM_KEY, key);
         return firstIn(query.getResultList());
    }

    public UserGroup persist(UserGroup userGroup) {
        em.persist(userGroup);
        return userGroup;
    }
}
