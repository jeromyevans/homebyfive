package com.blueskyminds.landmine.core.property.service;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.landmine.core.property.assets.PremiseAsset;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetMap;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaignSummary;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;
import java.util.List;

/**
 * A mock of the Property Service for testing
 *
 * Date Started: 1/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class MockPremiseService implements PremiseService {

    private static final Log LOG = LogFactory.getLog(MockPremiseService.class);

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup a Premise by the Address
    */
    public Premise lookupOrCreatePremise(Address address, PropertyTypes type, String sourceType, Long sourceId) throws PremiseServiceException {
        return null;
    }

    public Premise lookupOrCreatePremise(PropertyFormBean propertyFormBean, RegionBean regionBean, String sourceType, Long sourceId) throws PremiseServiceException {
        return null;  
    }

    /**
     * List all premises in the specified suburb
     *
     * @param suburb
     * @return
     */
    public Set<Premise> listPremises(SuburbHandle suburb) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * List all premises in the specified postcode
     *
     * @param postCode
     * @return
     */
    public Set<Premise> listPremises(PostCodeHandle postCode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * List all premises in the specified street
     *
     * @param street
     * @return
     */
    public Set<Premise> listPremises(Street street) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean associateWithNewAttributes(Premise premise, PremiseAttributeSet premiseAttributeSet) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Premise persist(Premise premise) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Premise lookupPremise(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<PremiseEvent> listPremiseEvents(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PropertyAdvertisementCampaignSummary lookupAdvertisementCampaign(Long campaignId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PremiseAsset lookupOrCreateAsset(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PremiseAsset lookupAsset(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<PremiseAsset> listAssets() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<PremiseAssetMap> listAssetsForPremise(Premise premise) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Update a page of PropertyBean's from their corresponding premises
     */
    public void updatePropertyBeans(int pageNo, int pageSize) throws PremiseServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
