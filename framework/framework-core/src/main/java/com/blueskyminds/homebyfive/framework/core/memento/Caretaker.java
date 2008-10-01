package com.blueskyminds.homebyfive.framework.core.memento;

/**
 * Identifies an object that's a Caretaker for a Memento
 *
 * Date Started: 11/11/2007
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Caretaker {

    /**
     * Get the memento serialized to a string
     *
     **/
    String getSerializedMemento();

    /**
     * Get the memento instance
     **/
    Object getMemento();

    /**
     * Get the MementoOriginator implementation
     **/   
    MementoOriginator getOriginator();
}
