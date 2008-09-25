package com.blueskyminds.framework.patterns;

/**
 * Thrown during the initialisation by a PatternMatcher or its components
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PatternMatcherInitialisationException extends PatternMatcherException {

    public PatternMatcherInitialisationException() {
    }

    public PatternMatcherInitialisationException(String message) {
        super(message);
    }

    public PatternMatcherInitialisationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatternMatcherInitialisationException(Throwable cause) {
        super(cause);
    }
}
