package com.blueskyminds.homebyfive.framework.framework.tools;

/**
 * Encapsulates an exception that occurs during a RelectionTools operation
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class ReflectionException extends Exception {

    public ReflectionException() {
        super("A ReflectionTools error occured");
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
