package com.blueskyminds.homebyfive.framework.core.recurrence.strategy;

import com.blueskyminds.homebyfive.framework.core.datetime.DateTools;
import com.blueskyminds.homebyfive.framework.core.recurrence.strategy.memento.NthDayOfMonthMemento;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A recurrence strategy for events that occur on a specific day of the month
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthDayOfMonth extends RecurrenceStrategy {    

    // ------------------------------------------------------------------------------------------------------

    public NthDayOfMonth(int dayOfMonth) {
        super(new NthDayOfMonthMemento(dayOfMonth));
    }

    /** Default constructor for reflective instantiation by Caretaker (must be public) */
    public NthDayOfMonth() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this recurrence strategy results in an occurrence on date specified
     **/
    public boolean occursOn(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return getDayOfMonth().equals(cal.get(GregorianCalendar.DAY_OF_MONTH));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the next occurrence for this strategy AFTER the given date
     **/
    public Date nextOccurrence(Date date) {
        return DateTools.findNextDateWithSameDayOfMonth(date, getDayOfMonth());
    }

    // ------------------------------------------------------------------------------------------------------

    public Integer getDayOfMonth() {
        return ((NthDayOfMonthMemento) getMemento()).getDayOfMonth();
    }

    // ------------------------------------------------------------------------------------------------------
}
