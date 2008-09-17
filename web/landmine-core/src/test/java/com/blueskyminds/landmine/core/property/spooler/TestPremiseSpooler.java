package com.blueskyminds.landmine.core.property.spooler;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import com.blueskyminds.framework.persistence.spooler.SpoolerTask;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Tests spooling of Premises from persistence
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class TestPremiseSpooler extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPremiseSpooler.class);
    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private PremiseEAO premiseEAO;
    private AddressService addressService;

    public TestPremiseSpooler() {
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

    public void testSpooler() throws Exception {
        EntitySpooler<Premise> entitySpooler = new EntitySpooler<Premise>(premiseEAO, new SpoolerTask<Premise>() {
            public void process(List<Premise> entities) throws SpoolerException {
                for (Premise premise : entities) {
                    LOG.info(premise);
                }
            }
        });

        entitySpooler.start();
    }
}
