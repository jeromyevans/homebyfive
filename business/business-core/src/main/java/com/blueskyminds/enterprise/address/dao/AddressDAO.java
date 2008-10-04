package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.street.StreetHandle;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * Implements Address-related queries
 *
 * <p/>
 * Date Started: 12/06/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class AddressDAO extends AbstractDAO {

    private static final String QUERY_COUNTRY_BY_NAME = "country.byName";
    private static final String QUERY_COUNTRY_BY_ISO3_CODE = "country.byIso3Code";
    private static final String QUERY_COUNTRY_BY_ISO2_CODE = "country.byIso2Code";

    private static final String QUERY_ALL_STATES_BY_COUNTRY_ID = "state.listAllByCountryId";
    private static final String QUERY_STATE_BY_COUNTRY_ID_AND_NAME = "state.byCountryAndName";
    private static final String QUERY_STATE_BY_COUNTRY_ID_AND_ABBR = "state.byCountryAndAbbr";
    //private static final String QUERY_POSTCODE_BY_COUNTRY = "postCode.byCountry";

    private static final String QUERY_SUBURB_BY_STATE_ID_AND_NAME = "suburb.byStateIdAndName";
    private static final String QUERY_POSTCODE_BY_STATE_ID_AND_NAME = "postCode.byStateIdAndName";

    private static final String QUERY_ALL_SUBURBS_BY_STATE_ID = "suburb.listAllByStateId";
    private static final String QUERY_ALL_POSTCODES_BY_STATE_ID = "postCode.listAllByStateId";

    private static final String QUERY_ALL_SUBURBS_BY_POSTCODE_ID = "suburb.listAllByPostCodeId";

    private static final String QUERY_SUBURB_BY_ISO3_CODE = "suburb.byIso3Code";

    private static final String PARAM_COUNTRY_NAME = "name";
    private static final String PARAM_ISO3_CODE = "iso3CountryCode";
    private static final String PARAM_ISO2_CODE = "iso2CountryCode";

    private static final String PARAM_COUNTRY = "country";
    private static final String PARAM_COUNTRY_ID = "countryId";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_SUBURB = "suburb";
    private static final String PARAM_STATE_ID = "stateId";
    private static final String PARAM_POSTCODE = "postCode";
    private static final String PARAM_ABBR = "abbr";

    private static final String PARAM_STREET_NAME = "name";
    private static final String PARAM_STREET_TYPE = "type";
    private static final String PARAM_STREET_SECTION = "section";
    private static final String PARAM_STREET_SUBURB = "suburb";
    private static final String QUERY_STREET_NAME = "street.byNameAndSuburb";
    private static final String QUERY_STREET_NAME_TYPE = "street.byNameTypeAndSuburb";
    private static final String QUERY_STREET_NAME_TYPE_SECTION = "street.byNameTypeSectionAndSuburb";
    private static final String PARAM_ADDRESS = "address";    

    private static final String QUERY_ALL_COUNTRIES = "country.listAll";
    private static final String QUERY_ALL_SUBURBS_BY_COUNTRY = "suburb.listAllByCountry";
    private static final String QUERY_ALL_POSTCODES_BY_COUNTRY_ID = "postCode.listAllByCountryId";
    private static final String QUERY_ALL_STREETS_BY_COUNTRY = "street.listAllByCountry";
    private static final String QUERY_ALL_STREETS_BY_SUBURB = "street.listAllBySuburb";
    private static final String QUERY_ALL_STREETS_BY_POSTCODE = "street.listAllByPostCode";
    private static final String PARAM_NAME = "name";

    private static final String QUERY_ALL_ADDRESSES_BY_SUBURB = "address.listAllBySuburb";
    private static final String QUERY_ALL_ADDRESSES_BY_POSTCODE = "address.listAllByPostCode";
    private static final String QUERY_ALL_ADDRESSES_BY_STREET = "address.listAllByStreet";
    private static final String PARAM_STREET = "street";

    public AddressDAO(EntityManager em) {
        super(em, Address.class);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AddressDAO with default attributes
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
    public CountryHandle lookupCountry(String iso3DigitCode) {
        List<CountryHandle> countries;
        Query query = em.createNamedQuery(QUERY_COUNTRY_BY_ISO3_CODE);
        query.setParameter(PARAM_ISO3_CODE, StringUtils.upperCase(iso3DigitCode));
        countries = new LinkedList<CountryHandle>(query.getResultList());

        if (countries.size() > 0) {
            return (CountryHandle) countries.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Get an instance of a country matching the 2 digit country code  eg. AU
     *
     * @param iso2DigitCode (it will be converted to uppercase for the query)
     * @return Country instance, or null if not found
     */
    public CountryHandle findCountryByISO2DigitCode(String iso2DigitCode) {
        List<CountryHandle> countries;
        Query query = em.createNamedQuery(QUERY_COUNTRY_BY_ISO2_CODE);
        query.setParameter(PARAM_ISO2_CODE, StringUtils.upperCase(iso2DigitCode));
        countries = new LinkedList<CountryHandle>(query.getResultList());

        if (countries.size() > 0) {
            return countries.iterator().next();
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the states in the specified country
     *
     * @return Country instance, or null if not found
     */
    public Set<StateHandle> listStatesInCountry(CountryHandle country) {

        Set<StateHandle> regions;
        Query query = em.createNamedQuery(QUERY_ALL_STATES_BY_COUNTRY_ID);
        query.setParameter(PARAM_COUNTRY_ID, country.getId());
        regions = new HashSet<StateHandle>(query.getResultList());

        return regions;
    }

    /**
     * Get a state by its name in the specified country
     *
     * @return Country instance, or null if not found
     */
    public StateHandle lookupState(String name, CountryHandle country) {
        Set<StateHandle> regions;
        Query query = em.createNamedQuery(QUERY_STATE_BY_COUNTRY_ID_AND_NAME);
        query.setParameter(PARAM_COUNTRY_ID, country.getId());
        query.setParameter(PARAM_NAME, name);
        regions = new HashSet<StateHandle>(query.getResultList());
        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Get a state by its abbreviation in the specified country
     *
     * @return Country instance, or null if not found
     */
    public StateHandle lookupStateByAbbr(String abbr, CountryHandle country) {
        Set<StateHandle> regions;
        Query query = em.createNamedQuery(QUERY_STATE_BY_COUNTRY_ID_AND_ABBR);
        query.setParameter(PARAM_COUNTRY_ID, country.getId());
        query.setParameter(PARAM_ABBR, abbr);
        regions = new HashSet<StateHandle>(query.getResultList());
        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Get a suburb by its name in the specified state
     *
     * @return SuburbHandle instance, or null if not found
     */
    public SuburbHandle lookupSuburb(String name, StateHandle state) {
        Set<SuburbHandle> regions;
        Query query = em.createNamedQuery(QUERY_SUBURB_BY_STATE_ID_AND_NAME);
        query.setParameter(PARAM_STATE_ID, state.getId());
        query.setParameter(PARAM_NAME, StringUtils.lowerCase(name));
        regions = new HashSet<SuburbHandle>(query.getResultList());
        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

     /**
     * Get a postcode by its name in the specified state
     *
     * @return PostCodeHandle instance, or null if not found
     */
    public PostCodeHandle lookupPostCode(String name, StateHandle state) {
        Set<PostCodeHandle> regions;
        Query query = em.createNamedQuery(QUERY_POSTCODE_BY_STATE_ID_AND_NAME);
        query.setParameter(PARAM_STATE_ID, state.getId());
        query.setParameter(PARAM_NAME, StringUtils.lowerCase(name));
        regions = new HashSet<PostCodeHandle>(query.getResultList());
        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the postcodes in the specified country
     *
     * @return Country instance, or null if not found
     */
//    public Collection<PostCodeHandle> listPostCodesInCountry(CountryHandle country) {
//        //return (Set) new RegionGraphDAO(em).listPostCodesInCountry(country);
//
//        Set<PostCodeHandle> regions;
//        Query query = em.createNamedQuery(QUERY_POSTCODE_BY_COUNTRY);
//        query.setParameter(PARAM_COUNTRY_ID, country.getId());
//        regions = new HashSet<PostCodeHandle>(query.getResultList());
//
//        return regions;
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the suburbs in the given state
     *
     * @return loist of suburbs
     */
    public Set<SuburbHandle> listSuburbsInState(StateHandle state) {
        Set<SuburbHandle> regions;
        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_STATE_ID);
        query.setParameter(PARAM_STATE_ID, state.getId());
        regions = new HashSet<SuburbHandle>(query.getResultList());
        return regions;
    }

    /**
     * Get the set of all postcodes in the specified country.
     *
     * @return
     */
    public Set<PostCodeHandle> listPostCodesInState(StateHandle state) {
        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES_BY_STATE_ID);
        query.setParameter(PARAM_STATE_ID, state.getId());
        return new HashSet<PostCodeHandle>(query.getResultList());
    }

    /**
     * Get the set of all countries.
     * The aliases for the country are eagerly loaded
     *
     * @return
     */
    public Set<CountryHandle> listCountries() {
        Query query = em.createNamedQuery(QUERY_ALL_COUNTRIES);
        return new HashSet<CountryHandle>(query.getResultList());
    }

    /**
     * Get the set of all suburbs in the specified country.
     *
     * @return
     */
    public Set<SuburbHandle> listSuburbsInCountry(CountryHandle countryHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_COUNTRY);
        query.setParameter("country", countryHandle);
        return new HashSet<SuburbHandle>(query.getResultList());
    }

    /**
     * Get the set of all postcodes in the specified country.
     *
     * @return
     */
    public Set<PostCodeHandle> listPostCodesInCountry(CountryHandle countryHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES_BY_COUNTRY_ID);
        query.setParameter(PARAM_COUNTRY_ID, countryHandle.getId());
        return new HashSet<PostCodeHandle>(query.getResultList());
    }

    /**
     * Get the set of all postcodes in the specified country.
     *
     * @return
     */
    public Set<StreetHandle> listStreetsInCountry(CountryHandle countryHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_STREETS_BY_COUNTRY);
        query.setParameter(PARAM_COUNTRY, countryHandle);
        return new HashSet<StreetHandle>(FilterTools.getNonNull(query.getResultList()));
    }

     /**
     * Get the set of all streets in the specified suburb.
     *
     * @return
     */
    public Set<StreetHandle> listStreetsInSuburb(SuburbHandle suburbHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_STREETS_BY_SUBURB);
        query.setParameter(PARAM_SUBURB, suburbHandle);
        return new HashSet<StreetHandle>(FilterTools.getNonNull(query.getResultList()));
    }

     /**
     * Get the set of all streets in the specified postcode.
     *
     * @return
     */
    public Set<StreetHandle> listStreetsInPostCode(PostCodeHandle postCodeHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_STREETS_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE, postCodeHandle);
        return new HashSet<StreetHandle>(FilterTools.getNonNull(query.getResultList()));
    }

    /**
     * Get the set of all known addresses in the specified suburb
     *
     * @return
     */
    public Set<Address> listAddressesInSuburb(SuburbHandle suburbHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_ADDRESSES_BY_SUBURB);
        query.setParameter(PARAM_SUBURB, suburbHandle);
        return new HashSet<Address>(FilterTools.getNonNull(query.getResultList()));
    }

    /**
     * Get the set of all known addresses in the specified postcode
     *
     * @return
     */
    public Set<Address> listAddressesInPostCode(PostCodeHandle postCodeHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_ADDRESSES_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE, postCodeHandle);
        return new HashSet<Address>(FilterTools.getNonNull(query.getResultList()));
    }

    /**
     * Get the set of all known addresses in the specified street
     *
     * @return
     */
    public Set<Address> listAddressesInStreet(StreetHandle street) {
        Query query = em.createNamedQuery(QUERY_ALL_ADDRESSES_BY_STREET);
        query.setParameter(PARAM_STREET, street);
        return new HashSet<Address>(FilterTools.getNonNull(query.getResultList()));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an instance of a street if it exists in the specified suburb
     *
     * @return Street instance, or null if not found
     */
    public StreetHandle getStreetByName(String streetName, StreetType type, StreetSection section, SuburbHandle suburb) {
        StreetHandle street;
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

        street = (StreetHandle) findOne(query);

        return street;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Searches for an exact match for the specified address
     *
     * @return Address instance, or null if not found
     */
    public Address lookupAddressByExample(Address address) throws PersistenceServiceException {
        Address addressFound;

        QueryBuilder queryBuilder = new QueryBuilder(address.getClass(), PARAM_ADDRESS);
        Query query;

        if ((address.getSuburb() != null) && (address.getSuburb().isIdSet())) {
            queryBuilder.addCondition("address.suburb = :suburb");
            queryBuilder.setParameter("suburb", address.getSuburb());
        } else {
            queryBuilder.addCondition("address.suburb is null");
        }

        if (address instanceof StreetAddress) {
            StreetAddress streetAddress = (StreetAddress) address;

            if (streetAddress.getStreet() != null) {
                if (streetAddress.getStreet().isIdSet()) {
                    queryBuilder.addCondition("address.street = :street");
                    queryBuilder.setParameter("street", streetAddress.getStreet());
                } else {
                    // match street by name
                    StreetHandle street = streetAddress.getStreet();
                    if (StringUtils.isNotBlank(street.getName())) {
                        queryBuilder.addCondition("lower(address.street.name) = :streetName");
                        queryBuilder.setParameter("streetName", StringUtils.lowerCase(street.getName()));
                    }
                    if (street.getStreetType() != null) {
                        queryBuilder.addCondition("address.street.type = :streetType");
                        queryBuilder.setParameter("streetType", street.getStreetType());
                    } else {
                        queryBuilder.addCondition("address.street.type is null");
                    }

                    if (street.getSection() != null) {
                        queryBuilder.addCondition("address.street.section = :streetSection");
                        queryBuilder.setParameter("streetSection", street.getSection());
                    } else {
                        queryBuilder.addCondition("address.street.section is null");
                    }
                }
            } else {
                queryBuilder.addCondition("address.street is null");
            }

            if (streetAddress.getStreetNumber() != null) {
                queryBuilder.addCondition("address.streetNumber = :streetNumber");
                queryBuilder.setParameter("streetNumber", streetAddress.getStreetNumber());
            } else {
                queryBuilder.addCondition("address.streetNumber is null");
            }

            if (address instanceof UnitAddress) {
                UnitAddress unitAddress = (UnitAddress) streetAddress;
                if (unitAddress.getUnitNumber() != null) {
                    queryBuilder.addCondition("address.unitNumber = :unitNumber");
                    queryBuilder.setParameter("unitNumber", unitAddress.getUnitNumber());
                } else {
                    queryBuilder.addCondition("address.unitNumber is null");
                }
            } else {
                if (address instanceof LotAddress) {
                    LotAddress lotAddress = (LotAddress) address;
                    if (lotAddress.getLotNumber() != null) {
                        queryBuilder.addCondition("address.lotNumber = :lotNumber");
                        queryBuilder.setParameter("lotNumber", lotAddress.getLotNumber());
                    } else {
                        queryBuilder.addCondition("address.lotNumber is null");
                    }
                }
            }
        }

        query = queryBuilder.generateQuery(em);
        addressFound = (Address) findOne(query);

        return addressFound;
    }

}

