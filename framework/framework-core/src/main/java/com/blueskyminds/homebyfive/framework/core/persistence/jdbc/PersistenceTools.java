package com.blueskyminds.homebyfive.framework.core.persistence.jdbc;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.sql.*;
import java.util.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.IOException;

import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;
import com.blueskyminds.homebyfive.framework.core.tools.FileTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.StringFilter;
import com.blueskyminds.homebyfive.framework.core.tools.filters.NonBlankFilter;

/**
 * Useful persistence methods that use JDBC
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PersistenceTools {

    private static final Log LOG = LogFactory.getLog(PersistenceTools.class);

    private static final String ORDINAL_POSITION = "ORDINAL_POSITION";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param connection
     * @param sql - a SINGLE sql statement
     * @return row count or 0 for statements with no effect
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();

        if (LOG.isInfoEnabled()) {
            LOG.info("Executing update: "+sql);
        }

        int result = statement.executeUpdate(sql);

        if (LOG.isInfoEnabled()) {
            LOG.info("   "+result+" rows affected");
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param connection
     * @param sql
     * @return total row count
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, String[] sql) throws SQLException {

        int rowsAffected = 0;
        if (LOG.isInfoEnabled()) {
            LOG.info("Executing "+sql.length+ " updates...");
        }
        for (String sqlLine : sql) {
            Statement statement = connection.createStatement();
            rowsAffected += statement.executeUpdate(sqlLine);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("   "+rowsAffected+" rows affected");
        }

        return rowsAffected;
    }

    /**
     * @param connection
     * @param sql
     * @return total row count
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, List<String> sql) throws SQLException {

        int rowsAffected = 0;
        if (LOG.isInfoEnabled()) {
            LOG.info("Executing "+sql.size()+ " updates...");
        }
        for (String sqlLine : sql) {
            try {
                Statement statement = connection.createStatement();
                rowsAffected += statement.executeUpdate(sqlLine);
            } catch (SQLException e) {
                LOG.error(sqlLine);
                throw e;
            }
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("   "+rowsAffected+" rows affected");
        }

        return rowsAffected;
    }

    /**
     * @param connection
     * @param inputStream         from a text stream of SQL
     * @return total row count
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, InputStream inputStream) throws SQLException, IOException {
        StringFilter filter = new NonBlankFilter();
        String[] sqlLines =  FileTools.readTextFile(inputStream, filter);
        return executeUpdate(connection, sqlLines);
    }

    /**
     * Performs a query that returns a single object from the first column of the first row
     *
     * @param connection
     * @param query
     * @param throwException
     * @return
     * @throws SQLException
     */
    public static Object executeSingleValueQuery(Connection connection, String query, boolean throwException) throws SQLException {
        Statement statement = null;
        ResultSet resultSet;
        Object result = null;
        try {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            if (resultSet != null) {
                if (resultSet.next()) {
                    result = resultSet.getObject(1);
                }

            }

        } catch (SQLException e) {
            LOG.error("error running sql: " + e.getMessage());
            if (throwException) {
                throw e;
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // pass
                }
            }
        }
        return result;
    }

    /**
     * Performs a query that returns a single object from the first column of the first row
     *
     * @param statement
     * @param throwException
     * @return
     * @throws SQLException
     */
    public static Object executeSingleValueQuery(PreparedStatement statement, boolean throwException) throws SQLException {
        ResultSet resultSet;
        Object result = null;
        try {
            if (statement.execute()) {
                resultSet = statement.getResultSet();

                if (resultSet != null) {
                    if (resultSet.next()) {
                        result = resultSet.getObject(1);
                    }
                }
            }

        } catch (SQLException e) {
            LOG.error("error running sql: " + e.getMessage());
            if (throwException) {
                throw e;
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // pass
                }
            }
        }
        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PersistenceTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets the list of columns in a table, in order */
    public static String[] getColumnNames(Connection connection, String tableName) throws SQLException {

        DatabaseMetaData metadata = connection.getMetaData();

        // get all the columns for the table
        ResultSet columnDescriptions = metadata.getColumns(null, null, tableName, null);
        String columnName;
        int ordinalPosition;
        // we use a sorted map
        Map<Integer, String> columnPositionMap = new TreeMap<Integer, String>();

        while (columnDescriptions.next()) {
            columnName = columnDescriptions.getString(COLUMN_NAME);
            ordinalPosition = columnDescriptions.getInt(ORDINAL_POSITION);
            columnPositionMap.put(ordinalPosition, columnName);
        }

        String[] columnNames = new String[columnPositionMap.size()];
        int index = 0;
        for (Integer i : columnPositionMap.keySet()) {
            columnNames[index++] = columnPositionMap.get(i);
        }

        return columnNames;
    }


    // ------------------------------------------------------------------------------------------------------

    /** Gets the list of columns in a table, in order including type information */
    public static String[] getColumnDescriptor(Connection connection, String tableName) throws SQLException {

        DatabaseMetaData metadata = connection.getMetaData();

        // get all the columns for the table
        ResultSet columnDescriptions = metadata.getColumns(null, null, tableName, null);
        String columnName;
        String columnType;
        int columnSize;
        int ordinalPosition;
        String isNullable;

        // we use a sorted map
        Map<Integer, String> columnPositionMap = new TreeMap<Integer, String>();

        while (columnDescriptions.next()) {
            columnName = columnDescriptions.getString(COLUMN_NAME);
            columnType = columnDescriptions.getString("TYPE_NAME");
            columnSize = columnDescriptions.getInt("COLUMN_SIZE");
            isNullable = columnDescriptions.getString("IS_NULLABLE");
            ordinalPosition = columnDescriptions.getInt(ORDINAL_POSITION);
            columnPositionMap.put(ordinalPosition, columnName+" "+columnType+"("+columnSize+")"+(isNullable.equals("YES") ? "" : " not null"));
        }

        String[] columnNames = new String[columnPositionMap.size()];
        int index = 0;
        for (Integer i : columnPositionMap.keySet()) {
            columnNames[index++] = columnPositionMap.get(i);
        }

        return columnNames;
    }
