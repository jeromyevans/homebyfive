package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Date Started: 30/04/2008
 * <p/>
 * History:
 */
public class RegionGraphDAOProvider implements Provider<RegionGraphDAO> {

    private Provider<EntityManager> em;

    public RegionGraphDAO get() {
        return new RegionGraphDAO(em.get());
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }
}