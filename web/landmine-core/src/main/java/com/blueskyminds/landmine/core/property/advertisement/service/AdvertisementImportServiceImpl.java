package com.blueskyminds.landmine.core.property.advertisement.service;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.UnitAddress;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.contact.*;
import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.enterprise.party.IndividualRelationship;
import com.blueskyminds.enterprise.party.IndividualRole;
import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceException;
import com.blueskyminds.enterprise.tag.service.TagService;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.patterns.PatternMatcherException;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.tools.text.StringTools;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.assets.PremiseAsset;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.landmine.core.property.advertisement.*;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementDAO;
import com.blueskyminds.landmine.core.property.advertisement.ejb.AdvertisementServiceException;
import com.blueskyminds.landmine.core.property.patterns.AskingPriceMatcher;
import com.blueskyminds.landmine.core.property.patterns.PropertyTypeMatcher;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceException;
import com.blueskyminds.housepad.core.region.eao.AddressPathDAO;
import com.blueskyminds.housepad.core.region.AddressPath;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.AddressPathComponents;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.service.RegionService;
import com.blueskyminds.housepad.core.region.reference.RegionRefType;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Stateless session-bean providing access to Property Advertisements
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class AdvertisementImportServiceImpl implements AdvertisementImportService {

    private static final Log LOG = LogFactory.getLog(AdvertisementImportServiceImpl.class);

    private AddressService addressService;
    private PremiseService premiseService;
    private PartyService partyService;
    private TagService tagService;

    private PropertyTypeMatcher propertyTypeMatcher;
    private AskingPriceMatcher askingPriceMatcher;

    private EntityManager em;
    private SubstitutionService substitutionService;

    private AdvertisementDAO advertisementDAO;
    private AddressPathDAO addressPathDAO;

    private RegionService regionService;

    public AdvertisementImportServiceImpl(AddressService addressService,
                             PremiseService premiseService,
                             PartyService partyService,
                             EntityManager entityManager,
                             PropertyTypeMatcher propertyTypeMatcher,
                             AskingPriceMatcher askingPriceMatcher) {

        this.addressService = addressService;
        this.partyService = partyService;
        this.premiseService = premiseService;
        this.em = entityManager;
        this.propertyTypeMatcher = propertyTypeMatcher;
        this.askingPriceMatcher = askingPriceMatcher;
        this.regionService = regionService;
    }

    public AdvertisementImportServiceImpl() {
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Transforms the bean into a PropertyAdvertisement.
     *
     * The AgentBean is not processed
     *
     * @param bean          the property advertisement
     * @return PropertyAdvertisement (non persistent)
     * @throws com.blueskyminds.landmine.core.property.advertisement.ejb.AdvertisementServiceException if the transformation cannot be performed
     */
    public PropertyAdvertisement transformBean(PropertyAdvertisementBean bean) throws AdvertisementServiceException {

        PropertyAdvertisement propertyAdvertisement = new PropertyAdvertisement(bean.getType());
        try {
            Address address = null;
            if (!StringUtils.isBlank(bean.getAddressPath())) {
                // a pre-parsed address is included with the bean.
                Premise premise = premiseService.lookupPremise(bean.getAddressPath());
                if (premise != null) {
                    address = premise.getAddress();
                    LOG.info("Using preparsed address :"+bean.getAddressPath());
                } else {
                    // create the premise using the path components
                    /*AddressPathComponents addressComponents = new AddressPathComponents(bean.getAddressPath());
                    if (addressComponents.getStreetPath() != null) {
                        // check if the street exists or needs to be created
                        Street street = addressService.lookupregionService.lookupStreet(addressComponents.getStreetPath());
                    }
                    SuburbBean suburb = regionService.lookupSuburb(addressComponents.getSuburbPath());
                    if (suburb != null) {
                        if (addressComponents.getUnitNo() != null) {
                            address = new UnitAddress(addressComponents.getUnitNo(), addressComponents.getStreetNo(), address.get)
                        }
                    }*/
                }
            }
            if (address == null) {
                if (StringUtils.isNotBlank(bean.getCountryISO3Code())) {

                    // check if there's a cached path
                    String addressString = StringUtils.trim(bean.getAddressString());
                    if ((StringUtils.isNotBlank(addressString) && addressPathDAO != null)) {
                        // check if this is a known address string
                        AddressPath addressPath = addressPathDAO.lookupAddress(addressString);
                        if (addressPath != null) {
                            Premise premise = premiseService.lookupPremise(addressPath.getPath());
                            if (premise != null) {
                                address = premise.getAddress();
                                LOG.info("Matched cached address :"+addressPath.getPath());
                            }
                        }
                    }

                    if (address == null) {
                        if (StringUtils.isNotBlank(bean.getSuburb())) {
                            try {
                                address = addressService.lookupOrCreateAddress(bean.getStreetString(), bean.getSuburb(), bean.getState(), bean.getCountryISO3Code());
                            } catch (AddressProcessingException e) {
                                LOG.error("Address in suburb '"+bean.getSuburb()+"' could not be processed (invalid): "+bean.getAddressString());
                            }
                        } else {
                            try {
                                address = addressService.lookupOrCreateAddress(bean.getAddressString(), bean.getCountryISO3Code());
                            } catch (AddressProcessingException e) {
                                LOG.error("Address could not be processed (invalid): "+bean.getAddressString());
                            }
                        }
                        if (address != null) {
                            // cache the addresspath
                            if (addressPathDAO != null) {
                                addressPathDAO.persist(new AddressPath(addressString, PathHelper.buildPath(address), RegionRefType.Address));
                            }
                        }
                    }
                }
            }

            if (address != null) {
                propertyAdvertisement.setAddress(address);

                PremiseAttributeSet attributes = new PremiseAttributeSet(bean.getDateEntered());
                attributes.setBathrooms(bean.getBathrooms());
                attributes.setBedrooms(bean.getBedrooms());
                attributes.setCarspaces(bean.getCarspaces());
                attributes.setStoreys(bean.getStoreys());
                attributes.setBuildingArea((bean.getFloorArea() != null ? new Area(bean.getFloorArea(), UnitsOfArea.SquareMetre) : null)); // todo: sqm assumption
                if (bean.getLandArea() != null) {
                    if (bean.getLandArea() < 30.0F) {
                        // this is better than assuming its in sqm, but the units may be wrong still
                        attributes.setLandArea(new Area(bean.getLandArea(), UnitsOfArea.Hectare));
                    } else {
                        attributes.setLandArea(new Area(bean.getLandArea(), UnitsOfArea.SquareMetre));
                    }
                }

                if (propertyTypeMatcher == null) {
                    propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
                }

                if (bean.getPropertyType() != null) {
                    attributes.setType(propertyTypeMatcher.extractBest(bean.getPropertyType()));
                }

                String constructionDateStr = bean.getConstructionDate();
                if (StringUtils.isNotBlank(constructionDateStr)) {
                    Date constructionDate = StringTools.extractDate(constructionDateStr);
                    if (constructionDate == null) {
                        int year = StringTools.extractInt(constructionDateStr, -1);
                        if (year > 0) {
                            constructionDate = DateTools.createDate(year, Calendar.JANUARY, 1, 0, 0, 0);
                        }
                    }
                    attributes.setConstructionDate(constructionDate);
                }

                propertyAdvertisement.setAttributes(attributes);
                propertyAdvertisement.setDateListed(bean.getDateEntered());
                propertyAdvertisement.setDateUnlisted(null);
                propertyAdvertisement.setDescription(bean.getDescription());
                if (StringUtils.isNotBlank(bean.getPrice())) {

                    if (askingPriceMatcher == null) {
                        askingPriceMatcher = new AskingPriceMatcher(substitutionService);
                    }
                    AskingPrice price = askingPriceMatcher.extractBest(bean.getPrice());
                    if (price != null) {
                        if (PeriodTypes.OnceOff.equals(price.getPeriod())) {
                            // set the period to weekly if the period is once-off and the advertisement is for rent
                            if (PropertyAdvertisementTypes.Lease.equals(propertyAdvertisement.getType())) {
                                price.setPeriod(PeriodTypes.Week);
                            }
                        }
                        propertyAdvertisement.setPrice(price);
                    }
                }
            } else {
                propertyAdvertisement = null;
            }
        } catch (PatternMatcherException e) {
            throw new AdvertisementServiceException("Error transforming PropertyAdvertismentBean", e);
        }

        return propertyAdvertisement;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a Property Advertisement associated with a Premise and Parties:
     * <p/>
     * create the PropertyAdvertisement
     * parses the Address
     * lookup the premise by Address / create a new Premise
     * lookup the associated Parties and points of Contact
     *
     * @param bean  - a PropertyAdvertisementBean
     */
    public PropertyAdvertisement importAdvertisement(PropertyAdvertisementBean bean) throws AdvertisementServiceException {
        Premise premise;

        PropertyAdvertisement propertyAdvertisement = transformBean(bean);
        if (propertyAdvertisement != null) {
            PropertyAdvertisementAgencyBean agentBean = bean.getAgent();


            // lookup or create the premise...
            try {
                premise = premiseService.lookupOrCreatePremise(propertyAdvertisement.getAddress(), propertyAdvertisement.getAttributes().getType(), getClass().getSimpleName(), 0L);
            } catch(PremiseServiceException e) {
                throw new AdvertisementServiceException("Failed to lookup or create a Premise", e);
            }

            Organisation agency = null;
            if (agentBean.hasAgency()) {
                // lookup or create the Agency
                agency = new Organisation(agentBean.getAgencyName());

                if (agentBean.hasWebsite()) {
                    agency.addWebsite(new Website(agentBean.getWebsite(), POCRole.Business));
                }
                if (agentBean.hasAgencyPhone()) {
                    agency.addPhoneNumber(new PhoneNumber(agentBean.getAgencyPhone(), POCRole.Business, PhoneNumberTypes.Fixed));
                }
                if (agentBean.hasAgencyFax()) {
                    agency.addPhoneNumber(new PhoneNumber(agentBean.getAgencyFax(), POCRole.Business, PhoneNumberTypes.Fixed));
                }
            }

            Individual agent = null;
            if (agentBean.hasContacts()) {

                for (PropertyAdvertisementContactBean contact : agentBean.getContacts()) {
                    try {
                        if (contact.hasContact()) {
                            agent = new Individual(contact.getContactName());
                            if (contact.hasEmail()) {
                                agent.addEmailAddress(new EmailAddress(contact.getEmail(), POCRole.Business));
                            }
                            if (contact.hasMobile()) {
                                agent.addPhoneNumber(new PhoneNumber(contact.getMobile(), POCRole.Business, PhoneNumberTypes.Mobile));
                            }
                            if (contact.hasPhone()) {
                               agent.addPhoneNumber(new PhoneNumber(contact.getPhone(), POCRole.Business, PhoneNumberTypes.Fixed));
                            }
                            if (contact.hasFax()) {
                                agent.addPhoneNumber(new PhoneNumber(contact.getFax(), POCRole.Business, PhoneNumberTypes.Fixed));
                            }

                            agent = partyService.createOrMergeIndividual(agent);
                            propertyAdvertisement.addAgent(agent);
                        }
                    } catch (PartyServiceException e) {
                        throw new AdvertisementServiceException("Failed to create or merge an Agent", e);
                    }
                }
            }

            if (agency != null) {
                if (agent != null) {
                    agency.addIndividualRelationship(new IndividualRelationship(agency, agent, "Agent", new IndividualRole("Agent")));
                }
                try {
                    agency = partyService.createOrMergeOrganisation(agency);
                    propertyAdvertisement.setAgency(agency);
                } catch (PartyServiceException e) {
                    throw new AdvertisementServiceException("Failed to create or merge an Agency", e);
                }
            }


            // associate the advertisement with this premise.
            PremiseEvent premiseEvent = premise.associateAdvertisement(propertyAdvertisement);

            // lets convert some of the features in the advertisement into useful information
            for (PropertyAdvertisementFeatureBean feature : bean.getFeatures()) {
                FeatureClassification classification = feature.getClassification();
                if (classification != null) {
                    if (FeatureClassification.Asset.equals(classification)) {
                        try {
                            PremiseAsset asset = premiseService.lookupOrCreateAsset(StringUtils.isNotBlank(feature.getSubstitution()) ? feature.getSubstitution() : feature.getDescription());
                            if (asset != null) {
                                premise.addAsset(asset, null, 1);
                            }
                        } catch (PremiseServiceException e) {
                            throw new AdvertisementServiceException("Failed to lookup or create an Asset", e);
                        }
                    } else {
                        if (FeatureClassification.Feature.equals(classification)) {
                            Tag tag = tagService.lookupOrCreateTag(StringUtils.isNotBlank(feature.getSubstitution()) ? feature.getSubstitution() : feature.getDescription());
                            if (tag != null) {
                                premise.addTag(tag);
                            }
                        }
                    }
                }
            }

            // save the premise
            premiseService.persist(premise);
            // ensure the back-reference to the advertisement was persisted
            em.persist(propertyAdvertisement.getAttributes());

            if (premiseEvent != null) {
                // notify listeners of the new premise event
            }
        }

        return propertyAdvertisement;
    }

    /**
     * Import a property advertisement bean
     *
     * @param bean  - a PropertyAdvertisementBean
     * @return the id of the property advertisement
     */
    public Long importAdvertisementBean(PropertyAdvertisementBean bean) throws AdvertisementServiceException {
        PropertyAdvertisement propertyAdvertisement = importAdvertisement(bean);
        if (propertyAdvertisement != null) {
            return propertyAdvertisement.getId();
        } else {
            return null;
        }
    }

    public Long updateAdvertisementBean(Long advertisementId, PropertyAdvertisementBean newBean) throws AdvertisementServiceException {
        PropertyAdvertisement existingAdvertisement = advertisementDAO.findById(advertisementId);
        if (existingAdvertisement != null) {
            PropertyAdvertisement newAdvertisement = transformBean(newBean);
            existingAdvertisement.updateFrom(newAdvertisement);
            existingAdvertisement = advertisementDAO.persist(existingAdvertisement);
            return existingAdvertisement.getId();
        } else {
            LOG.error("UpdateFailed: Could not find advertisement with id "+advertisementId);
            return null;
        }

   }

    /**
     * Associates a Property Advertisement with a Premise and persists both
     *
     * @param premise
     * @param advertisement
     */
    public Premise associateAdvertisement(Premise premise, PropertyAdvertisement advertisement) throws AdvertisementServiceException {

        premise.associateAdvertisement(advertisement);
        em.persist(premise);

        return premise;
    }
   
    @Inject
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Inject
    public void setPremiseService(PremiseService premiseService) {
        this.premiseService = premiseService;
    }

    @Inject
    public void setPartyService(PartyService partyService) {
        this.partyService = partyService;
    }

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

//    @Inject
//    public void setPropertyTypeMatcher(PropertyTypeMatcher propertyTypeMatcher) {
//        this.propertyTypeMatcher = propertyTypeMatcher;
//    }

//    @Inject
//    public void setAskingPriceMatcher(AskingPriceMatcher askingPriceMatcher) {
//        this.askingPriceMatcher = askingPriceMatcher;
//    }

    @Inject
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Inject
    public void setSubstitutionService(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }

    @Inject
    public void setAdvertisementDAO(AdvertisementDAO advertisementDAO) {
        this.advertisementDAO = advertisementDAO;
    }

    @Inject
    public void setAddressPathDAO(AddressPathDAO addressPathDAO) {
        this.addressPathDAO = addressPathDAO;
    }
}
