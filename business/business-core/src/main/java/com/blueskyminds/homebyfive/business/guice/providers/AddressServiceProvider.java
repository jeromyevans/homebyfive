package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.patterns.AddressParserFactory;
import com.blueskyminds.homebyfive.business.address.patterns.SuburbPatternMatcherFactory;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphService;
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
    private SuburbPatternMatcherFactory suburbPatternMatcherFactory;
    private RegionGraphService regionGraphService;

    public AddressService get() {
        AddressServiceImpl addressService = new AddressServiceImpl(addressParserFactory, em.get(), regionGraphService);
        addressService.setSuburbPatternMatcherFactory(suburbPatternMatcherFactory);
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
    public void setSuburbPatternMatcherFactory(SuburbPatternMatcherFactory suburbPatternMatcherFactory) {
        this.suburbPatternMatcherFactory = suburbPatternMatcherFactory;
    }
}
