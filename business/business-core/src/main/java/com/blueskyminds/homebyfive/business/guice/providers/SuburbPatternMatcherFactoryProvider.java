package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.address.patterns.SuburbPatternMatcherFactory;
import com.blueskyminds.homebyfive.business.address.patterns.SuburbPatternMatcherFactoryImpl;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.region.service.CountryService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Date Started: 31/05/2008
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SuburbPatternMatcherFactoryProvider implements Provider<SuburbPatternMatcherFactory> {

    private Provider<EntityManager> em;
    private Provider<AddressDAO> addressDAOProvider;
    private Provider<CountryService> countryService;
    private Provider<StateService> stateService;
    private Provider<PostalCodeService> postalCodeService;
    private Provider<SuburbService> suburbService;

    public SuburbPatternMatcherFactory get() {
        SuburbPatternMatcherFactory factory = new SuburbPatternMatcherFactoryImpl(em.get(), addressDAOProvider.get(), countryService.get(), stateService.get(), postalCodeService.get(), suburbService.get());
        return factory;
    }

    @Inject
    public void setEntityManagerProvider(Provider<EntityManager> em) {
        this.em = em;
    }

    @Inject
    public void setAddressDAOProvider(Provider<AddressDAO> addressDAOProvider) {
        this.addressDAOProvider = addressDAOProvider;
    }

    @Inject
    public void setCountryService(Provider<CountryService> countryService) {
        this.countryService = countryService;
    }

    @Inject
    public void setStateService(Provider<StateService> stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setPostalCodeService(Provider<PostalCodeService> postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Inject
    public void setSuburbService(Provider<SuburbService> suburbService) {
        this.suburbService = suburbService;
    }
}
