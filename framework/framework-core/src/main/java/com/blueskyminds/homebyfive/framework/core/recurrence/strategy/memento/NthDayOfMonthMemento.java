package com.blueskyminds.homebyfive.framework.core.recurrence.strategy.memento;

/**
 * Contains the memento for the NthDayOfMonth recurrence strategy
 *
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthDayOfMonthMemento extends RecurrenceMemento {

    /** The day of the month for this activity */
    private Integer dayOfMonth;

    public NthDayOfMonthMemento(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the NthDayOfMonthMemento with default attributes
     */
    private void init() {
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    // ------------------------------------------------------------------------------------------------------
}
