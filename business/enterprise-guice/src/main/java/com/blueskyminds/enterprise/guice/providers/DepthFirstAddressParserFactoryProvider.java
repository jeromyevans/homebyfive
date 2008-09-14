package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.enterprise.address.patterns.AddressParserFactory;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcherFactoryImpl;
import com.blueskyminds.enterprise.address.patterns.DepthFirstAddressParserFactory;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Creates a DepthFirstAddressParserFactory
 *
 * Date Started: 22/07/2008
 * <p/>
 * History:
 */
public class DepthFirstAddressParserFactoryProvider implements Provider<AddressParserFactory> {

    private static final Log LOG = LogFactory.getLog(DepthFirstAddressParserFactoryProvider.class);

    private Provider<AddressService> addressServiceProvider;
    private Provider<AddressDAO> addressDAOProvider;
    private Provider<SubstitutionService> substitutionService;

    public AddressParserFactory get() {
        return new DepthFirstAddressParserFactory(addressServiceProvider.get(), addressDAOProvider.get(), substitutionService.get());
    }

    @Inject    
    public void setAddressServiceProvider(Provider<AddressService> addressServiceProvider) {
        this.addressServiceProvider = addressServiceProvider;
    }

    @Inject
    public void setSubstitutionService(Provider<SubstitutionService> substitutionService) {
        this.substitutionService = substitutionService;
    }

    @Inject
    public void setAddressDAOProvider(Provider<AddressDAO> addressDAOProvider) {
        this.addressDAOProvider = addressDAOProvider;
    }
}