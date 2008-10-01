package com.blueskyminds.homebyfive.framework.framework.recurrence.strategy;

import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.memento.OnSpecificDateMemento;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A recurrence strategy for a single recurrence on a specific date
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class OnSpecificDate extends RecurrenceStrategy {

    /**
     * Create a recurrence strategy for a recurrence on a specific date
     * @param dateOfEvent
     */
    public OnSpecificDate(Date dateOfEvent) {
        super(new OnSpecificDateMemento(dateOfEvent));
    }

    /** Default constructor for reflective instantiation by Caretaker (must be public) */
    public OnSpecificDate() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if this recurrence occurs on the specified date.
     * Matches against the Date, not the time.
     *
     * @param date
     * @return true if this recurrence occurs on the specified date
     */
    public boolean occursOn(Date date) {
        if (date != null) {
            Calendar cal1 = GregorianCalendar.getInstance();
            cal1.setTime(date);
            Calendar cal2 = GregorianCalendar.getInstance();
            cal2.setTime(getDateOfEvent());

            // same year, month and day of month
            return ((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) &&
                    (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) &&
                    (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)));
        } else {
            return false;
        }

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the next occurrence of this recurrence, which will be the date of this recurrence if the given
     * date is before the specified date
     *
     * @param date
     * @return the date of the occurrence, or null if the specified date is already after the occurrence
     */
    public Date nextOccurrence(Date date) {
        if (date.before(getDateOfEvent())) {
            return getDateOfEvent();
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public Date getDateOfEvent() {
        return ((OnSpecificDateMemento)getMemento()).getDateOfEvent();
    }

    // ------------------------------------------------------------------------------------------------------
}
