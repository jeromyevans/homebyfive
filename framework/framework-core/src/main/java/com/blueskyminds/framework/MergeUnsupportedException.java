package com.blueskyminds.framework;

/**
 * Thrown if the mergeWith operation is called on a domainObject that doesn't include an implmentation for the
 *  method.
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class MergeUnsupportedException extends RuntimeException {

    public MergeUnsupportedException() {
        super("The DomainObject merge operation is not supported by this DomainObject");
    }

    public MergeUnsupportedException(DomainObject thisObject, DomainObject otherObject) {
        super("The DomainObject merge operation is not supported into "+thisObject.getClass().getSimpleName()+" from "+ otherObject.getClass().getSimpleName());
    }

    public MergeUnsupportedException(String message) {
        super(message);
    }

    public MergeUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MergeUnsupportedException(Throwable cause) {
        super(cause);
    }
}
