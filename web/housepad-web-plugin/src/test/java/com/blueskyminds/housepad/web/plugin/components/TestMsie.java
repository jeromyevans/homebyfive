package com.blueskyminds.housepad.web.plugin.components;

import junit.framework.TestCase;

/**
 * Date Started: 12/03/2008
 * <p/>
 * History:
 */
public class TestMsie extends TestCase {

    public void testParse() {
        assertTrue(Msie.userAgentOkay("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)", null, null, null));
        assertTrue(Msie.userAgentOkay("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)", "7", null, null));
        assertFalse(Msie.userAgentOkay("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)", "6", null, null));
        assertTrue(Msie.userAgentOkay("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)", null, 6.0F, null));
        assertTrue(Msie.userAgentOkay("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 6.0)", null, null, 7.0F));
    }
}
