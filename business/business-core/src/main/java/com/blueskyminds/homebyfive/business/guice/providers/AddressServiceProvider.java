package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.patterns.AddressParserFactory;
import com.blueskyminds.homebyfive.business.region.service.*;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * A Guice Provider for the AddressService
 *
 * Date Started: 22/10/2007
 * <p/>
 * History:
 */
public class AddressServiceProvider implements Provider<AddressService> {

    private Provider<EntityManager> em;
    private AddressParserFactory addressParserFactory;
    private RegionGraphService regionGraphService;
    private CountryService countryService;
    private SuburbService suburbService;
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private StreetService streetService;

    public AddressService get() {
        AddressServiceImpl addressService = new AddressServiceImpl(addressParserFactory, em.get(), countryService, stateService, postalCodeService, suburbService, streetService);
        return addressService;
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }

    @Inject
    public void setAddressPatternMatcherFactory(AddressParserFactory addressParserFactory) {
        this.addressParserFactory = addressParserFactory;
    }

    @Inject
    public void setRegionGraphService(RegionGraphService regionGraphService) {
        this.regionGraphService = regionGraphService;
    }

    @Inject
    public void setSuburbService(SuburbService suburbService) {
        this.suburbService = suburbService;
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Inject
    public void setPostalCodeService(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Inject
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }
}
