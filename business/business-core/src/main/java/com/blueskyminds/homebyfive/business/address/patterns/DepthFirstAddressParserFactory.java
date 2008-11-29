package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;
import com.blueskyminds.homebyfive.business.region.service.StreetService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

/**
 * Date Started: 22/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DepthFirstAddressParserFactory implements AddressParserFactory {

    private AddressService addressService;
    private AddressDAO addressDAO;
    private SubstitutionService substitutionService;
    private SuburbService suburbService;
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private StreetService streetService;

    public DepthFirstAddressParserFactory(AddressService addressService, AddressDAO addressDAO, SubstitutionService substitutionService, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService, StreetService streetService) {
        this.addressService = addressService;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;
        this.stateService = stateService;
        this.suburbService = suburbService;
        this.postalCodeService = postalCodeService;
        this.streetService = streetService;
    }

    /**
     * Create a parser for the specified country
    *
    * @param countryCode
    * @return
    */
    public AddressParser create(String countryCode) {
        Country countryHandle = addressDAO.lookupCountry(countryCode);
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, countryHandle, stateService, postalCodeService, suburbService, streetService);
        return addressParser;
    }

    /**
     * Create a parser constrained to the specified suburb.
     *
     * @param suburb
     * @return
     */
    public AddressParser create(Suburb suburb) {
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, suburb, stateService, postalCodeService, suburbService, streetService);
        return addressParser;
    }
}
