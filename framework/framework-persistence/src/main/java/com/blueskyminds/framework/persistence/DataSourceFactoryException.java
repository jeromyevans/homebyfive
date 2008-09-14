package com.blueskyminds.framework.persistence;

/**
 * Thrown if the DataSourceFactory can't create a DataSource
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DataSourceFactoryException extends Exception {

    public DataSourceFactoryException() {
    }

    public DataSourceFactoryException(String message) {
        super(message);
    }

    public DataSourceFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceFactoryException(Throwable cause) {
        super(cause);
    }
}
