package com.blueskyminds.homebyfive.framework.core.datetime;

import java.util.Calendar;

/**
 * Days of the week
 *
 * Date Created: 07 May 2006
 *
 * ---[ Blue Sky Minds Pty Ltd ]-----------------------------------------------------------------------------
 */

public enum Weekdays {
   Sunday,
   Monday,
   Tuesday,
   Wednesday,
   Thursday,
   Friday,
   Saturday,
   Unknown;

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the enumation from a Calendar.DAY_OF_WEEK */
    public static Weekdays getInstance(int calendarDayOfWeek) {
        Weekdays weekday = Unknown;
        switch (calendarDayOfWeek) {
            case Calendar.SUNDAY:
                weekday = Sunday;
                break;
            case Calendar.MONDAY:
                weekday = Monday;
                break;
            case Calendar.TUESDAY:
                weekday = Tuesday;
                break;
            case Calendar.WEDNESDAY:
                weekday = Wednesday;
                break;
            case Calendar.THURSDAY:
                weekday = Thursday;
                break;
            case Calendar.FRIDAY:
                weekday = Friday;
                break;
            case Calendar.SATURDAY:
                weekday = Saturday;
                break;
        }

        return weekday;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the week day as an integer compatible with CALENDAR.DAY_OF_WEEK
     * @return integer value for the Day of the week [Calendar.SUNDAY...Calendar.SATURDAY]
     */
    public int asDayOfWeek() {
        int dayOfWeek = -1;
        switch (ordinal()) {
            case 0:
                dayOfWeek = Calendar.SUNDAY;
                break;
            case 1:
                dayOfWeek = Calendar.MONDAY;
                break;
            case 2:
                dayOfWeek = Calendar.TUESDAY;
                break;
            case 3:
                dayOfWeek = Calendar.WEDNESDAY;
                break;
            case 4:
                dayOfWeek = Calendar.THURSDAY;
                break;
            case 5:
                dayOfWeek = Calendar.FRIDAY;
                break;
            case 6:
                dayOfWeek = Calendar.SATURDAY;
                break;
        }

        return dayOfWeek;
    }

    // ------------------------------------------------------------------------------------------------------
}
