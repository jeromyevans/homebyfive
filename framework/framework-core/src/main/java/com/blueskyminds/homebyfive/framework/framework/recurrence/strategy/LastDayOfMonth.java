package com.blueskyminds.homebyfive.framework.framework.recurrence.strategy;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A recurrence strategy for events that occur on the last day of the calendar month, on whichever day that falls.
 *
 * This strategy does not use a XMLMemento
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class LastDayOfMonth extends RecurrenceStrategy {

    // ------------------------------------------------------------------------------------------------------

    public LastDayOfMonth() {
        super(null);  // no memento
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if this recurrence strategy results in an occurrence on date specified */
    public boolean occursOn(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        // if the day of month equals the number of days in the month (the max value)
        return ((cal.getActualMaximum(Calendar.DAY_OF_MONTH)) == cal.get(Calendar.DAY_OF_MONTH));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the next occurrence for this strategy AFTER the given date
     **/
    public Date nextOccurrence(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        if (occursOn(date)) {
            // the date specified is the last day of the month, so roll over to the next month
            cal.add(Calendar.MONTH, 1);
        }

        // set the date to the last day of the month
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------
}
