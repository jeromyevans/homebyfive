package com.blueskyminds.housepad.core.region.service;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.group.RegionGroupFactory;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.eao.CountryEAO;
import com.blueskyminds.housepad.core.region.eao.StateEAO;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.region.eao.PostCodeEAO;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 7/03/2008
 * <p/>
 * History:
 */
public class RegionServiceTest extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(RegionServiceTest.class);

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private RegionService regionService;

    public RegionServiceTest() {
        super(PERSISTENCE_UNIT);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RegionGroupFactory regionGroupFactory = new RegionGroupFactory();
        CountryEAO countryEAO = new CountryEAO(em);
        StateEAO stateEAO = new StateEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        PostCodeEAO postCodeEAO= new PostCodeEAO(em);
        AddressService addressService = new AddressServiceImpl(em);
        regionService = new RegionServiceImpl(em, countryEAO, stateEAO, suburbEAO, postCodeEAO, addressService);
    }

    public void testCreateCountry() {
        CountryBean au = new CountryBean("Australia", "AU");
        try {
            regionService.createCountry(au);
        } catch (RegionException e) {
            fail(e.getMessage());
        }

        try {
            regionService.createCountry(au);
            fail("Did not throw duplicate region exception");
        } catch (RegionException e) {
            // okay
        }

    }
}
