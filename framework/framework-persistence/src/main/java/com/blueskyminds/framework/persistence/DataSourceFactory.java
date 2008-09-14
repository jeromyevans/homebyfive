package com.blueskyminds.framework.persistence;

import javax.sql.DataSource;

import com.blueskyminds.framework.persistence.hsqldb.HsqlDbMemoryDataSourceFactory;
import com.blueskyminds.framework.persistence.mysql.MysqlDataSourceFactory;
import com.blueskyminds.framework.tools.PropertiesContext;

import java.util.Properties;

/**
 * A factory to create a DataSource
 *
 * Developer note: This class should not have dependencies on vendor DataSourcs - only on the factories that
 *  create them.  We want to be able to deploy this class without all those dependencies available.
 *
 * Date Started: 13/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DataSourceFactory {
    
    private static final String DATASOURCE_TYPE = "datasource.type";
    private static final String DATASOURCE_DATABASE_NAME = "datasource.database.name";
    private static final String DATASOURCE_DATABASE_USER = "datasource.database.user";
    private static final String DATASOURCE_DATABASE_PASSWORD = "datasource.database.password";

    /**
     * Create a DataSource with a type specified by the enumeration
     *
     * @param type              type of datasource to create - determines which factory/driver to use
     * @param database          database/catalog name
     * @param user              user to login as
     * @param plaintextPassword    user's password (plain text)
     * @return a jdbc DataSource
     */
    public static DataSource createDataSource(DataSourceType type, String database, String user, String plaintextPassword) throws DataSourceFactoryException {

        DataSource dataSource = null;

        switch (type) {
            case hsqldb:
                dataSource = HsqlDbMemoryDataSourceFactory.createDataSource(database, user, plaintextPassword);
                break;
            case mysql:
                dataSource = MysqlDataSourceFactory.createDataSource(database, user, plaintextPassword);
                break;
        }

        return dataSource;
    }

    /**
     * Create a DataSource with the specified properties
     *
     * @param properties    datasource factory properties containing:
     *
     *      datasource.type
     *      datasource.database.name
     *      datasource.database.user
      *     datasource.database.password (sha1 encrypted)
     *
     * @return a jdbc DataSource
     */
    public static DataSource createDataSource(Properties properties) throws DataSourceFactoryException {

        DataSourceType type;
        String typeName = properties.getProperty(DATASOURCE_TYPE);
        try {
            type = DataSourceType.valueOf(typeName);
        } catch(IllegalArgumentException e) {
            throw new DataSourceFactoryException("Invalid DataSource type: "+typeName, e);
        }

        String name = properties.getProperty(DATASOURCE_DATABASE_NAME, "");
        String user = properties.getProperty(DATASOURCE_DATABASE_USER, "");
        String password = properties.getProperty(DATASOURCE_DATABASE_PASSWORD, "");

        return createDataSource(type, name, user, password);
    }

    /**
     * Create a DataSource with the specified properties
     *
     * @param properties    a PropertiesContext containing:
     *
     *      datasource.type
     *      datasource.database.name
     *      datasource.database.user
      *     datasource.database.password (sha1 encrypted)
     *
     * @return a jdbc DataSource
     */
    public static DataSource createDataSource(PropertiesContext properties) throws DataSourceFactoryException {
        if (properties != null) {
            return DataSourceFactory.createDataSource(properties.getProperties());
        } else {
            throw new DataSourceFactoryException("DataSourceFactory properties are null");
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
