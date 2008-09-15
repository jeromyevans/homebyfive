package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.guice.providers.SingletonSubstitutionService;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcher;
import com.blueskyminds.enterprise.address.patterns.AddressScoringStrategy;
import com.blueskyminds.enterprise.address.patterns.AddressParserFactory;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.google.inject.Singleton;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates the heavy-weight AddressPatternMatchers, caches them and ensures they're executed with the
 * correct entity manager
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
@Singleton
public class SingletonAddressParserFactory implements AddressParserFactory {

    private static final Log LOG = LogFactory.getLog(SingletonAddressParserFactory.class);

    private Map<String, AddressPatternMatcher> matchers;
    private AddressScoringStrategy addressScoringStrategy;
    private Provider<EntityManager> em;
    private Provider<AddressDAO> addressDAOProvider;
    private SingletonSubstitutionService substitutionService;

    private Map<Long, AddressPatternMatcher> suburbMatchers;

    public SingletonAddressParserFactory() {
        this.addressScoringStrategy = new AddressScoringStrategy();
        matchers = Collections.synchronizedMap(new HashMap<String, AddressPatternMatcher>());
        suburbMatchers = Collections.synchronizedMap(new HashMap<Long, AddressPatternMatcher>());
    }

    /**
     * Create/get an AddressPatternMatcher for the specified ISO 3 digit code
     *
     * @param iso3Code
     * @return
     * @throws PatternMatcherInitialisationException
     */
    public AddressPatternMatcher create(String iso3Code) {
        AddressPatternMatcher matcher = matchers.get(iso3Code);
        if (matcher == null) {
            try {
                LOG.info("Creating a new AddressPatternMatcher for "+iso3Code);
                matcher = new AddressPatternMatcher(iso3Code, addressScoringStrategy);

                matcher.setEntityManager(em.get());
                matcher.setAddressDAO(addressDAOProvider.get());
                matcher.setSubstitutionService(substitutionService);
                matcher.setupBins();
                matchers.put(iso3Code, matcher);
            } catch (PatternMatcherInitialisationException e) {
                LOG.error(e);
            }

        } else {
            /// inject the current entity manager and reset the bins
            matcher.setEntityManager(em.get());
            matcher.reset();
        }
        return matcher;
    }

    public AddressPatternMatcher create(SuburbHandle suburb) {
        AddressPatternMatcher matcher = suburbMatchers.get(suburb.getId());
        if (matcher == null) {
            try {
                LOG.info("Creating a new AddressPatternMatcher for "+suburb);
                matcher = new AddressPatternMatcher(suburb, addressScoringStrategy);

                matcher.setEntityManager(em.get());
                matcher.setAddressDAO(addressDAOProvider.get());
                matcher.setSubstitutionService(substitutionService);
                matcher.setupBins();
                suburbMatchers.put(suburb.getId(), matcher);
            } catch (PatternMatcherInitialisationException e) {
                LOG.error(e);
            }

        } else {
            /// inject the current entity manager and reset the bins
            matcher.setEntityManager(em.get());
            matcher.reset();
        }
        return matcher;
    }

    @Inject
    public void setEntityManagerProvider(Provider<EntityManager> em) {
        this.em = em;
    }

    @Inject
    public void setSubstitutionService(SingletonSubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }

    @Inject
    public void setAddressDAOProvider(Provider<AddressDAO> addressDAOProvider) {
        this.addressDAOProvider = addressDAOProvider;
    }
}
