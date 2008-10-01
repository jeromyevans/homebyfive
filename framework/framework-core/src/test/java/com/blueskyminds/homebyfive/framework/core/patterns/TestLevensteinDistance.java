package com.blueskyminds.homebyfive.framework.core.patterns;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import junit.framework.TestCase;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestLevensteinDistance extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestLevensteinDistance.class);

    /**
     * Find the closest match for the value to one of the candidates.
     * If it's the same distance to two or more candidates the first is returned
     *
     * @param value
     * @param candidates
     * @return
     */
    private String closestMatch(String value, String[] candidates) {
        String match= null;
        int minDistance = Integer.MAX_VALUE;
        int distance;

        LOG.info(value+": ");
        for (String candidate : candidates) {
            distance = StringUtils.getLevenshteinDistance(value, candidate);
            LOG.info("  "+candidate +": "+distance);
            if (distance < minDistance) {
                match = candidate;
                minDistance = distance;
            }
        }
        return match;
    }

    /**
     * Find the closest match for the value to one of the candidates.
     * If it's the same distance to two or more candidates the first is returned
     *
     * @param value
     * @param candidates
     * @return
     */
    private String closestMatch2(String value, String[] candidates) {
        String match= null;
        int minDistance = Integer.MAX_VALUE;
        int distance;

        LOG.info(value+": ");
        for (String candidate : candidates) {
            distance = LevensteinDistanceTools.calculateLevenshteinDistance(value, candidate);
            LOG.info("  "+candidate +": "+distance);
            if (distance < minDistance) {
                match = candidate;
                minDistance = distance;
            }
        }
        return match;
    }

     /**
     * Find the closest match for the value to one of the candidates.
     * If it's the same distance to two or more candidates the first is returned
     *
     * @param value
     * @param candidates
     * @return
     */
    private String closestMatch3(String value, String[] candidates) {
        String match= null;
        int minDistance = Integer.MAX_VALUE;
        int distance;

        LOG.info(value+": ");
        for (String candidate : candidates) {
            distance = LevensteinDistanceTools.calculateDemerauLevenshteinDistance(value, candidate);
            LOG.info("  "+candidate +": "+distance);
            if (distance < minDistance) {
                match = candidate;
                minDistance = distance;
            }
        }
        return match;
    }

    public void testLevensteinMatch() {

        String[] suburbs = {
                "Neutral Bay",
                "Mosmon",
                "Milsons Point",
                "Kirribilli"

        };

        LOG.info("Using StringUtils:");
        assertEquals("Neutral Bay", closestMatch("Neutral Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch("Neutrel Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch("Neturel Bay", suburbs));
        assertEquals("Milsons Point", closestMatch("Milson's Point", suburbs));
        assertEquals("Kirribilli", closestMatch("Kirribili", suburbs));

        LOG.info("Using implmentation:");
        assertEquals("Neutral Bay", closestMatch2("Neutral Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch2("Neutrel Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch2("Neturel Bay", suburbs));
        assertEquals("Milsons Point", closestMatch2("Milson's Point", suburbs));
        assertEquals("Kirribilli", closestMatch2("Kirribili", suburbs));

        LOG.info("Using Damerau-Levenshtein distance:");
        assertEquals("Neutral Bay", closestMatch3("Neutral Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch3("Neutrel Bay", suburbs));
        assertEquals("Neutral Bay", closestMatch3("Neturel Bay", suburbs));
        assertEquals("Milsons Point", closestMatch3("Milson's Point", suburbs));
        assertEquals("Kirribilli", closestMatch3("Kirribili", suburbs));
    }


    /**
     * The purpose of this test is to highlight that levenshtein distance along does not suffice
     * See the TestMetaphone cases instead
     **/
    public void testLevensteinTroublesomeExample() {

        String[] suburbs = {
                "Aberdeen",
                "Woerden",
                "Verden",
                "Alberton",
                "Oberon",
                "Aberdare"

        };

        LOG.info("Using StringUtils:");
        assertEquals("Aberdeen", closestMatch("Aberdeen", suburbs));
        assertEquals("Aberdeen", closestMatch("Aberden", suburbs));

        LOG.info("Using implementation:");
        assertEquals("Aberdeen", closestMatch2("Aberdeen", suburbs));
        assertEquals("Aberdeen", closestMatch2("Aberden", suburbs));

        LOG.info("Using Damerau-Levenshtein distance:");
        assertEquals("Aberdeen", closestMatch3("Aberdeen", suburbs));
        assertEquals("Aberdeen", closestMatch3("Aberden", suburbs));

    }
}
