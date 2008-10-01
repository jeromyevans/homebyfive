package com.blueskyminds.homebyfive.framework.framework.datetime;

/**
 * Occurs when a TimeOfDay has been specified with an invalid format
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TimeOfDayFormatException extends Exception {
    public TimeOfDayFormatException() {
        super("Invalid TimeOfDay format");
    }

    public TimeOfDayFormatException(String message) {
        super(message);
    }

    public TimeOfDayFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOfDayFormatException(Throwable cause) {
        super(cause);
    }
}
