package com.blueskyminds.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.business.AddressTestTools;
import com.blueskyminds.business.address.service.AddressService;
import com.blueskyminds.business.address.service.AddressServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Tests searching for a human-entered address
 * <p/>
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class TestAddressSearch extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressSearch.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestAddressSearch() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    private AddressService addressService;

    /**
     * Initialise some sample addresses
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses(getConnection());
        addressService = new AddressServiceImpl(em);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseAddress() throws Exception {


        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VIC", "AUS", 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseAddressTypo() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzry VIC", "AUS", 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseIncompleteAddress() throws Exception {
//        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street", "AUS", 10);
//        DebugTools.printCollection(addresses);

        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzro", "AUS", 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseAddressWithKnownSuburb() throws Exception {
        Address address = addressService.parseAddress("48 Westgarth Street", "Fitzroy", "VIC", "AUS");
        LOG.info(address);

    }


    /**
     * Parse a plain text address in a suburb that can be found in two states
     * <p/>
     * There is a Carlton in VIC and NSW
     *
     * @throws Exception
     */
    public void testParseMultipleSuburbAddress() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("Apt5305/560 Lygon Street Carlton VIC", "AUS", 10);
        DebugTools.printCollection(addresses);
    }

    public void testSearchAddress() throws Exception {
        // The following address exists
        // "48 Westgarth Street Fitzroy VIC"
        List<Address> addresses = addressService.findAddress("48 Westgarth Street Fitzroy VIC", "AUS");
        DebugTools.printCollection(addresses);

        // the following address does not exist
        List<Address> addresses2 = addressService.findAddress("1/22 Spruson Street, Neutral Bay NSW", "AUS");
        DebugTools.printCollection(addresses2);
    }

    /**
     * Decompose an address while it's being typed
     *
     * @throws Exception
     */
    public void testDecomposeAddress() throws Exception {
        DebugTools.printCollection(addressService.parseAddressCandidates("48", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 W", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Wes", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westg", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgar", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth S", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Str", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Stree", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street ", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fi", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitz", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzro", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy ", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VI", "AUS", 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VIC", "AUS", 10));
    }

    /**
     * Parse a plain text suburb address into its candidate components
     *
     * @throws Exception
     */
    public void testParseSuburb() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("Neutral Bay", "AUS", 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text suburb address into its candidate components
     *
     * @throws Exception
     */
    public void testParseSuburbIncomplete() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("Neutral", "AUS", 10);
        DebugTools.printCollection(addresses);
    }
}
