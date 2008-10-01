package com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.memento;

import java.util.Date;

/**
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class OnSpecificDateMemento extends RecurrenceMemento {

    private Date dateOfEvent;

    public OnSpecificDateMemento(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the OnSpecificDateMemento with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }
}
