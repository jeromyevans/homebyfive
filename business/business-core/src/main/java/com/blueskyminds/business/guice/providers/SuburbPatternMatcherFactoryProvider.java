package com.blueskyminds.business.guice.providers;

import com.blueskyminds.business.address.patterns.SuburbPatternMatcherFactory;
import com.blueskyminds.business.address.patterns.SuburbPatternMatcherFactoryImpl;
import com.blueskyminds.business.address.dao.AddressDAO;
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

    public SuburbPatternMatcherFactory get() {
        SuburbPatternMatcherFactory factory = new SuburbPatternMatcherFactoryImpl(em.get(), addressDAOProvider.get());
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
}
