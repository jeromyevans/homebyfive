package com.blueskyminds.landmine.core.property.advertisement.dao;

import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaign;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Entity Access Object for PropertyAdvertisementCampaign's
 *
 * Date Started: 23/04/2008
 */
public class AdvertisementCampaignDAO extends AbstractDAO<PropertyAdvertisementCampaign> {

    @Inject
    public AdvertisementCampaignDAO(EntityManager em) {
        super(em, PropertyAdvertisementCampaign.class);
    }
        
}
