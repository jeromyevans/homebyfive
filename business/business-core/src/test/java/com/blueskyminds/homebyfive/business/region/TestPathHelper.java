package com.blueskyminds.homebyfive.business.region;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

/**
 * Date Started: 7/03/2008
 * <p/>
 * History:
 */
public class TestPathHelper extends TestCase {

    public void testJoinPaths() {
        assertNull(PathHelper.joinPaths());
        assertEquals("/", PathHelper.joinPaths(""));
        assertEquals("/", PathHelper.joinPaths("/"));
        assertEquals("/au", PathHelper.joinPaths("au"));
        assertEquals("/au/nsw", PathHelper.joinPaths("au", "nsw"));
        assertEquals("/au/nsw", PathHelper.joinPaths("au", "/nsw"));
        assertEquals("/au/nsw", PathHelper.joinPaths("/au", "/nsw"));
        assertEquals("/au/nsw", PathHelper.joinPaths("au", "//nsw"));
    }

    public void testGenerateId() {
        assertEquals("neutral+bay", KeyGenerator.generateId("Neutral Bay"));
        assertEquals("oconner", KeyGenerator.generateId("O'Conner"));
        assertEquals("ryde", KeyGenerator.generateId("Ryde"));
        assertEquals("key-west", KeyGenerator.generateId("Key-west"));
        assertEquals("north+key-west", KeyGenerator.generateId("North Key-west"));

    }
}
