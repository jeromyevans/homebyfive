package com.blueskyminds.framework.persistence;

/**
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class AbstractPersistenceSession<T> implements PersistenceSession<T> {

    /** The implementation specific session instance wrapped by this implementation */
    protected T session;

    /** The current transaction */
    protected PersistenceTransaction transaction;

    /** This threadlocal integer is used to track how many attempts have been made to open a new session
      *   within an existing session on the same thread.  If the depth is not zero, then closing the session
      *   has no effect other than to reduce the depth.  This allows a session to be shared within the
      *  same thread while allowing strong demarkation of transaction and session boundaries.
      */
     private static final ThreadLocal<Integer> sessionStackDepth = new ThreadLocal<Integer>();


    // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of a session */
    public AbstractPersistenceSession(T session) {
        this.session = session;
        this.transaction = null;
        clearStack();
        incrementStack();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractPersistenceSession with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    protected void decrementStack() {
        sessionStackDepth.set(sessionStackDepth.get()-1); // decrement the depth
    }

    protected void incrementStack() {
        sessionStackDepth.set(sessionStackDepth.get()+1); // increment the depth
    }

    protected int getStackDepth() {
        return sessionStackDepth.get();
    }

    protected boolean stackEmpty() {
        return (sessionStackDepth.get() == 0);
    }

    protected void clearStack() {
        sessionStackDepth.set(0);
    }

    public void continueConversation() {
        incrementStack();
    }
}
