package com.blueskyminds.homebyfive.framework.core;

import com.blueskyminds.homebyfive.framework.core.datetime.DateTools;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.datetime.MonthOfYear;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.framework.core.test.BaseTestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Unit cases to test the DateTools package
 *
 * Date Started: 13/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestDateUtils extends BaseTestCase {

    public TestDateUtils(String string) {
        super(string);
    }

    public void testValidDates() {
        // remember month starts at zero
        assertTrue(DateTools.isDateValid(2006, 3, 30));
        assertFalse(DateTools.isDateValid(2006, 3, 31));
        assertFalse(DateTools.isDateValid(2006, 1, 29));
        assertTrue(DateTools.isDateValid(2004, 1, 29));
        assertFalse(DateTools.isDateValid(2006, 3, 50));
        assertFalse(DateTools.isDateValid(2006, 22, 30));
        assertTrue(DateTools.isDateValid(-1, 3, 30));

    }

    public void testFindNext() {
        Date date1 = new GregorianCalendar(2006, 0, 15).getTime();  // 15 Jan 06
        Date date2 = new GregorianCalendar(2006, 0, 31).getTime();  // 31 Jan 06
        Date date3 = new GregorianCalendar(2004, 0, 29).getTime();  // 29 Jan 04
        Date date4 = new GregorianCalendar(2006, 0, 29).getTime();  // 29 Jan 06

        Date dateA = DateTools.findNextDateWithSameDayOfMonth(date1);
        Date dateB = DateTools.findNextDateWithSameDayOfMonth(date2);
        Date dateC = DateTools.findNextDateWithSameDayOfMonth(date3);
        Date dateD = DateTools.findNextDateWithSameDayOfMonth(date4);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dateA);
        assertEquals(new GregorianCalendar(2006, 1, 15), cal);
        cal.setTime(dateB);
        assertEquals(new GregorianCalendar(2006, 2, 31), cal);
        cal.setTime(dateC);
        assertEquals(new GregorianCalendar(2004, 1, 29), cal);
        cal.setTime(dateD);
        assertEquals(new GregorianCalendar(2006, 2, 29), cal);
    }

    public void testTimespan() {
        // 3 month interval
        Interval interval = new Interval(3, PeriodTypes.Month);
        // 1 Jan 2006:
        Calendar cal1 = new GregorianCalendar(2006, Calendar.JANUARY, 1, 0, 0, 0);
        Date lastSecond = interval.lastSecond(cal1.getTime());
        // last second is: 31 Mar 06 23:59:59
        assertEquals(new GregorianCalendar(2006, Calendar.MARCH, 31, 23, 59, 59).getTime(), lastSecond);

        Calendar cal2 = new GregorianCalendar(2005, Calendar.DECEMBER, 31, 23, 59, 59);
        Date firstSecond = interval.firstSecond(cal2.getTime());
        // first second is: 1 Oct 05 00:00:00
        assertEquals(new GregorianCalendar(2005, Calendar.OCTOBER, 1, 0, 0, 0).getTime(), firstSecond);

        // check the reverse of the first case
        Date firstSecond2 = interval.firstSecond(lastSecond);
        assertEquals(cal1.getTime(), firstSecond2);
    }

    public void testTimePeriod() {
        MonthOfYear monthOfYear = new MonthOfYear(Calendar.DECEMBER, 2005);
        Date lastSecond = monthOfYear.lastSecond();
        Date firstSecond = monthOfYear.firstSecond();
        assertEquals(new GregorianCalendar(2005, Calendar.DECEMBER, 31, 23, 59, 59).getTime(), lastSecond);
        assertEquals(new GregorianCalendar(2005, Calendar.DECEMBER, 1, 0, 0, 0).getTime(), firstSecond);
    }
}
