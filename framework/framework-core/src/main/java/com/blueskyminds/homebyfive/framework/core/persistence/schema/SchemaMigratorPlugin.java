package com.blueskyminds.homebyfive.framework.core.persistence.schema;

import java.sql.Connection;

/**
 * Classes that implement this interface can be plugged into the SchemaMigrator to perform a migration task
 * that's too complicated plain SQL.
 *
 * If the schema migration order file contains an entry java:className then the migrator will create an instance
 *  and execute it.  Ensure the class has a default constructor.
 *
 */
public interface SchemaMigratorPlugin {

    /**
     * Apply a migration to a schema
     * @return true if the opreration was success, false if the transaction (and migration) should be aborted.
     */
    boolean migrate(MigrationScript migrationScript, Connection connection, String dialectName);
}

