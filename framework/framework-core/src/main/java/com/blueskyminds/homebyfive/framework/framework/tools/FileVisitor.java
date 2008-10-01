package com.blueskyminds.homebyfive.framework.framework.tools;

import java.net.URI;

/**
 *
 * Used by FileTools to visit the files in a directory or jar
 *
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public interface FileVisitor {

    void visit(URI pathToFile, boolean isDirectory);
    
}
