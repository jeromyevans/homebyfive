package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.test.SamplePremiseGenerator;

/**
 * Generates same premise data for testing
 *
 * Date Started: 14/03/2008
 * <p/>
 * History:
 */
public class TestGenerateSamplePremisesTest extends OutOfContainerTestCase {

    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private SamplePremiseGenerator samplePremiseGenerator;

    public TestGenerateSamplePremisesTest() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();    

        samplePremiseGenerator = new SamplePremiseGenerator(em);
    }

    public void testStart() throws Exception {
        samplePremiseGenerator.generateSamplePremises();
    }

}
