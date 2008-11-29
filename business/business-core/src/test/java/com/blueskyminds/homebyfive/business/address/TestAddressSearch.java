package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
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


        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VIC", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseAddressTypo() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzry VIC", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseIncompleteAddress() throws Exception {
//        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street", Countries.AU, 10);
//        DebugTools.printCollection(addresses);

        List<Address> addresses = addressService.parseAddressCandidates("48 Westgarth Street Fitzro", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text address into its candidate components
     *
     * @throws Exception
     */
    public void testParseAddressWithKnownSuburb() throws Exception {
        Address address = addressService.parseAddress("48 Westgarth Street", "Fitzroy", "VIC", Countries.AU);
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
        List<Address> addresses = addressService.parseAddressCandidates("Apt5305/560 Lygon Street Carlton VIC", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }

    public void testSearchAddress() throws Exception {
        // The following address exists
        // "48 Westgarth Street Fitzroy VIC"
        List<Address> addresses = addressService.findAddress("48 Westgarth Street Fitzroy VIC", Countries.AU);
        DebugTools.printCollection(addresses);

        // the following address does not exist
        List<Address> addresses2 = addressService.findAddress("1/22 Spruson Street, Neutral Bay NSW", Countries.AU);
        DebugTools.printCollection(addresses2);
    }

    /**
     * Decompose an address while it's being typed
     *
     * @throws Exception
     */
    public void testDecomposeAddress() throws Exception {
        DebugTools.printCollection(addressService.parseAddressCandidates("48", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 W", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Wes", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westg", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgar", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth S", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Str", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Stree", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street ", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fi", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitz", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzro", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy ", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VI", Countries.AU, 10));
        DebugTools.printCollection(addressService.parseAddressCandidates("48 Westgarth Street Fitzroy VIC", Countries.AU, 10));
    }

    /**
     * Parse a plain text suburb address into its candidate components
     *
     * @throws Exception
     */
    public void testParseSuburb() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("Neutral Bay", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }

    /**
     * Parse a plain text suburb address into its candidate components
     *
     * @throws Exception
     */
    public void testParseSuburbIncomplete() throws Exception {
        List<Address> addresses = addressService.parseAddressCandidates("Neutral", Countries.AU, 10);
        DebugTools.printCollection(addresses);
    }
}
