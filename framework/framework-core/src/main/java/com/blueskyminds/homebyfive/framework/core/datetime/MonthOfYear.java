package com.blueskyminds.homebyfive.framework.core.datetime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Implementation of a MonthOfYear - meaning a specific month in a specific year, with no greater precision
 *
 * This is a value object
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Embeddable
public class MonthOfYear implements Serializable, Comparable {

    private int month;
    private int year;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new MonthOfYear timePeriod
     *
     * @param calendarMonth - from Calendar.MONTH (JANUARY = 0)
     * @param calendarYear  - from Calendar.YEAR (four digit)
     */
    public MonthOfYear(Integer calendarMonth, Integer calendarYear) {
        this.month = calendarMonth;
        this.year = calendarYear;
    }

    /** Default constructor for ORM */
    protected MonthOfYear() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the MonthOfYear for this MonthOfYear */
    @Basic
    @Column(name="MonthOfYear")
    public Integer getMonth() {
        return month;
    }

    protected void setMonth(Integer month) {
        this.month = month;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the Year for this MonthOfYear */
    @Basic
    @Column(name="Year")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the MonthOfYear as a Date.
     * The date equals the last second of the MonthOfYear
     *
     * @return the returned value is equal to the LAST second of the time period
     */
    public Date toDate() {
        return lastSecond();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the last second of this MonthOfYear as a Date */
    public Date lastSecond() {
        Calendar cal = new GregorianCalendar(year, month, 1, 0, 0, 0);
        cal.add(Calendar.MONTH, 1);   // 1st of next month
        cal.add(Calendar.SECOND, -1); // last second of this month

        return cal.getTime();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the first second of this MonthOfYear as a Date */
    public Date firstSecond() {
        Calendar cal = new GregorianCalendar(year, month, 1, 0, 0, 0); // 1st of this month

        return cal.getTime();
    }
    // ------------------------------------------------------------------------------------------------------

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MonthOfYear month1 = (MonthOfYear) o;

        if (month != month1.month) return false;
        if (year != month1.year) return false;

        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    public int hashCode() {
        int result;
        result = month;
        result = 29 * result + year;
        return result;
    }

    public String toString() {
        return year+"/"+month;
    }


    public int compareTo(Object o) {
        int result = -1;
        if (o instanceof MonthOfYear) {
            MonthOfYear other = (MonthOfYear) o;
            result = ((Integer) this.year).compareTo(other.year);
            if (result == 0) {
                result= ((Integer) this.month).compareTo(other.month);
            }
        }
        return result;
    }

    public MonthOfYear add(int months) {
        int yearsToAdd = months/12;
        int monthsToAdd = months % 12;
        int newYear = this.year + yearsToAdd;
        int newMonth = this.month + monthsToAdd;
        // rollover
        if (newMonth >= 11) {
            newMonth = newMonth - 12;
            newYear++;
        } else {
            if (newMonth < 0) {
                newMonth += 12;
                newYear--;
            }
        }
        return new MonthOfYear(newMonth, newYear);
    }

    public MonthOfYear add(Interval interval) {
         return add(interval.approximateMonths());
    }

     public MonthOfYear subtract(int months) {
         return add(-months);
    }

    public MonthOfYear subtract(Interval interval) {
         return subtract(interval.approximateMonths());
    }
      
}
