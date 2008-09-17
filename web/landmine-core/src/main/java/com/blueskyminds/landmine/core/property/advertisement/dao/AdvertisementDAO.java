package com.blueskyminds.landmine.core.property.advertisement.dao;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements queries specific to property advertisements
 *
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AdvertisementDAO extends AbstractDAO<PropertyAdvertisement> {

    private static final String QUERY_ALL_ADVERTISEMENTS_BY_REGION= "advertisement.listAllByRegion";
    private static final String PARAM_REGION = "region";

    @Inject
    public AdvertisementDAO(EntityManager em) {
        super(em, PropertyAdvertisement.class);
    }

    /**
     * Get the set of all all advertisements in the specified region
     *
     * @return
     */
    public Set<PropertyAdvertisement> listAdvertisementsInRegion(RegionHandle region) {
        Query query = em.createNamedQuery(QUERY_ALL_ADVERTISEMENTS_BY_REGION);
        query.setParameter(PARAM_REGION, region);
        return new HashSet<PropertyAdvertisement>(FilterTools.getNonNull(query.getResultList()));
    }

    public PropertyAdvertisement persist(PropertyAdvertisement propertyAdvertisement) {
        em.persist(propertyAdvertisement);
        return propertyAdvertisement;
    }
}
