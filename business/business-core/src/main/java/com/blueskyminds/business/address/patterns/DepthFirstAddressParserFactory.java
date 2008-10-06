package com.blueskyminds.business.address.patterns;

import com.blueskyminds.business.region.graph.Suburb;
import com.blueskyminds.business.region.graph.Country;
import com.blueskyminds.business.address.service.AddressService;
import com.blueskyminds.business.address.dao.AddressDAO;
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
        Country countryHandle = addressDAO.lookupCountry(iso3DigitCode);
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, countryHandle);
        return addressParser;
    }

    /**
     * Create a parser constrained to the specified suburb.
     *
     * @param suburb
     * @return
     */
    public AddressParser create(Suburb suburb) {
        DepthFirstAddressParser addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, suburb);
        return addressParser;
    }
}
