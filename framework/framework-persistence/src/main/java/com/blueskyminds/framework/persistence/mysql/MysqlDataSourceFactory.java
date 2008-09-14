package com.blueskyminds.framework.persistence.mysql;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Creates a Mysql DataSource
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class MysqlDataSourceFactory {
 /**
     * Create an MySql datasource
     *
     * @param database           database name
     * @param user               user to login as
     * @param plaintextPassword  password to use in plain text
     * @return a jdbc datasource
     */
    public static DataSource createDataSource(String database, String user, String plaintextPassword) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setDatabaseName(database);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(plaintextPassword);
        return mysqlDataSource;
    }
}
