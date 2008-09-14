package com.blueskyminds.framework.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Tools for manipulating dates and times
 *
 * Date Started: 13/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DateTools {

     // ------------------------------------------------------------------------------------------------------

    /**
     * A helper method for subclasses to determine if the specified date is valid
     *
     * @param year
     * @param monthInt [0..11]
     * @param dayOfMonth [1..31]
     */
    public static boolean isDateValid(int year, int monthInt, int dayOfMonth) {

        if ((monthInt >= 0) && (monthInt <= 12)) {
            GregorianCalendar cal = new GregorianCalendar(year, monthInt, 1);

            return ((dayOfMonth <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) && (dayOfMonth > 0));
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * This is the maxium number of times to iterate trying to find the next date - this is the number of
     * months to attempt, and has been selected to exceed 8 years to exceed the longest time no leap year
     * can occur in the Gregorian Calendar.
     */
    private static final int MAX_ATTEMPTS = 12*9;

    /**
     * Find the next date that has the same day of the month as the day of month specified, after the start
     * date
     *
     * eg.
     *  startDate = 15 Jan 06, returns 15 Feb 06
     *  startDate = 31 Jan 06, returns 31 Mar 06
     *  startDate = 29 Jan 04, returns 29 Feb 04
     *  startDate = 29 Jan 06, returns 29 Mar 06
     *
     * @param startDate
     * @return the date found
     */
    public static Date findNextDateWithSameDayOfMonth(Date startDate, int dayOfMonth) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(startDate);
        boolean found = false;
        int attempts = 0;
        int dom = cal.get(Calendar.DAY_OF_MONTH);
        Calendar currentCal;

        if ((dom < dayOfMonth) && (dayOfMonth <= cal.getActualMaximum(Calendar.DAY_OF_MONTH))) {
            // this is the simple case - the next date is in the same month
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            currentCal = cal;
            found = true;
        }
        else {
            // next month or later
            currentCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1,  cal.get(Calendar.HOUR_OF_DAY),  cal.get(Calendar.MINUTE),  cal.get(Calendar.SECOND));
            currentCal.add(Calendar.MONTH, 1);

            // currentCal equals the first of next month

            while ((!found) && (attempts < MAX_ATTEMPTS)) {
                if (dayOfMonth <= currentCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    // the date of month is valid for this month - use it
                    currentCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    found = true;
                }
                else {
                    // the day of month doesn't occur in this month, try the next month
                    currentCal.add(Calendar.MONTH, 1);
                }
            }
        }

        if (found) {
            return currentCal.getTime();
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Find the next date that has the same day of the month as the date specified.
     *
     * eg.
     *  startDate = 15 Jan 06, returns 15 Feb 06
     *  startDate = 31 Jan 06, returns 31 Mar 06
     *  startDate = 29 Jan 04, returns 29 Feb 04
     *  startDate = 29 Jan 06, returns 29 Mar 06
     *
     * @param startDate
     * @return the date found
     */
    public static Date findNextDateWithSameDayOfMonth(Date startDate) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(startDate);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        return findNextDateWithSameDayOfMonth(startDate, dayOfMonth);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add a time span to the specified date, plus one second
     * return the first second after the interval */
    public static Date addTimespan(Date date, Interval interval) {
        return interval.nextAfterLastSecond(date);
    }

    /**
     *
     * @param year
     * @param month  use Calendar.X (0 = January)
     * @param dayOfMonth
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static Date createDate(int year, int month, int dayOfMonth, int hour, int min, int sec) {
        return new GregorianCalendar(year, month, dayOfMonth, hour, min, sec).getTime();
    }

    /**
     *
     * @param year
     * @param month  use Calendar.X (0 = January)
     * @param dayOfMonth
     * @return
     */
    public static Date createDate(int year, int month, int dayOfMonth) {
        return new GregorianCalendar(year, month, dayOfMonth).getTime();
    }

     /**
     * Add the specified amount of time to a date
     *
     * accurate to the millisecond
      *
     * @return
     */
    public static Date addToDate(Date date, int hours, int minutes, int seconds) {
         long current = date.getTime();
         long period = toMilliseconds(hours, minutes, seconds);
         return new Date(current+period);
    }

    public static long toMilliseconds(int hours, int minutes, int seconds) {
        return toSeconds(hours, minutes, seconds) * 1000;
    }

    public static long toSeconds(int hours, int minutes, int seconds) {
        return (hours * 3600) + (minutes * 60) + seconds;
    }
}
