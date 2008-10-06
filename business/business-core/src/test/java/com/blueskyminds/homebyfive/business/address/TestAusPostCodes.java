package com.blueskyminds.homebyfive.business.address;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.address.tools.AusPostCodes;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;

import java.util.Collection;

/**
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
public class TestAusPostCodes extends TestCase {

    public void testReadCSV() throws Exception {
        Collection<AusPostCodes.State> states = AusPostCodes.readCsv(ResourceTools.openStream("/src/resources/pc-full_20080303.csv"));
        assertNotNull(states);
        assertEquals(8, states.size());
    }
}
