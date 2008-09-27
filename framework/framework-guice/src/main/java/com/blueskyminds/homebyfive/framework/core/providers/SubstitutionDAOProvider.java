package com.blueskyminds.homebyfive.framework.core.providers;

import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.dao.SubstitutionDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Provides a SubstitutionDAO with created with the current EntityManager
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public class SubstitutionDAOProvider implements Provider<SubstitutionDAO> {

    private Provider<EntityManager> em;

    public SubstitutionDAO get() {
        return new SubstitutionDAO(em.get());
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }
}
