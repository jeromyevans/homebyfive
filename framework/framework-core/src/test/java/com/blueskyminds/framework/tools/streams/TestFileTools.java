package com.blueskyminds.framework.tools.streams;

import junit.framework.TestCase;
import com.blueskyminds.framework.tools.FileTools;

/**
 * Date Started: 26/02/2008
 * <p/>
 * History:
 */
public class TestFileTools extends TestCase {

    public void testParentFolder() {

        assertEquals("c:\\program files\\", FileTools.containingFolder("c:\\program files\\test", true));
        assertEquals("c:\\", FileTools.containingFolder("c:\\program files\\", true));
        assertEquals("", FileTools.containingFolder("c:\\", true));
        assertEquals("/usr/bin/", FileTools.containingFolder("/usr/bin/myssql", true));
        assertEquals("/usr/bin/", FileTools.containingFolder("/usr/bin/myssql/", true));
        assertEquals("/usr/", FileTools.containingFolder("/usr/bin", true));
        assertEquals("/", FileTools.containingFolder("/usr", true));
        assertEquals("", FileTools.containingFolder("/", true));
        assertEquals("", FileTools.containingFolder("blank.txt", true));
        assertEquals("/", FileTools.containingFolder("/blank.txt", true));

        assertEquals("c:\\program files", FileTools.containingFolder("c:\\program files\\test", false));
        assertEquals("c:\\", FileTools.containingFolder("c:\\program files\\", false));
        assertEquals("", FileTools.containingFolder("c:\\", false));
        assertEquals("/usr/bin", FileTools.containingFolder("/usr/bin/myssql", false));
        assertEquals("/usr/bin", FileTools.containingFolder("/usr/bin/myssql/", false));
        assertEquals("/usr", FileTools.containingFolder("/usr/bin", false));
        assertEquals("/", FileTools.containingFolder("/usr", false));
        assertEquals("", FileTools.containingFolder("/", false));
        assertEquals("", FileTools.containingFolder("blank.txt", false));
        assertEquals("/", FileTools.containingFolder("/blank.txt", false));
    }
}