// ------------------------------------------------------------------------------------------------------

    /** Gets the list of tables in the database */
    public static String[] getTableNames(Connection connection) throws SQLException {

        DatabaseMetaData metadata = connection.getMetaData();

        // get all the columns for the table
        ResultSet tableDescriptions = metadata.getTables(null, null, null, null);
        String tableName;
        int ordinalPosition;
        // we use a sorted map
        List<String> tableList = new LinkedList<String>();

        while (tableDescriptions.next()) {
            tableList.add(tableDescriptions.getString(TABLE_NAME));
        }

        String[] tableNames = new String[tableList.size()];
        tableList.toArray(tableNames);

        return tableNames;
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String sqlQuoteChar = "'";
    private static final String sqlQuoteEscaped = "''";

    // ------------------------------------------------------------------------------------------------------

    private static String escape(Object value) {
        String result = "";
        if (value == null) {
            result = "null";
        } else {
            result = sqlQuoteChar+StringUtils.replace(value.toString(), sqlQuoteChar, sqlQuoteEscaped)+sqlQuoteChar;
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Unload all the rows of a table and generate insert statements for them
     **/
    public static String[] unload(Connection connection, String tableName) throws SQLException {
        
        DatabaseMetaData metadata = connection.getMetaData();

        String[] columnNames = getColumnNames(connection, tableName);

        // lookup all the data...
        String allColumnNames = StringUtils.join(columnNames, ", ");
        String query = "select " + allColumnNames + " from "+ tableName;
        String insert = "insert into "+ tableName + " ("+ allColumnNames + ") values ";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        List<String> results = new LinkedList<String>();

        while (resultSet.next()) {
            StringBuffer values = new StringBuffer();
            boolean first = true;
            for (String columnName : columnNames) {
                Object object = resultSet.getObject(columnName);
                if (!first) {
                    values.append(", ");
                } else {
                    first = false;
                }
                values.append(escape(object));
            }
            results.add(insert+"("+values.toString()+")");
        }

        String[] array = new String[results.size()];
        return results.toArray(array);
    }


    /** Gets the last generated identity value */ 
    public static Integer getIdentity(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("{call identity()}");
        return result.getInt(1);

        //return null;
    }

    /** Generates a string containing information about all the tables
     *
     * Note it ignores tables starting with the given prefix
     *
     * All values are shown in lowercase
     * */
    public static String getTableMetadata(Connection connection, String ignorePrefix) {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        try {
            String[] tables = getTableNames(connection);
            for (String table : tables) {
                if (!StringTools.startWithIgnoreCase(table, ignorePrefix)) {
                    writer.print("table "+table.toLowerCase()+"(");
                    String[] columns = getColumnDescriptor(connection, table);
                    boolean first = true;
                    for (String columnName : columns) {
                        if (!first) {
                            writer.print(", ");
                        } else {
                            first = false;
                        }
                        writer.print(columnName.toLowerCase());
                    }
                    writer.println(")");
                }
            }
        } catch(SQLException e) {
            LOG.error("Failed to print table metadta", e);
        }
        return sw.getBuffer().toString();
    }
}
