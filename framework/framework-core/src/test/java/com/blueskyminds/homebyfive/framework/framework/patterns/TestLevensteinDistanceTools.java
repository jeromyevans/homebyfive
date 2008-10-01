package com.blueskyminds.homebyfive.framework.framework.patterns;

import junit.framework.TestCase;

import java.util.List;

/**
 * Date Started: 21/03/2008
 * <p/>
 * History:
 */
public class TestLevensteinDistanceTools extends TestCase {

    public void testMatch() {
        String[] suburbs = {
                "Aberdeen",
                "Woerden",
                "Verden",
                "Alberton",
                "Oberon",
                "Aberdare"

        };

        List<String> matches = LevensteinDistanceTools.match("Aberdeen", suburbs);
        assertEquals(1, matches.size());
        assertEquals("Aberdeen", matches.get(0));

        List<String> matches2 = LevensteinDistanceTools.match("Aberd", suburbs);
        assertEquals(2, matches2.size());
    }
}
