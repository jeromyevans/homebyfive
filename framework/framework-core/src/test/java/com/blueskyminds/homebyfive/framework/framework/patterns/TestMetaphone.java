package com.blueskyminds.homebyfive.framework.framework.patterns;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.language.DoubleMetaphone;
import junit.framework.TestCase;

/**
 *
 * The Metaphone algorithm is for the phonetic encoding of strings
 *
 * Date Started: 18/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestMetaphone extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestMetaphone.class);

    /**
     * Find the phonetically closest match
     *
     * @param value
     * @param candidates
     * @return
     */
    private String closestMatch(String value, String[] candidates) {
        String match= null;

        LOG.info(value+": ");
        DoubleMetaphone encoder = new DoubleMetaphone();
        String valueCode = encoder.doubleMetaphone(value);
        for (String candidate : candidates) {
            String candidateCode = encoder.doubleMetaphone(candidate);

            LOG.info("  "+candidate +": "+candidateCode);
            if (candidateCode.equals(valueCode)) {
                match = candidate;
                break;
            }
        }
        return match;
    }

    /** The purpose of this test is to highlight that levenshtein distance along does not suffice */
    public void testLevensteinTroublesomeExample() {

        String[] suburbs = {
                "Aberdeen",
                "Woerden",
                "Verden",
                "Alberton",
                "Oberon",
                "Aberdare"

        };

        LOG.info("Using extract match Metaphone:");
        assertEquals("Aberdeen", closestMatch("Aberdeen", suburbs));
        assertEquals("Aberdeen", closestMatch("Aberden", suburbs));

    }
}
