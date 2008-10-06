package com.blueskyminds.business.test;

import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.StringFilter;
import com.blueskyminds.homebyfive.framework.core.tools.filters.NonBlankFilter;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.core.tools.FileTools;
import com.blueskyminds.homebyfive.framework.core.persistence.jdbc.PersistenceTools;

import java.sql.Connection;
import java.sql.SQLException;
import java.net.URI;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 15/04/2008
 */
public class LoadRegionBeanData extends TestTools {

    private static final Log LOG = LogFactory.getLog(LoadRegionBeanData.class);

    private static final String[] SQL_FILES = {
            "hpcountry",
            "hpstate",
            "hppostcode",
            "hpsuburb"
    };

    public static final String AUSTRALIA = "Australia";

    protected static void applySQLFiles(Connection connection, String[] sqlFiles) {
        StringFilter filter = new NonBlankFilter();
        String[] sqlLines;
        try {
            for (String file : sqlFiles) {
                URI location = ResourceTools.locateResource(file+".sql");
                if (location != null) {
                    sqlLines = FileTools.readTextFile(location, filter);
                    PersistenceTools.executeUpdate(connection, sqlLines);
                    connection.commit();
                } else {
                    LOG.error("Could not find resource: "+file+".sql");
                }
            }
        } catch(SQLException e) {
            LOG.error("SQL error", e);
        } catch (IOException e) {
            LOG.error("Failed to read data", e);
        }
    }

        /** Initialise all of the reference region data.
     * This method uses a direct JDBC connection to the in-memory hypersonic database */
    public static void load() throws SQLException {
        createHSQLDB();
        Connection connection = getConnection();

        connection.setAutoCommit(false);

        applySQLFiles(connection, SQL_FILES);                
    }

}
