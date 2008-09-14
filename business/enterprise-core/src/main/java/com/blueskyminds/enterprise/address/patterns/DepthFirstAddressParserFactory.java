package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;

/**
 * Date Started: 22/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DepthFirstAddressParserFactory implements AddressParserFactory {

    private AddressService addressService;
    private AddressDAO addressDAO;
    private SubstitutionService substitutionService;

    public DepthFirstAddressParserFactory(AddressService addressService, AddressDAO addressDAO, SubstitutionService substitutionService) {
        this.addressService = addressService;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;
    }

    /**
     * Create a parser for the specified country
    *
    * @param iso3DigitCode
    * @return
    */
    public AddressParser create(String iso3DigitCode) {
        CountryHandle countryHandle = addressDAO.lookupCountry(iso3DigitCode);
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, countryHandle);
        return addressParser;
    }

    /**
     * Create a parser constrained to the specified suburb.
     *
     * @param suburb
     * @return
     */
    public AddressParser create(SuburbHandle suburb) {
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, suburb);
        return addressParser;
    }
}
