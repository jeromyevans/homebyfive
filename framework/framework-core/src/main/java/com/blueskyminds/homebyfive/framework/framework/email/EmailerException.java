package com.blueskyminds.homebyfive.framework.framework.email;

/**
 * Indicates that an error occured while trying to send an email
 *
 * Date Started: 3/08/2007
 * <p/>
 * History:
 */
public class EmailerException extends Exception {


    public EmailerException() {
    }

    public EmailerException(String message) {
        super(message);
    }

    public EmailerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailerException(Throwable cause) {
        super(cause);
    }
}
