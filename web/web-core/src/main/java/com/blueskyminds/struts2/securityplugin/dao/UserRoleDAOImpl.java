package com.blueskyminds.struts2.securityplugin.dao;

import com.blueskyminds.enterprise.user.model.UserRole;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.enterprise.user.dao.UserRoleDAO;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Date Started: 6/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class UserRoleDAOImpl extends AbstractDAO<UserRole> implements UserRoleDAO {

    private static final String QUERY_USERROLE_BY_NAME = "auth.userRole.byName";
    private static final String QUERY_USERROLES = "auth.userRoles";
    private static final String PARAM_NAME = "name";

    @Inject
    public UserRoleDAOImpl(EntityManager entityManager) {
        super(entityManager, UserRole.class);
    }

    public List<UserRole> listRoles() {
        Query query = em.createNamedQuery(QUERY_USERROLES);
        return query.getResultList();
    }

    public UserRole create(String name) {
        UserRole role = new UserRole(name);
        em.persist(role);
        return role;
    }

    public UserRole lookupByName(String name) {
        Query query = em.createNamedQuery(QUERY_USERROLE_BY_NAME);
        query.setParameter(PARAM_NAME, name);
        return firstIn(query.getResultList());
    }

    public boolean roleExists(String name) {
        return lookupByName(name) != null;
    }
}
