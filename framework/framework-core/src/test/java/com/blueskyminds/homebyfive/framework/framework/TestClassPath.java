package com.blueskyminds.homebyfive.framework.framework;

import com.blueskyminds.homebyfive.framework.framework.test.BaseTestCase;
import com.blueskyminds.homebyfive.framework.framework.tools.ClassPath;

import java.util.List;
import java.io.File;

/**
 * Date Started: 21/08/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestClassPath extends BaseTestCase {

    public TestClassPath(String string) {
        super(string);
    }

    public void testClasspath() {
        ClassPath cp = new ClassPath();
        List<String> dirs = cp.getDirsContainingPattern("target"+ File.separator+"classes");
        assertNotNull(dirs);
        assertEquals(1, dirs.size());
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestClassPath with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
