package com.blueskyminds.homebyfive.framework.core.tools;

import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;
import junit.framework.TestCase;

/**
 * Test for the StringTools class
 *
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class TestStringTools extends TestCase {

    public void testStripConsecutiveWhitespace() {
        String source = "This is   some text\n with whitespace\n\r";
        String target = "This is some text with whitespace";

        String result = StringTools.stripConsecutiveWhitespace(source);
        assertEquals(target, result);
    }
}
