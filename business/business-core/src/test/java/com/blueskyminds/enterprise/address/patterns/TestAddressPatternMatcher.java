package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcher;
import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.region.graph.CountryHandle;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvOptions;
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvTextReader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test the address handling of the framework
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestAddressPatternMatcher extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressPatternMatcher.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private AddressPatternMatcher matcher;

    public TestAddressPatternMatcher() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        em.flush();
        matcher = new AddressPatternMatcher("AUS", em);
    }

     public void testAddressCleansing1() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("1/22 Spruson St, Neutral Bay NSW 2089");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing2() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("5 Braeside St Blackheath NSW");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing3() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("Apt5305/560 Lygon Street Carlton VIC");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing4() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("2 Ballaraat Street Talbot VIC");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing5() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("48 Westgarth Street Fitzroy VIC");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing6() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("6/6-8 Lansdowne Road St Kilda East VIC");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing7() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("27 B Clarke Street Alice Springs NT");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing8() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("Unit 1, 20 Leichhardt Terrace Alice Springs NT");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing9() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("Lot 2811 Wooliana Road Daly River NT");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    public void testAddressCleansing9_1() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("lot 10 - 5 lindel street kippa-ring qld");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    
    public void testAddressCleansing10() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("304/ The Point Port Melbourne VIC");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing11() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("Oaklands Estate, Hall Parade, Commercial Hazelbrook NSW");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }
    public void testAddressCleansing12() throws Exception {
         PlainTextAddress inputAddress = new PlainTextAddress("Shop 3 - 195 Great Western Highway Hazelbrook Hazelbrook Business Only!!! NSW");
         String addressString = inputAddress.getAddress().trim().toLowerCase();

         Address streetAddress = matcher.extractBest(addressString);
         System.out.println(StringUtils.leftPad(addressString, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Loads a CSV file of plain text addresses for parsing */
    public void  testLargeScaleAddressCleansing() throws Exception {

        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        CountryHandle country = new AddressDAO(em).lookupCountry("AUS");

        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        CsvTextReader csvReader = new CsvTextReader(ResourceTools.openStream("/exampleAddressesSml.csv"), csvOptions);
        String addressText;
        Address streetAddress;

        // initialise the address matcher
        AddressPatternMatcher matcher = new AddressPatternMatcher("AUS", em);

        while (csvReader.read()) {
            if (csvReader.isNonBlank()) {
                String[] values = csvReader.getAsStrings();
                try {
                    addressText = values[0];
                    if ((addressText != null) && (addressText.length() > 0)) {
                        // run the cleanser
                        matcher.reset();
                        streetAddress = matcher.extractBest(addressText);
                        System.out.println(StringUtils.leftPad(addressText, 38)+"|"+(streetAddress != null ? streetAddress.toString() : "FAIL"));
                    }
                }
                catch (ArrayIndexOutOfBoundsException e2) {
                    System.out.println();
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void testRegEx() {

        Pattern pattern = Pattern.compile("^(\\d+)/([\\d|\\-]+)$");
        Matcher m = pattern.matcher("6/6-8");

        assertTrue(m.find());
        assertEquals("6", m.group(1));
        assertEquals("6-8", m.group(2));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Tests searching or an address */
    public void testAddressSearch() throws Exception {
        AddressTestTools.initialisePlainAddresses(em);

        AddressDAO addressDAO = new AddressDAO(em);

        Address address = addressDAO.lookupAddressByExample(AddressTestTools.testAddressA());
        assertNotNull(address);

        address.print(System.out);
    }


}
