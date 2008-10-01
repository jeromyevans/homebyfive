package com.blueskyminds.homebyfive.framework.core.recurrence.strategy.memento;

import com.blueskyminds.homebyfive.framework.core.datetime.Weekdays;
import com.blueskyminds.homebyfive.framework.core.datetime.WeekOfMonth;

/**
 * Settings for the NthDayOfWeekOfMonth recurrence strategy
 *
 * Date Started: 18/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthDayOfWeekOfMonthMemento extends RecurrenceMemento {

    /** The weekday of the event */
    private Weekdays weekday;

    /** The week of the month for the event */
    private WeekOfMonth weekOfMonth;

    public NthDayOfWeekOfMonthMemento(Weekdays weekday, WeekOfMonth weekOfMonth) {
        this.weekday = weekday;
        this.weekOfMonth = weekOfMonth;
    }

    protected NthDayOfWeekOfMonthMemento() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the NthDayOfWeekOfMonthMemento with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    public Weekdays getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekdays weekday) {
        this.weekday = weekday;
    }

    public WeekOfMonth getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(WeekOfMonth weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }
}
