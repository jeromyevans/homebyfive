package com.blueskyminds.homebyfive.framework.core.datetime;

import junit.framework.TestCase;

import java.util.Date;
import java.util.Calendar;

/**
 * Date Started: 16/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestInterval extends TestCase {


    public void testAdd() {
        MonthOfYear month = MonthOfYear.november(2008);

        Interval interval = Interval.threeMonths();

        Date result1 = DateTools.createDate(2009, Calendar.FEBRUARY, 1);
        Date result2 = DateTools.createDate(2009, Calendar.FEBRUARY, 28, 23, 59, 59);


        assertEquals(result1, interval.addToStart(month));
        assertEquals(result2, interval.addToEnd(month));

    }
}
