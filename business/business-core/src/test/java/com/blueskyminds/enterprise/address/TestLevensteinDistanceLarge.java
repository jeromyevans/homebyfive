package com.blueskyminds.enterprise.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.time.StopWatch;

import java.util.*;

/**
 * Examines the performance of the Levenstein Distance implementation with large datasets.
 * The intention is to see if this can be reasonably implemented in the client.
 *
 * Date Started: 18/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestLevensteinDistanceLarge  extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestLevensteinDistanceLarge.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestLevensteinDistanceLarge() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    private class RegionNode implements Named {
        private Long id;
        private String name;

        public RegionNode(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name + " ("+id+")";
        }
    }

    public void testSuburbMatch() throws Exception {
        AddressTestTools.initialiseCountryList();

        Collection<Region> allSuburbs = TestTools.findAll(Region.class, em);

        Set<RegionNode> regionsNodes = new HashSet<RegionNode>(allSuburbs.size());
        // transfer to a simple set of nodes
        for (Region suburb : allSuburbs) {
            regionsNodes.add(new RegionNode(suburb.getId(), suburb.getName()));
        }

        match("Neutral Bay", regionsNodes);
        match("Nutral Bay", regionsNodes);
        match("Neutrel Bay", regionsNodes);
        match("Nutrel Bay", regionsNodes);

    }

    private void match(String name, Set<RegionNode> regionNodes) {
        LOG.info("Matching "+name+" ("+regionNodes.size()+" nodes)...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<RegionNode> matches = LevensteinDistanceTools.matchName(name, regionNodes);
        stopWatch.stop();
        LOG.info(stopWatch);
        for (RegionNode match : matches) {
            LOG.info("   "+match);
        }
    }

    public void testSuburbMatchLarger() throws Exception {
        AddressTestTools.initialiseCountryList();

        Collection<Region> allSuburbs = TestTools.findAll(Region.class, em);

        Set<RegionNode> regionsNodes = new HashSet<RegionNode>(allSuburbs.size());
        for (int i = 0; i < 7; i++) {
            // transfer to a simple set of nodes
            for (Region suburb : allSuburbs) {
                regionsNodes.add(new RegionNode(suburb.getId(), suburb.getName()));
            }
        }

        match("Neutral Bay", regionsNodes);
        match("Nutral Bay", regionsNodes);
        match("Neutrel Bay", regionsNodes);
        match("Nutrel Bay", regionsNodes);
    }
}
