package com.blueskyminds.homebyfive.framework.framework.persistence.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

import com.blueskyminds.homebyfive.framework.framework.persistence.jdbc.PersistenceTools;

/**
 *
 * The SchemaRevision is used to record a revision number for the current database schema for each module.
 * Changes in the revision number are required for schema migration
 *
 */
public class SchemaRevision {

    private static final Log LOG = LogFactory.getLog(SchemaRevision.class);

    private static final String SCHEMA_REVISION = "SchemaRevision";

    private static final String LOOKUP_SCHEMA_REVISION =
        "select max(revNo) from "+ SCHEMA_REVISION + " where schemaName = ?";

    private static final String UPDATE_SCHEMA_REVISION =
        "update "+SCHEMA_REVISION+" set revNo = ? where schemaName = ?";

    private static final String INSERT_SCHEMA_REVISION =
        "insert into "+SCHEMA_REVISION+" (revNo, schemaName) values (?, ?)";

    public static final String CREATE_SCHEMA_REVISION_TABLE =
        "CREATE TABLE "+ SCHEMA_REVISION +" ( "+
        "    revNo integer NOT NULL, "+
        "    schemaName varchar(64) NOT NULL, "+
        "    dateStamp TIMESTAMP DEFAULT current_timestamp, "+
        "    primary key (revNo, schemaName) "+
        ")";

    private Connection connection;
    private String dialectName;

    public SchemaRevision(Connection connection, String dialectName) {
        this.connection = connection;
        this.dialectName = dialectName;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SchemaRevision with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if the SchemaRevision table exists in this database
     **/
    public boolean hasSchemaRevisionTable() throws SQLException {
        boolean found = false;

        DatabaseMetaData databaseMetadata = connection.getMetaData();
        ResultSet tables = databaseMetadata.getTables(null, null, null, null);
        if (tables != null) {
            while ((tables.next()) && (!found)) {
                if (SCHEMA_REVISION.equalsIgnoreCase(tables.getString("TABLE_NAME"))) {
                    found = true;
                }
            }
        }

        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the current schema revision for a module
     * @return the revision number, or -1 if the table exists and has no value for the moduleName, or null of the table doesn't exist
     */
    public Integer lookupRevision(String schemaName) throws SQLException {
        Integer value = null;
        if (hasSchemaRevisionTable()) {
            PreparedStatement statement = connection.prepareStatement(LOOKUP_SCHEMA_REVISION);
            statement.setString(1, schemaName);
            value = (Integer) PersistenceTools.executeSingleValueQuery(statement, true);
            if (value == null) {
                value = -1;
            }
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates the table used to track the schema revision number */
    public void createSchemaRevisionTable() throws SQLException {
        PersistenceTools.executeUpdate(connection, CREATE_SCHEMA_REVISION_TABLE);        
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Updatee the current revision
     *
     * This needs to be wrapped by a database transaction
     */
    public void updateRevision(String schemaName, int revisionNo) throws SQLException {

        PreparedStatement statement;
        Integer currentRevision = lookupRevision(schemaName);
        if (currentRevision == null) {
            createSchemaRevisionTable();
            currentRevision = lookupRevision(schemaName);
        }
        if (currentRevision != null) {
            if (currentRevision == -1) {
                statement = connection.prepareStatement(INSERT_SCHEMA_REVISION);
            } else {
                statement = connection.prepareStatement(UPDATE_SCHEMA_REVISION);
            }
            statement.setInt(1, revisionNo);
            statement.setString(2, schemaName);
            statement.executeUpdate();
        } else {
            throw new SQLException("Unable to access/create the SchemaRevision table");
        }
    }
}
