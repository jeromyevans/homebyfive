package com.blueskyminds.homebyfive.business.address.service;

/**
 * Indicates a critical problem while processing an Address
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AddressProcessingException extends Exception {

    public AddressProcessingException() {
    }

    public AddressProcessingException(String message) {
        super(message);
    }

    public AddressProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressProcessingException(Throwable cause) {
        super(cause);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AddressProcessingException with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
