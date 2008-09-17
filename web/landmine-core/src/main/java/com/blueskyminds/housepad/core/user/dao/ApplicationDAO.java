package com.blueskyminds.housepad.core.user.dao;

import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Date Started: 17/05/2008
 */
public class ApplicationDAO extends AbstractDAO<Application> {

    private static final String QUERY_APPLICATION_BY_TOKEN = "housepad.application.byToken";
    private static final String PARAM_TOKEN = "token";

    @Inject
    public ApplicationDAO(EntityManager em) {
        super(em, Application.class);
    }

    public Application lookupByToken(String token) {
         Query query = em.createNamedQuery(QUERY_APPLICATION_BY_TOKEN);
         query.setParameter(PARAM_TOKEN, token);
         return firstIn(query.getResultList());
    }

    public Application persist(Application application) {
        em.persist(application);
        return application;
    }
}
