package com.blueskyminds.enterprise.address.parser;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.patterns.*;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.enterprise.address.patterns.*;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.PlainTextAddress;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

/**
 * A faster implementation of an address pattern matcher
 *
 * Date Started: 13/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestFastAddressParser extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestFastAddressParser.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private CountryHandle aus;
    private AddressService addressService;
    private AddressDAO addressDAO;
    private SubstitutionDAO substitutionDAO;
    private SubstitutionServiceImpl substitutionService;

    private DepthFirstAddressParser addressParser;

    public TestFastAddressParser() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        em.flush();
        addressService = new AddressServiceImpl(em);
        addressDAO = new AddressDAO(em);
        substitutionDAO = new SubstitutionDAO(em);
        substitutionService = new SubstitutionServiceImpl(substitutionDAO);
        aus = addressDAO.lookupCountry("AUS");
        addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, aus);
    }

   
    public void testAddress1() throws Exception {
        String inputString = "1/22 Spruson St, Neutral Bay NSW 2089";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }

     public void testAddressCleansing2() throws Exception {
         String inputString = "5 Braeside St Blackheath NSW";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing3() throws Exception {
         String inputString = "Apt5305/560 Lygon Street Carlton VIC";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing4() throws Exception {
         String inputString = "2 Ballaraat Street Talbot VIC";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing5() throws Exception {
         String inputString = "48 Westgarth Street Fitzroy VIC";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }

    public void testAddressCleansing6() throws Exception {
         String inputString = "6/6-8 Lansdowne Road St Kilda East VIC";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing7() throws Exception {
         String inputString = "27 B Clarke Street Alice Springs NT";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing8() throws Exception {
         String inputString = "Unit 1, 20 Leichhardt Terrace Alice Springs NT";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing9() throws Exception {
         String inputString = "Lot 2811 Wooliana Road Daly River NT";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }

    public void testAddressCleansing9_1() throws Exception {
         String inputString = "lot 10 - 5 lindel street kippa-ring qld";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }

    public void testAddressCleansing10() throws Exception {
         String inputString = "304/ The Point Port Melbourne VIC";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing11() throws Exception {
         String inputString = "Oaklands Estate, Hall Parade, Commercial Hazelbrook NSW";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }
    public void testAddressCleansing12() throws Exception {
         String inputString = "Shop 3 - 195 Great Western Highway Hazelbrook Hazelbrook Business Only!!! NSW";

        Address address = addressParser.parseAddress(inputString);
        LOG.info(address);
    }

}
