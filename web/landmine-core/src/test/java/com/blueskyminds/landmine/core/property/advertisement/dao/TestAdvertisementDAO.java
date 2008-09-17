package com.blueskyminds.landmine.core.property.advertisement.dao;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit tests for the PropertyAdvertisementDAO
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class TestAdvertisementDAO extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestAdvertisementDAO.class);
    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private AdvertisementDAO advertisementDAO;
    private AddressService addressService;

    public TestAdvertisementDAO() {
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
        PremiseTestTools.initialiseSampleAusPremisesWithAdvertisements(PropertyAdvertisementTypes.PrivateTreaty, 2000, 2006, em);

        addressService = new AddressServiceImpl(em);
        advertisementDAO = new AdvertisementDAO(em);
    }

    public void testFind() throws Exception {
        assertTrue(advertisementDAO.findAll().size() > 0);
    }
}
