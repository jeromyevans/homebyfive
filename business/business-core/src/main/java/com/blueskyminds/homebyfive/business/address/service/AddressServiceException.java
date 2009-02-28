package com.blueskyminds.homebyfive.business.address.service;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 27/02/2009
 */
public class AddressServiceException extends Exception {

    private static final Log LOG = LogFactory.getLog(AddressServiceException.class);

    public AddressServiceException() {
    }

    public AddressServiceException(String s) {
        super(s);
    }

    public AddressServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddressServiceException(Throwable throwable) {
        super(throwable);
    }
}
