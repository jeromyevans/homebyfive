package com.blueskyminds.homebyfive.framework.core.datetime;

/**
 * Week of the month, with a special case for the 'last' week
 *
 * Date Created: 07 May 2006
 *
 * ---[ Blue Sky Minds Pty Ltd ]-----------------------------------------------------------------------------
 */

public enum WeekOfMonth {
   First,
   Second,
   Third,
   Fouth,
   Fifth,
   Last;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an instance of the enumation from a Calendar.DAY_OF_WEEK_OF_MONTH.
     *
     * IMPORTANT: Last will never be returned by this method as the Fouth/Fifth weeks take precedence
     */
    public static WeekOfMonth getInstance(int calendarDayOfWeekOfMonth) {
        WeekOfMonth wom = null;

        switch (calendarDayOfWeekOfMonth) {
            case 1:
                wom = First;
                break;
            case 2:
                wom = Second;
                break;
            case 3:
                wom = Third;
                break;
            case 4:
                wom = Fouth;
                break;
            case 5:
                wom = Fifth;
                break;
        }

        return wom;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the week of month as an integer value in the range [1..5], compatible with the
     * Calendar.DAY_OF_WEEK_OF_MONTH value
     *
     * @return integer in the range [1..5]
     */
    public int asDayOfWeekOfMonth() {
        return ordinal()+1;
    }

    // ------------------------------------------------------------------------------------------------------
}
