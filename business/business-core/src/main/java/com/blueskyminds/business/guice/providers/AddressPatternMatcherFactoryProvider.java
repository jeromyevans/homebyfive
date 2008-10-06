package com.blueskyminds.business.guice.providers;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.business.address.patterns.AddressParserFactory;
import com.blueskyminds.business.address.patterns.AddressPatternMatcherFactoryImpl;
import com.blueskyminds.business.address.dao.AddressDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Date Started: 31/05/2008
 * <p/>
 * History:
 */
public class AddressPatternMatcherFactoryProvider implements Provider<AddressParserFactory> {

    private static final Log LOG = LogFactory.getLog(AddressPatternMatcherFactoryProvider.class);

    private Provider<EntityManager> em;
    private Provider<AddressDAO> addressDAOProvider;
    private Provider<SubstitutionService> substitutionService;

    public AddressParserFactory get() {
        return new AddressPatternMatcherFactoryImpl(em.get(), addressDAOProvider.get(), substitutionService.get());
    }

    @Inject
    public void setEntityManagerProvider(Provider<EntityManager> em) {
        this.em = em;
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