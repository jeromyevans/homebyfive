package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.CountryHandle;
import com.blueskyminds.enterprise.region.graph.PostCodeHandle;
import com.blueskyminds.enterprise.region.graph.StateHandle;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.enterprise.region.graph.SuburbHandle;

import java.util.*;

import javax.persistence.EntityManager;

/**
 * Implements Address-related queries
 *
 * An adapter to the RegionDAO
 *
 * <p/>
 * Date Started: 12/06/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AddressDAOAdapter extends AbstractDAO {

    private static final String QUERY_COUNTRY_BY_NAME = "country.byName";
    private static final String QUERY_COUNTRY_BY_ISO3_CODE = "country.byIso3Code";
    private static final String QUERY_COUNTRY_BY_ISO2_CODE = "country.byIso2Code";

    private static final String QUERY_STATE_BY_COUNTRY = "state.byCountry";
    private static final String QUERY_POSTCODE_BY_COUNTRY = "postCode.byCountry";
    private static final String QUERY_SUBURB_BY_STATE = "suburb.byState";
    private static final String QUERY_SUBURB_BY_POSTCODE = "suburb.byPostCode";

    private static final String QUERY_SUBURB_BY_ISO3_CODE = "suburb.byIso3Code";

    private static final String PARAM_COUNTRY_NAME = "name";
    private static final String PARAM_ISO3_CODE = "iso3CountryCode";
    private static final String PARAM_ISO2_CODE = "iso2CountryCode";

    private static final String PARAM_COUNTRY = "country";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_POSTCODE = "postCode";

    private static final String PARAM_STREET_NAME = "name";
    private static final String PARAM_STREET_TYPE = "type";
    private static final String PARAM_STREET_SECTION = "section";
    private static final String PARAM_STREET_SUBURB = "suburb";
    private static final String QUERY_STREET_NAME = "street.byNameAndSuburb";
    private static final String QUERY_STREET_NAME_TYPE = "street.byNameTypeAndSuburb";
    private static final String QUERY_STREET_NAME_TYPE_SECTION = "street.byNameTypeSectionAndSuburb";
    private static final String PARAM_ADDRESS = "address";


    public AddressDAOAdapter(EntityManager em) {
        super(em, Address.class);
    }

    @Deprecated
    public AddressDAOAdapter(PersistenceService persistenceService) {
        super(null, null);
        throw new RuntimeException("PersistencService in the AddressDAOAdapter is deprecated");
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AddressDAOAdapter with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an instance of a country matching the 3 Digit country code
     *
     * @param iso3DigitCode (it will be converted to uppercase for the query)
     * @return Country instance, or null if not found
     */
    public CountryHandle getCountry(String iso3DigitCode) {
        return (CountryHandle) new RegionGraphDAO(em).getCountry(iso3DigitCode);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the states in the specified country
     *
     * @return Country instance, or null if not found
     */
    public Collection<StateHandle> getStates(CountryHandle country) {
        return (Set) new RegionGraphDAO(em).findChildrenOfType(country, RegionTypes.State);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the postcodes in the specified country
     *
     * @return Country instance, or null if not found
     */
    public Collection<PostCodeHandle> getPostCodes(CountryHandle country) {

        return (Set) new AddressDAO(em).listPostCodesInCountry(country);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an list of all the suburbs in the given state
     *
     * @return loist of suburbs
     */
    public Collection<SuburbHandle> getSuburbs(StateHandle state) {

        return (Set) new RegionGraphDAO(em).findChildrenOfType(state, RegionTypes.Suburb);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an list of all the suburbs in the given postcode
     *
     * @return loist of suburbs
     */
  /*  public Collection<Suburb> getSuburbs(PostCode postCode) {

        Query query = em.createNamedQuery(QUERY_SUBURB_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE, postCode);

        Collection<Suburb> suburbs = query.getResultList();

        return suburbs;
    }    */

     /**
     * Get an list of all the suburbs in the given country
     *
     * @return list of suburbs
     */
  /*  public Collection<Suburb> getSuburbs(String iso3CountryCode) {

        Query query = em.createNamedQuery(QUERY_SUBURB_BY_ISO3_CODE);
        query.setParameter(PARAM_ISO3_CODE, iso3CountryCode);

        Collection<Suburb> suburbs = query.getResultList();

        return suburbs;
    }
        */
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an instance of a street if it exists in the specified suburb
     *
     * @return Street instance, or null if not found
     */
/*    public Street getStreetByName(String streetName, StreetType type, StreetSection section, Suburb suburb) {
        Street street;
        Query query;

        if (type != null) {
            if (section != null) {
                query = em.createNamedQuery(QUERY_STREET_NAME_TYPE_SECTION);
                query.setParameter(PARAM_STREET_TYPE, type);
                query.setParameter(PARAM_STREET_SECTION, section);
            } else {
                query = em.createNamedQuery(QUERY_STREET_NAME_TYPE);
                query.setParameter(PARAM_STREET_TYPE, type);
            }
        } else {
            query = em.createNamedQuery(QUERY_STREET_NAME);
        }
        query.setParameter(PARAM_STREET_NAME, streetName);
        query.setParameter(PARAM_STREET_SUBURB, suburb);

        street = (Street) findOne(query);

        return street;
    }
                 */
    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Searches for an exact match for the specified address
     *
     * @return Address instance, or null if not found
     */
 /*   public Address lookupAddressByExample(Address address) throws PersistenceServiceException {
        Address addressFound;

        QueryBuilder queryBuilder = new QueryBuilder(address.getClass(), PARAM_ADDRESS);
        Query query;

        if ((address.getSuburb() != null) && (address.getSuburb().isIdSet())) {
            queryBuilder.addCondition("address.suburb = :suburb");
            queryBuilder.setParameter("suburb", address.getSuburb());
        }

        if (address instanceof StreetAddress) {
            StreetAddress streetAddress = (StreetAddress) address;

            if (streetAddress.getStreet() != null) {
                if (streetAddress.getStreet().isPersistent()) {
                    queryBuilder.addCondition("address.street = :street");
                    queryBuilder.setParameter("street", streetAddress.getStreet());
                }
            }
            if (streetAddress.getStreetNumber() != null) {
                queryBuilder.addCondition("address.streetNumber = :streetNumber");
                queryBuilder.setParameter("streetNumber", streetAddress.getStreetNumber());
            }

            if (address instanceof UnitAddress) {
                UnitAddress unitAddress = (UnitAddress) streetAddress;
                if (unitAddress.getUnitNumber() != null) {
                    queryBuilder.addCondition("address.unitNumber = :unitNumber");
                    queryBuilder.setParameter("unitNumber", unitAddress.getUnitNumber());
                }
            } else {
                if (address instanceof LotAddress) {
                    LotAddress lotAddress = (LotAddress) address;
                    if (lotAddress.getLotNumber() != null) {
                        queryBuilder.addCondition("address.lotNumber = :lotNumber");
                        queryBuilder.setParameter("lotNumber", lotAddress.getLotNumber());
                    }
                }
            }
        }

        query = queryBuilder.generateQuery(em);
        addressFound = (Address) findOne(query);

        return addressFound;
    }
          */
    // ------------------------------------------------------------------------------------------------------

    /**
     * Recursive method that visits all of the children for a regoin and adds them to the Set of
     * regions
     *
     * @param regionSet
     * @param children
     */
   /* private void traverseChildren(Set<RegionOLD> regionSet, Collection<RegionOLD> children) {
        if (children != null) {
            for (RegionOLD region : children) {
                traverseChildren(regionSet, region.getChildRegions());
                regionSet.add(region);
            }
        }
    }
         */
    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns ALL of the regions defined in the specified country.
     * <p/>
     * Regions include states, suburbs, postcodes etc
     *
     * @param country
     * @return list of all of the regions in the country.
     */
 /*   public Set<RegionOLD> getRegions(Country country) {
        Set<RegionOLD> regions = new HashSet<RegionOLD>();

        RegionOLD root;

        // reload the root of the region hierarchy
        root = (RegionOLD) findById(RegionOLD.class, country.getId());

        regions.add(root);
        traverseChildren(regions, root.getChildRegions());

        return regions;
    }     */

    // ------------------------------------------------------------------------------------------------------

}

