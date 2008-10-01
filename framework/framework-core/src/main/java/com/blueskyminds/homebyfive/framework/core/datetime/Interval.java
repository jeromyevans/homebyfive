package com.blueskyminds.homebyfive.framework.core.datetime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A timespan is a relative duration of time - it has duration, but no start and end time
 *
 * Interval is accurate to the nearest second.
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class Interval implements Serializable {

    /** The timespan represented in number of periodTypes */
    private int periods;

    /** The type of period for this time span */
    private PeriodTypes periodType;

    // ------------------------------------------------------------------------------------------------------

    /** 
     * Create a new timespan specifying the value and periodType for the value.
     * eg. 12 months
     *
     * @param periods
     * @param periodType
     */
    public Interval(int periods, PeriodTypes periodType) {
        this.periods = periods;
        this.periodType = periodType;
    }

    /** Default constructor for ORM */
    protected Interval() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Given the specified startDate, calculates the date of the specified period number in this
     * timespan.
     *
     * @param startDate
     * @return last date of the timepan, given the start date
     */
    @Transient
    public Date getDateOfPeriod(Date startDate, int period) {
        if ((period >= 0) && (period <= periods)) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(startDate);
            // adjust the calendar date by the number of periods
            cal = adjustCalendar(cal, period);

            return cal.getTime();
        }
        else {
            // period number out of bounds
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Adjust the date of a calendar by the specified number of periods in this timespan.
     * The number of periods may be positive or negative
     *
     * @param cal
     * @param periods
     * @return adjusted calendar
     */
    private Calendar adjustCalendar(Calendar cal, int periods) {
        switch (periodType) {
            case Day:
                cal.add(Calendar.DAY_OF_MONTH, periods);
                break;
            case Fortnight:
                cal.add(Calendar.DAY_OF_MONTH, periods*14);
                break;
            case Hour:
                cal.add(Calendar.HOUR, periods);
                break;
            case Minute:
                cal.add(Calendar.MINUTE, periods);
                break;
            case Month:
                cal.add(Calendar.MONTH, periods);
                break;
            case OnceOff:
                break;
            case Quarter:
                cal.add(Calendar.MONTH, periods*3);
                break;
            case Second:
                cal.add(Calendar.SECOND, periods);
                break;
            case Week:
                cal.add(Calendar.WEEK_OF_YEAR, periods);
                break;
            case Year:
                cal.add(Calendar.YEAR, periods);
                break;
        }
        return cal;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Given the specified startDate, calculates the date in this time span of the
     * start of the final period.
     *
     * eg. if the timespan is 3 months, and startDate is 1 Jan 06, then the result is
     * 1 Mar 06.
     *
     * @param startDate
     * @return last date of the timepan, given the start date
     */
    @Transient
    public Date getDateOfFinalPeriod(Date startDate) {
        return getDateOfPeriod(startDate, (periods-1 >= 0 ? periods - 1 : 0));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Given the specified startDate, calculates the datetime of the last second of the timespan
     *
     * eg. if the timespan is 3 months, and startDate is 1 Jan 06 00:00:00, then the result is
     * 31 Mar 06 23:59:59
     *
     * Please note: the reason for providing the last second of the period is because it would be
     * ambiguous to provide a method called 'lastDate' for timespans that commence at times other
     * than 00:00:00
     *
     * eg. 1 Jan 06 10:21:45  + 3 months includes all seconds up to and including 1 Apr 06 10:21:44
     *
     * @param startDate
     * @return DateTime of the last second of the timepan, given the start date
     */
    public Date lastSecond(Date startDate) {
        Date nextDate = getDateOfPeriod(startDate, periods);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(nextDate);
        // subtract one second
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Given the specified startDate, calculates the datetime of the next second one
     * timespan after the start date
     */
    public Date nextAfterLastSecond(Date startDate) {
        Date nextDate = getDateOfPeriod(startDate, periods);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(nextDate);
        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Given an end date (inclusive to the  second), calculate the datetime of the first second of the
     *  timespan
     *
     * eg. if the timespan is 3 months and endDate is 31 Dec 05 23:59:59, then the result is
     *  1 Oct 05 00:00:00
     *
     * @param endDate
     * @return DateTime of the first second of the timepan, given the end date
     */
    public Date firstSecond(Date endDate) {
        Date emulatedEndDate = getDateOfPeriod(endDate, 0);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(emulatedEndDate);
        // add a negative amount of time to the calendar, equal to -periods
        cal = adjustCalendar(cal, -periods);
        // subtract one second from the period (by adding)
        cal.add(Calendar.SECOND, 1);
        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if this timespan contains the specified datetime */
    public boolean contains(Date startDate, Date date) {
        Date endDate = lastSecond(startDate);

        // equal to or after the start date and equal to or before the end date
        return (((date.equals(startDate)) || (date.after(startDate))) &&
                ((date.equals(endDate)) || (date.before(endDate))));
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="PeriodType")
    public PeriodTypes getPeriodType() {
        return periodType;
    }

    protected void setPeriodType(PeriodTypes periodType) {
        this.periodType = periodType;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Periods")
    public int getPeriods() {
        return periods;
    }

    protected void setPeriods(int periods) {
        this.periods = periods;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return periods+" "+periodType;
    }

    private static final int APPROXIMATE_DAYS_IN_MONTH = 30;
    private static final int APPROXIMATE_WEEKS_IN_MONTH = 4;
    private static final int APPROXIMATE_FORTNIGHTS_IN_MONTH = 2;

    // ------------------------------------------------------------------------------------------------------

    /** Approximate estimate of the number of whole months in the interval */
    public int approximateMonths() {
        int months = 0;
        switch (periodType) {
            case Day:
                months = periods / APPROXIMATE_DAYS_IN_MONTH;  // approximate
                break;
            case Fortnight:
                months = periods / APPROXIMATE_FORTNIGHTS_IN_MONTH;  // approximate
                break;
            case Hour:
                months = periods / (APPROXIMATE_DAYS_IN_MONTH * 24);
                break;
            case Minute:
                months = periods / (APPROXIMATE_DAYS_IN_MONTH * 24 * 60);
                break;
            case Month:
                months = periods;
                break;
            case OnceOff:
                break;
            case Quarter:
                months = periods * 3;
                break;
            case Second:
                months = periods / (APPROXIMATE_DAYS_IN_MONTH * 24 * 60 * 60);
                break;
            case Week:
                months = periods / APPROXIMATE_WEEKS_IN_MONTH;
                break;
            case Year:
                months = periods * 12;
                break;
        }
        return months;
    }

}
