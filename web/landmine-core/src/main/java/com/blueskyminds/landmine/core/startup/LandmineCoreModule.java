package com.blueskyminds.landmine.core.startup;

import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.patterns.SuburbPatternMatcherFactory;
import com.blueskyminds.enterprise.address.patterns.AddressParserFactory;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.guice.providers.*;
import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.enterprise.region.service.RegionGraphService;
import com.blueskyminds.enterprise.tag.service.TagService;
import com.blueskyminds.framework.guice.providers.SingletonSubstitutionService;
import com.blueskyminds.framework.guice.providers.SubstitutionDAOProvider;
import com.blueskyminds.framework.guice.providers.SubstitutionServiceProvider;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.landmine.core.property.advertisement.service.AdvertisementImportService;
import com.blueskyminds.landmine.core.property.advertisement.service.AdvertisementImportServiceImpl;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementDAO;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.assets.eao.PremiseAssetEAO;
import com.blueskyminds.landmine.core.events.EventRegistry;
import com.blueskyminds.landmine.core.events.EventRegistryImpl;
import com.blueskyminds.housepad.core.region.group.RegionGroupFactory;
import com.blueskyminds.housepad.core.region.service.RegionService;
import com.blueskyminds.housepad.core.region.service.RegionServiceImpl;
import com.blueskyminds.housepad.core.region.eao.CountryEAO;
import com.blueskyminds.housepad.core.region.eao.StateEAO;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.region.eao.AddressPathDAO;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;
import com.google.inject.AbstractModule;

/**
 * A Guice module that sets up the standard LandMine services
 */
public class LandmineCoreModule extends AbstractModule {


    /** Configure the default bindings for the landmine services */
    protected void configure() {
        bind(SubstitutionDAO.class).toProvider(SubstitutionDAOProvider.class);
      //  bind(SubstitutionService.class).toProvider(SubstitutionServiceProvider.class);
        bind(AddressDAO.class).toProvider(AddressDAOProvider.class);
        bind(PartyService.class).toProvider(PartyServiceProvider.class);
        bind(TagService.class).toProvider(TagServiceProvider.class);
        //bind(SubstitutionService.class).to(SingletonSubstitutionService.class).asEagerSingleton();
        bind(SubstitutionService.class).toProvider(SubstitutionServiceProvider.class);
        //bind(AddressPatternMatcherFactory.class).to(SingletonAddressPatternMatcherFactory.class).asEagerSingleton();
        //bind(AddressPatternMatcherFactory.class).toProvider(AddressPatternMatcherFactoryProvider.class);
        bind(AddressParserFactory.class).toProvider(DepthFirstAddressParserFactoryProvider.class);
        bind(SuburbPatternMatcherFactory.class).toProvider(SuburbPatternMatcherFactoryProvider.class);
        bind(AddressService.class).toProvider(AddressServiceProvider.class);
        bind(RegionGraphDAO.class).toProvider(RegionGraphDAOProvider.class);
        bind(RegionGraphService.class).toProvider(RegionGraphServiceProvider.class);

        bind(PremiseService.class).to(PremiseServiceImpl.class);
        bind(AdvertisementImportService.class).to(AdvertisementImportServiceImpl.class);
        
        bind(PropertyService.class).to(PropertyServiceImpl.class);

        bind(RegionGroupFactory.class);
        bind(RegionService.class).to(RegionServiceImpl.class);
        
        bind(CountryEAO.class);
        bind(StateEAO.class);
        bind(SuburbEAO.class);
        bind(PropertyEAO.class);
        bind(PremiseEAO.class);
        bind(PremiseAssetEAO.class);
        bind(AdvertisementDAO.class);
        bind(AdvertisementCampaignDAO.class);

        bind(AddressPathDAO.class);
        
//        bind(PropertyTypeMatcher.class);
//        bind(AskingPriceMatcher.class);

        bind(EventRegistry.class).to(EventRegistryImpl.class).asEagerSingleton();
    }
}
