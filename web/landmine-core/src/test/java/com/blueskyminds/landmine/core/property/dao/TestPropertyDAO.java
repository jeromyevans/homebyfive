package com.blueskyminds.landmine.core.property.dao;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Set;

/**
 * Date Started: 28/10/2007
 * <p/>
 * History:
 */
public class TestPropertyDAO extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPropertyDAO.class);
    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private PremiseEAO premiseEAO;
    private AddressService addressService;

    public TestPropertyDAO() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        addressService = new AddressServiceImpl(em);
        premiseEAO = new PremiseEAO(em);
    }

    private void print(Set<Premise> premises) {
        for (Premise premise : premises) {
            premise.print(System.out);
        }
    }
   
    public void testListPremisesInSuburb() {
        List<SuburbHandle> suburbs = addressService.findSuburb("Carlton", "AUS");
        for (SuburbHandle suburb : suburbs) {
            Set<Premise> premises = premiseEAO.listPremisesInSuburb(suburb);
            print(premises);
        }
    }

    public void testListPremisesByPostCode() throws Exception {
        List<PostCodeHandle> postcodes = addressService.findPostCode("3053", "AUS");
        for (PostCodeHandle postCodeHandle : postcodes) {
            Set<Premise> premises = premiseEAO.listPremisesInPostCode(postCodeHandle);
            print(premises);
        }
    }

    public void testListPremisesByStreet() throws Exception {
        List<Street> streets = addressService.findStreet("lygon", "AUS");
        for (Street street : streets) {
            Set<Premise> premises = premiseEAO.listPremisesInStreet(street);
            print(premises);
        }
    }

}
