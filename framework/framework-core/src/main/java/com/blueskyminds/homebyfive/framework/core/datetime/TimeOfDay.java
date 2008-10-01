package com.blueskyminds.homebyfive.framework.core.datetime;

import javax.persistence.Embeddable;
import javax.persistence.Basic;
import javax.persistence.Column;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Represents a relative time of day, independent of the date, timezone or Locale
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Embeddable
public class TimeOfDay {

    /** The number of seconds since 0:00 */
    long secondOfDay;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a time of day extracted from the specified Date
     * @param date
     */
    public TimeOfDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        calculateSecondOfDay(hour, min, sec);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the time of day as the number of seconds since 0:00.
     * 24 hour time format:
     *   HH:mm:ss */
    public TimeOfDay(String time)
            throws TimeOfDayFormatException {

        String hr = "0";
        String min = "0";
        String sec = "0";
        int h, m, s;

        // split the string into its parts
        String[] parts = time.split(":");
        if (parts.length > 0) {
            hr = parts[0];
            if (parts.length > 1) {
                min = parts[1];
                if (parts.length > 2) {
                    sec = parts[2];
                }
            }
        }

        // attempt to convert to ints
        try {
            h = Integer.parseInt(hr);
            if ((h < 0) || (h > 23))  {
                throw new TimeOfDayFormatException("Hour is an invalid value");
            }
        }
        catch (NumberFormatException e) {
            throw new TimeOfDayFormatException("Hour is an invalid format");
        }
        try {
            m = Integer.parseInt(min);
            if ((m < 0) || (m > 59)) {
                throw new TimeOfDayFormatException("Minute is an invalid value");
            }
        }
        catch (NumberFormatException e) {
            throw new TimeOfDayFormatException("Minute is an invalid format");
        }
        try {
            s = Integer.parseInt(sec);
            if ((s < 0) || (s > 59)) {
                throw new TimeOfDayFormatException("Second is an invalid value");
            }
        }
        catch (NumberFormatException e) {
            throw new TimeOfDayFormatException("Second is an invalid format");
        }

        // now scale
        calculateSecondOfDay(h, m, s);
    }

    /** Default constructor for ORM */
    protected TimeOfDay() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the number of seconds in the day
     * @param hours
     * @param mins
     * @param seconds
     */
    private void calculateSecondOfDay(int hours, int mins, int seconds) {
        secondOfDay = (hours * 3600) + (mins * 60) + seconds;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="SecondOfDay")
    protected long getSecondOfDay() {
        return secondOfDay;
    }

    protected void setSecondOfDay(long secondOfDay) {
        this.secondOfDay = secondOfDay;
    }
}
