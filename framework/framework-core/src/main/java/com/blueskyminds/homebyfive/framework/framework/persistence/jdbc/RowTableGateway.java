package com.blueskyminds.homebyfive.framework.framework.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Identifies an entity/class that supports the Row Table Gateway pattern (PoEAA).
 *
 * This means an instance can populate itself from a jdbc ResultSet and an instance can insert itself over a
 *  connection.
 *
 * A persistent entity (using ORM) can also support RowTableGateway, but it carries some risk that the JDBC
 *  code is not consistent with the ORM.
 *
 * Date Started: 15/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface RowTableGateway {

    /**
     * Perform an insert using the values of this entity over the jdbc connection
     *
     * @param connection    - an open jdbc connection to use
     * @return rows affected
     * @throws SQLException
     */
    int insert(Connection connection) throws SQLException;

    /**
     * Load this instance with values in the current ResultSet
     *
     * @param resultSet   - the current result set is used (its not scrolled)
     * @throws SQLException
     */
    void load(ResultSet resultSet) throws SQLException;
}
