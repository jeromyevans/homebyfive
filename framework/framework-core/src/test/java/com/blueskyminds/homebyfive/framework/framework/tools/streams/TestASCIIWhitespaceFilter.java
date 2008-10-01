package com.blueskyminds.homebyfive.framework.framework.tools.streams;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;

/**
 * Runs some tests through the filter
 *
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class TestASCIIWhitespaceFilter extends TestCase {

    public void testUnmodified() throws Exception {
        String sample = "This is simple text";
        String expectedResult = sample;

        ByteArrayOutputStream out = new ByteArrayOutputStream(sample.getBytes().length);
        FilterOutputStream filter = new ASCIIWhitespaceFilter(out);
        filter.write(sample.getBytes());

        assertEquals(expectedResult, toString(out));
    }

    public void testModified() throws Exception {
        String sample = "This is simple text\nspanning two lines\n";
        String expectedResult = "This is simple text spanning two lines ";

        ByteArrayOutputStream out = new ByteArrayOutputStream(sample.getBytes().length);

        FilterOutputStream filter = new ASCIIWhitespaceFilter(out);
        filter.write(sample.getBytes());

        assertEquals(expectedResult, toString(out));
    }

    private String toString(ByteArrayOutputStream out) {
        return new String(out.toByteArray());
    }
}
