package com.blueskyminds.homebyfive.framework.framework.persistence.schema;

import java.sql.Connection;

/**
 * Performs incremental migration of a database schema
 */
public interface SchemaMigrator {

    /**
     * Migrate the database schema to the current revision
     */
    boolean migrate(Connection connection, String migrationDialect) throws MigrationFailedException;
}
