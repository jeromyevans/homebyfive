package com.blueskyminds.homebyfive.framework.core.recurrence.strategy;

import com.blueskyminds.homebyfive.framework.core.datetime.Weekdays;
import com.blueskyminds.homebyfive.framework.core.datetime.WeekOfMonth;
import com.blueskyminds.homebyfive.framework.core.recurrence.strategy.memento.NthDayOfWeekOfMonthMemento;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A recurrence strategy for events that occur on a specific day of a specific week in the month.
 * Also used for a recurrence on a specific day in the LAST week in the month.
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NthDayOfWeekOfMonth extends RecurrenceStrategy {    

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a strategy that occurs on a specific weekday in a specific week of the month.
     * To select the last week of the month, use null for weekOfMonth
     * @param weekday
     * @param weekOfMonth
     */
    public NthDayOfWeekOfMonth(Weekdays weekday, WeekOfMonth weekOfMonth) {
        super(new NthDayOfWeekOfMonthMemento(weekday, weekOfMonth));
    }

    /** Default constructor for reflective instantiation by Caretaker (must be public) */
    public NthDayOfWeekOfMonth() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if the specified date falls in the last week of the month
     *
     * @param cal
     * @return true if the date falls in the last week of the month
     */
    private boolean isLastWeekOfMonth(Calendar cal) {
        // it is the last week of the month if the number of days in the month minus the day of the month is less than one week [0..6]
        return ((cal.getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)) < cal.getActualMaximum(Calendar.DAY_OF_WEEK));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if this recurrence strategy results in an occurrence on date specified */
    public boolean occursOn(Date date) {
        boolean occurs = false;
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        WeekOfMonth wom;

        Weekdays dow = Weekdays.getInstance(cal.get(Calendar.DAY_OF_WEEK));
        if (getWeekday().equals(dow)) {
            // if checking for the last week of the month...
            if (getWeekOfMonth().equals(WeekOfMonth.Last)) {
                // special check for the special case 'Last'
                if (isLastWeekOfMonth(cal)) {
                    occurs = true;
                }
            }
            else {
                // compare the week of the month to the enumeration
                wom = WeekOfMonth.getInstance(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
                if (getWeekOfMonth().equals(wom)) {
                    occurs = true;
                }
            }
        }


        return occurs;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the next occurrence for this strategy AFTER the given date
     **/
    public Date nextOccurrence(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        WeekOfMonth wom;

        if (occursOn(date)) {
            // need to roll over to the next month
            cal.add(Calendar.MONTH, 1);
        }

        if (getWeekOfMonth().equals(WeekOfMonth.Last)) {
            cal.set(Calendar.DAY_OF_WEEK, getWeekday().asDayOfWeek());
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, cal.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH));
         }
         else {
            cal.set(Calendar.DAY_OF_WEEK, getWeekday().asDayOfWeek());
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, getWeekOfMonth().asDayOfWeekOfMonth());
        }



        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------

    public Weekdays getWeekday() {
        return ((NthDayOfWeekOfMonthMemento) getMemento()).getWeekday();
    }

    // ------------------------------------------------------------------------------------------------------

    public WeekOfMonth getWeekOfMonth() {
        return ((NthDayOfWeekOfMonthMemento) getMemento()).getWeekOfMonth();
    }

    // ------------------------------------------------------------------------------------------------------
}
