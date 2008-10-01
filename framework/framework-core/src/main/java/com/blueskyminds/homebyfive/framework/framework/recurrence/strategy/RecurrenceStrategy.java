package com.blueskyminds.homebyfive.framework.framework.recurrence.strategy;

import com.blueskyminds.homebyfive.framework.framework.memento.XMLMemento;
import com.blueskyminds.homebyfive.framework.framework.memento.MementoOriginator;

import java.util.Date;

/**
 * Abstract implementation of a Recurrence Strategy
 * Standard interface to implement strategies that apply recurrence patterns.
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class RecurrenceStrategy implements MementoOriginator {

    private XMLMemento memento;

    // ------------------------------------------------------------------------------------------------------

    /** A recurrence strategy with a memento that contains its memento
     * @param memento       contains the memento for the RecurrenceStategy
     **/
    protected RecurrenceStrategy(XMLMemento memento) {
        this.memento = memento;
    }

    /** Default constructor for reflective instantiation by Caretaker */
    protected RecurrenceStrategy() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if this recurrence strategy results in an occurrence on date specified */
    public abstract boolean occursOn(Date date);

    /**
     * Returns the next occurrence for this strategy AFTER the given date
     **/
    public abstract Date nextOccurrence(Date date);

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public XMLMemento getMemento() {
        return memento;
    }

    public void setMemento(XMLMemento memento) {
        this.memento = memento;
    }
}
