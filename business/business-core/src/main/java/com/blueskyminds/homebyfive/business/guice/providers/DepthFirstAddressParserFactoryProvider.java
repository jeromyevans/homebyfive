package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.business.address.patterns.AddressParserFactory;
import com.blueskyminds.homebyfive.business.address.patterns.DepthFirstAddressParserFactory;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;
import com.blueskyminds.homebyfive.business.region.service.StreetService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.google.inject.Provider;
import com.google.inject.Inject;

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
    private Provider<StateService> stateService;
    private Provider<SuburbService> suburbService;

    private Provider<PostalCodeService> postalCodeService;
    private Provider<StreetService> streetService;

    public AddressParserFactory get() {
        return new DepthFirstAddressParserFactory(addressServiceProvider.get(), addressDAOProvider.get(), substitutionService.get(), stateService.get(), postalCodeService.get(), suburbService.get(), streetService.get());
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

    @Inject
    public void setSuburbServiceProvider(Provider<SuburbService> suburbService) {
        this.suburbService = suburbService;
    }

    @Inject
    public void setPostalCodeServiceProvider(Provider<PostalCodeService> postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Inject
    public void setStreetServiceProvider(Provider<StreetService> streetService) {
        this.streetService = streetService;
    }

    @Inject
    public void setStateService(Provider<StateService> stateService) {
        this.stateService = stateService;
    }
}