package com.blueskyminds.homebyfive.business.address.parser;

import com.blueskyminds.homebyfive.business.address.patterns.*;
import com.blueskyminds.homebyfive.business.address.Address;
import com.blueskyminds.homebyfive.business.address.PlainTextAddress;
import com.blueskyminds.homebyfive.business.address.AddressTestCase;
import com.blueskyminds.homebyfive.business.address.UnitAddress;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.service.SuburbServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

/**
 * A faster implementation of an address pattern matcher that uses depth first matching instead of breadthfirst
 * <p/>
 * Date Started: 13/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestDepthFirstAddressParser extends AddressTestCase {

    private static final Log LOG = LogFactory.getLog(TestDepthFirstAddressParser.class);

    private Country aus;

    private DepthFirstAddressParser addressParser;


    protected void setUp() throws Exception {
        super.setUp();

        SuburbPatternMatcherFactory suburbPatternMatcherFactory = new SuburbPatternMatcherFactoryImpl(em, addressDAO, countryService, stateService, postalCodeService, suburbService);
        ((SuburbServiceImpl) suburbService).setSuburbPatternMatcherFactory(suburbPatternMatcherFactory);
        aus = countryService.lookupCountry(Countries.AU);
        addressParser = new DepthFirstAddressParser(addressService, addressDAO, substitutionService, aus, stateService, postalCodeService, suburbService, streetService);
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

    public void testAddressCleansing14() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("196 Seventh Road Armadale WA");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing15() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("9 COLGAN CLOSE CALLALA BAY NSW");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        assertEquals("Callala Bay", streetAddress.getSuburb().getName());
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing16() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("6 Moore Ave HAZELWOOD PARK + SA");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        assertEquals("Hazelwood Park", streetAddress.getSuburb().getName());
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing17() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("58 Post Office Road GLENORIE NSW");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        assertEquals("Glenorie", streetAddress.getSuburb().getName());
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing18() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("1308/81 Macleay St POTTS POINT NSW");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        assertEquals("Potts Point", streetAddress.getSuburb().getName());
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }


    public void testAddressCleansing19() throws Exception {
        PlainTextAddress inputAddress = new PlainTextAddress("15/10A Challis Avenue POTTS POINT NSW");
        String addressString = inputAddress.getAddress().trim().toLowerCase();

        Address streetAddress = addressParser.parseAddress(addressString);
        assertNotNull(streetAddress);
        assertEquals("Potts Point", streetAddress.getSuburb().getName());
        assertEquals("15", ((UnitAddress) streetAddress).getUnitNumber());
        assertEquals("10a", ((UnitAddress) streetAddress).getStreetNumber());
        System.out.println(StringUtils.leftPad(addressString, 38) + "|" + (streetAddress != null ? streetAddress.toString() : "FAIL"));
    }


}
