package com.blueskyminds.housepad.core.region;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.enterprise.region.reference.RegionRefType;
import com.blueskyminds.enterprise.region.composite.RegionComposite;

/**
 * Date Started: 16/09/2007
 */
public class TestRegionComposite extends TestCase {

    public static final Log LOG = LogFactory.getLog(TestRegionComposite.class);

    public void testRegionComposite() throws Exception {
        RegionComposite result = new RegionComposite();

        result.add(1L, "", "1/22", RegionRefType.Address);
        result.add(2L, "", "Spruson Street", RegionRefType.Street);
        result.add(3L, "", "Neutral Bay", RegionRefType.Suburb);
        result.add(4L, "", "NSW", RegionRefType.State);
        result.add(5L, "", "2089", RegionRefType.PostCode);

        LOG.info(result);
    }
}
