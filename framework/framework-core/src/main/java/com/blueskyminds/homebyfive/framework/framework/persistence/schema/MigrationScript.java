package com.blueskyminds.homebyfive.framework.framework.persistence.schema;

import java.net.URI;

/**
 * Identifies a migration script and the revision number it applies to
 *
 * Date Started: 29/03/2007
 * <p/>
 * History:
 * <p/>
 */
public class MigrationScript {

    private int revisionNo;
    private URI script;
    private String className;
    private boolean sqlFile;

    public MigrationScript(int revisionNo, URI script) {
        this.revisionNo = revisionNo;
        this.script = script;
        sqlFile = true;
    }

    public MigrationScript(int revisionNo, String className) {
        this.revisionNo = revisionNo;
        this.className = className;
        sqlFile = false;
    }

    public int getRevisionNo() {
        return revisionNo;
    }

    public URI getScript() {
        return script;
    }

    public String getClassName() {
        return className;
    }

    public boolean isSqlFile() {
        return sqlFile;
    }
}
