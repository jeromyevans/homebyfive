package com.blueskyminds.homebyfive.framework.framework.persistence.jdbc;

/**
 * A class that implements a Finder in the Row Data Gateway pattern in PoEAA
 *
 * Date Started: 15/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface RowDataGatewayFinder {

    String getColumnName(int index);

    /** Vendor independent select by Id */
    String getSelectStatement();

    String getInsertStatement();
}
