package com.blueskyminds.homebyfive.framework.framework.tools;

/**
 * Indicates that an error occured during a Crypto process
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class CryptoException extends Exception {

    public CryptoException() {
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
    
}
