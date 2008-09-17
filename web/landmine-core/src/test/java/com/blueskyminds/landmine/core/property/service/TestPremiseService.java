package com.blueskyminds.landmine.core.property.service;

import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.landmine.core.test.LandmineTestHelper;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Set;

/**
 * Date Started: 29/10/2007
 * <p/>
 * History:
 */
public class TestPremiseService extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPremiseService.class);
    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private LandmineTestHelper env;

    public TestPremiseService() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
       
        env = LandmineTestHelper.setupTestEnvironment(em).
                withRegions().
                withProperties();
    }

    private void print(Set<Premise> premises) {
        for (Premise premise : premises) {
            premise.print(System.out);
        }
    }

    public void testListPremisesInSuburb() {
        List<SuburbHandle> suburbs = env.getAddressService().findSuburb("Carlton", "AUS");
        for (SuburbHandle suburb : suburbs) {
            Set<Premise> premises = env.getPremiseService().listPremises(suburb);
            print(premises);
        }
    }

    public void testListPremisesByPostCode() throws Exception {
        List<PostCodeHandle> postcodes = env.getAddressService().findPostCode("3053", "AUS");
        for (PostCodeHandle postCodeHandle : postcodes) {
            Set<Premise> premises = env.getPremiseService().listPremises(postCodeHandle);
            print(premises);
        }
    }

    public void testListPremisesByStreet() throws Exception {
        List<Street> streets = env.getAddressService().findStreet("lygon", "AUS");
        for (Street street : streets) {
            Set<Premise> premises = env.getPremiseService().listPremises(street);
            print(premises);
        }
    }

    public void testListEvents() throws Exception {
        List<PremiseEvent> events = env.getPremiseService().listPremiseEvents("/au");
    }

    /** Assert that a complex is created with the right type */
    public void testCreatePremiseAndComplex() throws Exception {
        SuburbHandle suburb = env.getAddressService().findSuburb("Carlton", "AUS").iterator().next();
        assertNotNull(suburb);
        SuburbBean suburbBean = env.getRegionService().lookupOrCreateSuburb(suburb);

        PropertyFormBean bean = new PropertyFormBean(suburbBean);
        bean.setUnitNo("3");
        bean.setStreetNo("26");
        bean.setStreetName("Yeo");
        bean.setStreetType(StreetType.Street);
        Premise premise = env.getPremiseService().lookupOrCreatePremise(bean, suburbBean, "test", 1L);
        assertNotNull(premise);
        em.flush();
        Set<PropertyBean> property = env.getPropertyService().lookupProperties(premise);
        assertNotNull(property);
        assertEquals(1, property.size());
                
        Premise complex = env.getPremiseService().lookupPremise("/au/vic/carlton/yeo+street/26");
        assertNotNull(complex);
        assertEquals(PropertyTypes.Complex, complex.getType());
        Premise unit = env.getPremiseService().lookupPremise("/au/vic/carlton/yeo+street/26/3");
        assertNotNull(unit);
        assertNull(unit.getType());
    }
}
