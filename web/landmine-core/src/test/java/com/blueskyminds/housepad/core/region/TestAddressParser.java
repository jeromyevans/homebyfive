package com.blueskyminds.housepad.core.region;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.service.RegionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 22/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestAddressParser extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressParser.class);

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private RegionService regionService;

    public TestAddressParser() {
        super(PERSISTENCE_UNIT);
    }


    private void testBulkParser() throws Exception {
        //Addresses addressesunlgle
    }
}
