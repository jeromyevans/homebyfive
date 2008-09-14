package com.blueskyminds.framework.patterns;

/**
 *
 * An exception thrown by the PatternMatcher or one of its components
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PatternMatcherException extends Exception {

    public PatternMatcherException() {
    }

    public PatternMatcherException(String message) {
        super(message);
    }

    public PatternMatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatternMatcherException(Throwable cause) {
        super(cause);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PatternMatcherException with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
