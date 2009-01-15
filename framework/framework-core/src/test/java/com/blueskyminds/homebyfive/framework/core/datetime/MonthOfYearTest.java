package com.blueskyminds.homebyfive.framework.core.datetime;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 16/11/2007
 * <p/>
 * History:
 */
public class MonthOfYearTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(MonthOfYearTest.class);

    public void testCompare() {
        MonthOfYear april = new MonthOfYear(Calendar.APRIL, 2007);
        MonthOfYear june = new MonthOfYear(Calendar.JUNE, 2007);
        MonthOfYear april2008 = new MonthOfYear(Calendar.APRIL, 2008);
        MonthOfYear june2008 = new MonthOfYear(Calendar.JUNE, 2008);
        MonthOfYear april2006 = new MonthOfYear(Calendar.APRIL, 2006);
        MonthOfYear june2006 = new MonthOfYear(Calendar.JUNE, 2006);
        MonthOfYear anotherApril = new MonthOfYear(Calendar.APRIL, 2007);

        assertTrue(april.compareTo(june) == -1);
        assertTrue(april.compareTo(april2008) == -1);
        assertTrue(april.compareTo(june2008) == -1);
        assertTrue(april.compareTo(april2006) == 1);
        assertTrue(april.compareTo(june2006) == 1);
        assertTrue(april.compareTo(anotherApril) == 0);
    }

    public void testAdd() {
        MonthOfYear april = new MonthOfYear(Calendar.APRIL, 2007);

        MonthOfYear july = april.add(3);
        MonthOfYear oct06 = april.add(-6);
        MonthOfYear april06 = april.add(-12);
        MonthOfYear april08 = april.add(12);
        MonthOfYear may09 = april.add(25);

        assertEquals(new MonthOfYear(Calendar.JULY, 2007), july);
        assertEquals(new MonthOfYear(Calendar.OCTOBER, 2006), oct06);
        assertEquals(new MonthOfYear(Calendar.APRIL, 2006), april06);
        assertEquals(new MonthOfYear(Calendar.APRIL, 2008), april08);
        assertEquals(new MonthOfYear(Calendar.MAY, 2009), may09);

        MonthOfYear nov08 = MonthOfYear.november(2008);
        MonthOfYear dec08 = nov08.add(1);
        MonthOfYear jan09 = dec08.add(1);

        assertEquals(new MonthOfYear(Calendar.JANUARY, 2009), jan09);

    }

    public void testLastMonth() {
        MonthOfYear lastMonth = MonthOfYear.lastMonth();
        MonthOfYear now = MonthOfYear.now();
        LOG.info(lastMonth);
        LOG.info(now);
    }
}
