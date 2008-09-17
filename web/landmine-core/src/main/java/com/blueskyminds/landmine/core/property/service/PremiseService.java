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
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaign;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaignSummary;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.housepad.core.region.model.RegionBean;

import java.util.Set;
import java.util.List;

/**
 * Services to access information about a real property
 *
 * Date Started: 27/12/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public interface PremiseService {

    /**
     * Lookup a Premise by the Address.  Create it if it doesn't exist
     */
    Premise lookupOrCreatePremise(Address address, PropertyTypes type, String sourceType, Long sourceId) throws PremiseServiceException;

    /**
     * Lookup a Premise/Create a new one with no attributes from a PropertyFormBean
     *
     * @param propertyFormBean  attributes of the property
     * @param regionBean        the region the property is located in (eg. known suburb so it doesnt have to be parsed)
     */
    Premise lookupOrCreatePremise(PropertyFormBean propertyFormBean, RegionBean regionBean, String sourceType, Long sourceId) throws PremiseServiceException;

    /**
     * List all premises in the specified suburb
     *
     * @param suburb
     * @return
     */
    Set<Premise> listPremises(SuburbHandle suburb);

    /**
     * List all premises in the specified postcode
     *
     * @param postCode
     * @return
     */
    Set<Premise> listPremises(PostCodeHandle postCode);

    /**
     * List all premises in the specified street
     *
     * @param street
     * @return
     */                         
    Set<Premise> listPremises(Street street);

    /**
     * Associate a new PremiseAttributeSet with the specified premise IFF it changes the current
     * attributes of the premise
     *
     * @param premise
     * @param premiseAttributeSet
     * @return
     */
    boolean associateWithNewAttributes(Premise premise, PremiseAttributeSet premiseAttributeSet);

    /**
     * Apply changes to a Premise
     *
     * @param premise
     * @return
     */
    Premise persist(Premise premise);

    /**
     * Lookup a premise from the path of the PropertyBean
     *
     * @param path
     * @return
     */
    Premise lookupPremise(String path);

    /**
     * Lookup the events for the specified premise
     *
     * @param path      path of the corresponding property bean
     * @return events returned in order from most recent to oldest
     */
    List<PremiseEvent> listPremiseEvents(String path);


    /**
     * Lookup the PropertyAdvertisementCampaign identified by Id
     *
     * @param campaignId
     * @return
     */
    PropertyAdvertisementCampaignSummary lookupAdvertisementCampaign(Long campaignId);

    /**
     * Lookup an asset by its name or create a new one
     *
     * @param name
     * @return
     */
    PremiseAsset lookupOrCreateAsset(String name) throws PremiseServiceException;

    /** Lookup an asset by its unique key */
    PremiseAsset lookupAsset(String key);

    Set<PremiseAsset> listAssets();

    Set<PremiseAssetMap> listAssetsForPremise(Premise premise);

    /** Update a page of PropertyBean's from their corresponding premises */
    void updatePropertyBeans(int pageNo, int pageSize) throws PremiseServiceException;
}
