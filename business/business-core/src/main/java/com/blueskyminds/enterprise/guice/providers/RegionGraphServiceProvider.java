package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.enterprise.region.service.RegionGraphServiceImpl;
import com.blueskyminds.enterprise.region.service.RegionGraphService;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * A Guice Provider for the RegionGraphService
 *
 * Date Started: 30/04/2008
 * <p/>
 * History:
 */
public class RegionGraphServiceProvider implements Provider<RegionGraphService> {

    private Provider<EntityManager> em;
    private RegionGraphDAO regionGraphDAO;

    public RegionGraphService get() {
        return new RegionGraphServiceImpl(em.get(), regionGraphDAO);
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }

    @Inject
    public void setRegionGraphDAO(RegionGraphDAO regionGraphDAO) {
        this.regionGraphDAO = regionGraphDAO;
    }
}