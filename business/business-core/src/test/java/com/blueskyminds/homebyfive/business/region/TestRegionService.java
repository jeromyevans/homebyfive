package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.business.region.service.RegionService;
import com.blueskyminds.homebyfive.business.region.service.RegionServiceImpl;
import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.patterns.AddressPatternMatcher;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;

/**
 * Date Started: 5/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestRegionService extends RegionTestCase {

    public void testListStatesInCountry() {
        TableModel statesTable = countryService.listStatesAsTable("au");
        assertNotNull(statesTable);

        TableModel suburbsTable = stateService.listSuburbsAsTable("au", "nsw");
        assertNotNull(suburbsTable);

        TableModel postCodesTable = stateService.listPostCodesAsTable("au", "nsw");
        assertNotNull(postCodesTable);

    }

}
