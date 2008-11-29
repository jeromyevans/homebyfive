package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.address.AddressTestCase;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test the suburb name patten matching
 *
 * Date Started: 31/05/2008
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestSuburbPatternMatcher extends AddressTestCase {

    private static final Log LOG = LogFactory.getLog(TestSuburbPatternMatcher.class);

    private SuburbPatternMatcher matcher;

    protected void setUp() throws Exception {
        super.setUp();
        matcher = new SuburbPatternMatcher(Countries.AU, em, countryService, stateService, postalCodeService, suburbService);
    }

     public void testSuburbCleansing1() throws Exception {
         String inputText = "Neutral Bay NSW 2089";         
         Suburb suburb = matcher.extractBest(inputText);
         System.out.println(StringUtils.leftPad(inputText, 38)+"|"+(suburb != null ? suburb.toString() : "FAIL"));
         assertNotNull(suburb);
    }





}