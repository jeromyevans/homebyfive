package com.blueskyminds.homebyfive.framework.framework.persistence.schema;

import java.net.URI;

/**
 * Identifies a migration script directory and the revision number it applies to
 *
 * Date Started: 29/03/2007
 * <p/>
 * History:
 * <p/>
 */
public class MigrationDir {

    private int revisionNo;
    private URI directory;

    public MigrationDir(int revisionNo, URI directory) {
        this.revisionNo = revisionNo;
        this.directory = directory;
    }

    public int getRevisionNo() {
        return revisionNo;
    }

    public URI getDirectory() {
        return directory;
    }
}

