package com.blueskyminds.landmine.core.property.service;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.address.UnitAddress;
import com.blueskyminds.enterprise.address.StreetAddress;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.landmine.core.property.assets.PremiseAsset;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetMap;
import com.blueskyminds.landmine.core.property.assets.eao.PremiseAssetEAO;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaign;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaignSummary;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementSummaryFactory;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.events.EventRegistry;
import com.blueskyminds.landmine.core.events.LandmineEvents;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import com.blueskyminds.framework.patterns.LevensteinDistanceTools;
import com.blueskyminds.framework.persistence.paging.Page;
import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.List;

/**
 * Default implementation of the PremiseService
 *
 * Date Started: 29/10/2007
 * <p/>
 * History:
 */
public class PremiseServiceImpl implements PremiseService {

    private AddressService addressService;
    private PropertyService propertyService;
    private PremiseEAO premiseEAO;
    private AdvertisementCampaignDAO advertisementCampaignDAO;
    private EventRegistry eventRegistry;
    private PremiseAssetEAO premiseAssetEAO;

    public PremiseServiceImpl(AddressService addressService, PropertyService propertyService, PremiseEAO premiseEAO, AdvertisementCampaignDAO advertisementCampaignDAO) {        
        this.addressService = addressService;
        this.propertyService = propertyService;
        this.premiseEAO = premiseEAO;
        this.advertisementCampaignDAO = advertisementCampaignDAO;
    }

    public PremiseServiceImpl() {
    }

    /**
     * Creates a new premise for the specified address.
     *
     * The address will be persisted it it hasn't been yet.
     *
     * If the premise is a unit an entry is also created for the parent complex
     *
     * @param address
     * @param type
     * @param sourceType
     *@param sourceId @return
     */
    public Premise createPremise(Address address, PropertyTypes type, String sourceType, Long sourceId) throws PremiseServiceException {
        return createPremise(address, type, false, sourceType, sourceId);
    }

    /**
     * Creates a new premise for the specified address.
     *
     * The address will be persisted it it hasn't been yet.
     *
     * If the premise is a unit an entry is also created for the parent complex
     *
     * @param address
     * @param type
     * @param sourceType
     *@param sourceId @return
     */
    public Premise createPremise(Address address, PropertyTypes type, boolean setType, String sourceType, Long sourceId) throws PremiseServiceException {
        Premise premise = new Premise();
        premise.associateAddress(address, new Date());
        if (setType) {
            // init the initial type of the property - used when creating a parent property
            premise.setType(type, sourceType, sourceId);
        }

        if (UnitAddress.class.isAssignableFrom(address.getClass())) {
            // check if there's a premise for the parent complex
            StreetAddress streetAddress = ((UnitAddress) address).extractStreetAddress();

            if (type != null) {
                PropertyTypes parentType = type.getParentType();
                Premise complex = lookupOrCreatePremise(streetAddress, parentType, true, sourceType, sourceId);
                if (complex != null) {
                    if (!complex.hasChild(premise)) {
                        complex.addChild(premise);
                        premiseEAO.persist(complex) ; // don't fire workflow again
                    }
                }
            }                 
        }
        
        persist(premise);

        return premise;
    }

    /**
     * Lookup a Premise from an Address
     *
     * If the Address isn't known then the Address is persisted too
     * If a Premise isn't known at that (new)address a new Premise is created
     *
     * The new Premise will not be created with any PremiseAttributes
     *
     * A Premise may also be created for the parent Complex if applicable
     *
     * @param type     if supplied, property type will be used to infer the type of the parent premise if appropriate.
     *  
     *
     */
    public Premise lookupOrCreatePremise(Address address, PropertyTypes type, String sourceType, Long sourceId) throws PremiseServiceException {
        return lookupOrCreatePremise(address, type, false, sourceType, sourceId);
    }


    /**
     * Lookup a Premise/Create a new one with no attributes from a PropertyFormBean
     *
     * The PropertyFormBean is augmented with the RegionBean to create an Address.  The Address is created/looked up
     * via the AddressService and a Premise created/looked up at that Address
     */
    public Premise lookupOrCreatePremise(PropertyFormBean propertyFormBean, RegionBean regionBean, String sourceType, Long sourceId) throws PremiseServiceException {
        Premise premise = null;
        Address address = propertyFormBean.toAddress(regionBean);

        try {
            address = addressService.lookupOrCreateAddress(address);

            if (address != null) {
                premise = lookupOrCreatePremise(address, (propertyFormBean.isUnitAddress() ? PropertyTypes.Unit : null), sourceType,  sourceId);
            } else {
                throw new PremiseServiceException("Failed to look or create the address for a Premise");
            }
        } catch (AddressProcessingException e) {
            throw new PremiseServiceException("Failed to look or create the address for a Premise", e);
        }
        return premise;
    }

