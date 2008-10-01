package com.blueskyminds.homebyfive.framework.core.tools;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

/**
 * Helper class for accessing the Classpath
 *
 * Date Started: 21/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class ClassPath {

    private static final String CLASSPATH_PROPERTY = "java.class.path";

    private Set<String> jars;
    private Set<String> dirs;
    private Set<String> files;

    public ClassPath() {
        init();
        String path = System.getProperty(CLASSPATH_PROPERTY);
        String[] paths = StringUtils.split(path, ';');

        for (String thisPath : paths) {
            if (isJarPath(thisPath)) {
                File f = new File(thisPath);
                if (f.exists()) {
                    jars.add(f.getAbsolutePath());
                }
            } else {
                File f = new File(thisPath);
                if (f.isDirectory()) {
                    dirs.add(f.getAbsolutePath());
                }
                else {
                    if (f.exists()) {
                        files.add(f.getAbsolutePath());
                    }
                }
            }
        }
    }


    // ------------------------------------------------------------------------------------------------------

    /** Return all directories in the classpath containing the pattern */
    public List<String> getDirsContainingPattern(String pattern) {
        List<String> dirsFound = new LinkedList<String>();
        for (String dir : dirs) {
            if (dir.contains(pattern)) {
                dirsFound.add(dir);
            }
        }
        return dirsFound;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return true if the classpath contains a jar matching the specified pattern */
    public boolean includesJar(String pattern) {
        boolean found = false;
        for (String path : jars) {
            if (path.contains(pattern)) {
                found = true;
                break;
            }
        }
        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     *
     * @param path
     * @return true if the path is to a jar or zip
     */
    private boolean isJarPath(String path) {
        return ((path.endsWith(".jar")) || (path.endsWith(".zip")));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the ClassPath with default attributes
     */
    private void init() {
        jars = new HashSet<String>();
        dirs = new HashSet<String>();
        files = new HashSet<String>();
    }

    // ------------------------------------------------------------------------------------------------------
}
