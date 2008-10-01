package com.blueskyminds.homebyfive.framework.core.persistence.jdbc;

import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * Methods that assist with jdbc
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JdbcTools {


    /**
     * Creates a string of comma separated ? for use as an array parameter
     *
     * eg. If length is 3, it returns:  ?, ?, ?
     *
     * @param length    number of values in the array
     * @return a string that can be used as an array parameter
     */
    public static String createArrayParamater(int length) {
        String result = "";
        if (length > 0) {
            result = StringTools.fillRepeat("?,", length-1)+"?";
        }
        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sets an array parameter in a prepared statement
     *
     * The statement must contain the right number of parameters {@see createArrayParameter} to hold the
     * array
     *
     * @param statement     the preparedStatement
     * @param firstIndex    the first parameter index in the statrtment to start the array (don't forget it starts at 1)
     * @param array         values to set
     * @throws SQLException if the parameters can't be set in the statement
     */
    public static void setArrayParameter(PreparedStatement statement, int firstIndex, Integer[] array) throws SQLException {
        if (array.length > 0) {
            for (int index = 0; index < array.length; index++) {
                statement.setInt(firstIndex+index, array[index]);
            }
        }
    }

    // todo: add other sql data type implementations as needed
}