    /**
     * Lookup a Premise from an Address
     *
     * If the Address isn't known then the Address is persisted too
     * If a Premise isn't known at that (new)address a new Premise is created
     *
     * If setType is true, a new attribute set is created setting the type to the specified value
     *
     */
    private Premise lookupOrCreatePremise(Address address, PropertyTypes type, boolean setType, String sourceType, Long sourceId) throws PremiseServiceException {
        Premise premise;
        Address persistentAddress;

        if (!address.isPersistent()) {
            try {
                // assume the address is by value
                persistentAddress = addressService.lookupOrCreateAddress(address);
            } catch (AddressProcessingException e) {
                throw new PremiseServiceException("Failed to look or create the address for a Premise", e);
            }
        } else {
            persistentAddress = address;
        }

        // check if there's a premise already
        premise = premiseEAO.getPremiseByAddress(persistentAddress);
        if (premise == null) {
            // no premise associated with this address - create one
            premise = createPremise(address, type, setType, sourceType, sourceId);
        }

        return premise;
    }

    /**
     * List all premises in the specified suburb
     *
     * @param suburb
     * @return
     */
    public Set<Premise> listPremises(SuburbHandle suburb) {
        return premiseEAO.listPremisesInSuburb(suburb);
    }

    /**
     * List all premises in the specified postcode
     *
     * @param postCode
     * @return
     */
    public Set<Premise> listPremises(PostCodeHandle postCode) {
        return premiseEAO.listPremisesInPostCode(postCode);
    }

    /**
     * List all premises in the specified street
     *
     * @param street
     * @return
     */
    public Set<Premise> listPremises(Street street) {
        return premiseEAO.listPremisesInStreet(street);
    }

    /**
     * List all premises in the specified complex
     *
     * @param complex
     * @return
     */
    public Set<Premise> listPremises(Premise complex) {
        return premiseEAO.listPremisesInComplex(complex);
    }

    /**
     * Associate a new PremiseAttributeSet with the specified premise IFF it changes the current
     * attributes of the premise
     *
     * @param premise
     * @param premiseAttributeSet
     * @return
     */
    public boolean associateWithNewAttributes(Premise premise, PremiseAttributeSet premiseAttributeSet) {
        if (premise != null) {
            if (premise.attributesChangedBy(premiseAttributeSet)) {
                premise.associateWithAttributes(premiseAttributeSet);
                persist(premise);
                return true;
            }
        }
        return false;
    }

    /**
     * Apply changes to a Premise
     *
     * Notifies the PropertyService of changes
     *
     * @param premise
     * @return
     */
    @Transactional
    public Premise persist(Premise premise) {
        premiseEAO.persist(premise);
        propertyService.createOrUpdateProperties(premise);
        if (eventRegistry != null) {
            eventRegistry.fire(LandmineEvents.PREMISE_UPDATED, premise);
        }
        return premise;
    }


    /**
     * Lookup a premise from the path of the PropertyBean
     *
     * @param path
     * @return
     */
    public Premise lookupPremise(String path) {
        return premiseEAO.lookupPremise(path);
    }

    /**
     * Lookup the events for the specified premise
     *
     * @param path      path of the corresponding property bean
     * @return events returned in order from most recent to oldest
     */
    public List<PremiseEvent> listPremiseEvents(String path) {
        return premiseEAO.listEvents(path);
    }

    /**
     * Lookup the PropertyAdvertisementCampaign identified by Id
     *
     * @param campaignId
     * @return
     */
    public PropertyAdvertisementCampaignSummary lookupAdvertisementCampaign(Long campaignId) {
        PropertyAdvertisementCampaign campaign = advertisementCampaignDAO.findById(campaignId);
        if (campaign != null) {
            return PropertyAdvertisementSummaryFactory.createCampaign(campaign);
        } else {
            return null;
        }
    }

    /**
     * Looks uyp the asset by its name.
     * Comparison is performed using a fuzzy matching algorithm
     *
     * @param name
     * @return
     */
    public PremiseAsset lookupOrCreateAsset(String name) {
        Set<PremiseAsset> assets = listAssets();
        List<PremiseAsset> matches = LevensteinDistanceTools.matchName(name, assets);
        if (matches.size() > 0) {
            return matches.iterator().next();
        } else {
            // create a new asset
            PremiseAsset asset = new PremiseAsset(name, null);
            premiseAssetEAO.persist(asset);
            return asset;
        }
    }

    /** Lookup an asset by its unique key */
    public PremiseAsset lookupAsset(String key) {
        return premiseAssetEAO.lookupAsset(key);
    }
    
    public Set<PremiseAsset> listAssets() {
        return premiseAssetEAO.listAssets();
    }

    public Set<PremiseAssetMap> listAssetsForPremise(Premise premise) {
        return premiseAssetEAO.listAssetsForPremise(premise);
    }

    /** Update a page of PropertyBean's from their corresponding premises */
    @Transactional
    public void updatePropertyBeans(int pageNo, int pageSize) {
        // lookup a page of premises
        Page<Premise> page = premiseEAO.findPage(pageNo, pageSize);

        for (Premise premise : page.getPageResults()) {
            propertyService.createOrUpdateProperties(premise);
        }
    }

    @Inject
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Inject
    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Inject
    public void setPremiseEAO(PremiseEAO premiseEAO) {
        this.premiseEAO = premiseEAO;
    }

    @Inject
    public void setAdvertisementCampaignEAO(AdvertisementCampaignDAO advertisementCampaignDAO) {
        this.advertisementCampaignDAO = advertisementCampaignDAO;
    }

    @Inject(optional=true)
    public void setEventRegistry(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Inject
    public void setPremiseAssetEAO(PremiseAssetEAO premiseAssetEAO) {
        this.premiseAssetEAO = premiseAssetEAO;
    }
}
