package com.blueskyminds.housepad.core.region;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.housepad.core.model.PremiseSummaryFactory;
import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.region.composite.RegionCompositeFactory;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 1/11/2007
 * <p/>
 * History:
 */
public class TestServiceFacade extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestServiceFacade.class);

    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremiseAnalysisPersistenceUnit";

    private ServiceFacade serviceFacade;

    public TestServiceFacade() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        serviceFacade = new ServiceFacadeImpl();
        AddressServiceImpl addressService = new AddressServiceImpl(em);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        ((ServiceFacadeImpl) serviceFacade).setAddressService(addressService);
        ((ServiceFacadeImpl) serviceFacade).setPremiseService(new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em)));
        ((ServiceFacadeImpl) serviceFacade).setPremiseSummaryFactory(new PremiseSummaryFactory());
        ((ServiceFacadeImpl) serviceFacade).setRegionCompositeFactory(new RegionCompositeFactory());
    }

    public void testCreateTableModel() throws Exception {
        TableModel tableModel = serviceFacade.listPremisesInSuburb(5957);

        LOG.info(tableModel);
    }

}
