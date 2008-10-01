package com.blueskyminds.homebyfive.framework.framework.recurrence;

import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.NthDayOfMonth;
import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.NthDayOfWeekOfMonth;
import com.blueskyminds.homebyfive.framework.framework.recurrence.strategy.LastDayOfMonth;
import com.blueskyminds.homebyfive.framework.framework.datetime.WeekOfMonth;
import com.blueskyminds.homebyfive.framework.framework.datetime.Weekdays;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * A recurrence occuring at a specified time of the month
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("Monthly")
public class MonthlyRecurrence extends Recurrence {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a one-day-a-month recurrence on the specified day of the month
     *
     * eg. 1st of the month
     *
     * @param dayOfMonth
     */
    public MonthlyRecurrence(int dayOfMonth) {
        super(new NthDayOfMonth(dayOfMonth));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a one-day-a-month recurrence on the specified day of the week in the specified week of month.
     *
     * eg. second tuesday of the month, last friday of the month
     *
     * @param weekday
     * @param weekOfMonth
     */
    public MonthlyRecurrence(Weekdays weekday, WeekOfMonth weekOfMonth) {
        super(new NthDayOfWeekOfMonth(weekday, weekOfMonth));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a one-day-a-month recurrence that occurs on the last day of the month, whereever that falls
     *
     * eg. the 28th in February, the 31st in March
     *
     * @param endOfMonth if true, falls on the end of the month, if false, is the same as a simple
     *  monthly recurrence where the day of the month is not specified
     */
    public MonthlyRecurrence(boolean endOfMonth) {
        super(new LastDayOfMonth());
    }

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected MonthlyRecurrence() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
