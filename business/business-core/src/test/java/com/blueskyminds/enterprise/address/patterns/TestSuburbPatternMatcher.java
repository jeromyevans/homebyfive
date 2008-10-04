package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.graph.Suburb;
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
public class TestSuburbPatternMatcher extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestSuburbPatternMatcher.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private SuburbPatternMatcher matcher;

    public TestSuburbPatternMatcher() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        em.flush();
        matcher = new SuburbPatternMatcher("AUS", em);
    }

     public void testSuburbCleansing1() throws Exception {
         String inputText = "Neutral Bay NSW 2089";         
         Suburb suburb = matcher.extractBest(inputText);
         System.out.println(StringUtils.leftPad(inputText, 38)+"|"+(suburb != null ? suburb.toString() : "FAIL"));
         assertNotNull(suburb);
    }





}