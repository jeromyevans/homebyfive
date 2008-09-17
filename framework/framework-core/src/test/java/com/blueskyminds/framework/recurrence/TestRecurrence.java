package com.blueskyminds.framework.recurrence;

import com.blueskyminds.framework.datetime.WeekOfMonth;
import com.blueskyminds.framework.datetime.Weekdays;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.recurrence.constraint.NoEarlierThan;
import com.blueskyminds.framework.recurrence.constraint.Before;
import com.blueskyminds.framework.recurrence.constraint.After;
import com.blueskyminds.framework.recurrence.constraint.NoLaterThan;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.test.DbTestCase;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * Methods for testing the recurrence algorithms
 * <p/>
 * Date Started: 7/05/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestRecurrence extends DbTestCase {

    public TestRecurrence(String s) {
        super(s);
    }

    public void testMonthlyRecurrence() {

        MonthlyRecurrence firstOfMonth = new MonthlyRecurrence(1);
        MonthlyRecurrence secTuesOfMonth = new MonthlyRecurrence(Weekdays.Tuesday, WeekOfMonth.Second);
        MonthlyRecurrence lastFriOfMonth = new MonthlyRecurrence(Weekdays.Friday, WeekOfMonth.Last);
        MonthlyRecurrence lastDayOfMonth = new MonthlyRecurrence(true);


        Date dateA = new GregorianCalendar(2006, Calendar.MAY, 7).getTime();  // Sunday 7th May 06
        Date dateB = new GregorianCalendar(2006, Calendar.MAY, 1).getTime();  // Monday 1st May 06
        Date dateC1 = new GregorianCalendar(2006, Calendar.MAY, 2).getTime();  // Tuesday 2th May 06
        Date dateC2 = new GregorianCalendar(2006, Calendar.MAY, 9).getTime();  // Tuesday 9th May 06
        Date dateC3 = new GregorianCalendar(2006, Calendar.MAY, 16).getTime();  // Tuesday 16th May 06
        Date dateD = new GregorianCalendar(2006, Calendar.APRIL, 30).getTime();  // Tuesday 30 Apr 06
        Date dateE = new GregorianCalendar(2006, Calendar.MAY, 31).getTime();  // Wednesday 31 May 06
        Date dateF = new GregorianCalendar(2006, Calendar.MAY, 26).getTime();  // Friday 26 May 06 (last Friday of the month)

        //
        assertFalse(firstOfMonth.isDueOn(dateA));
        assertTrue(firstOfMonth.isDueOn(dateB));
        assertFalse(firstOfMonth.isDueOn(dateC1));

        assertFalse(secTuesOfMonth.isDueOn(dateA));
        assertFalse(secTuesOfMonth.isDueOn(dateB));
        assertFalse(secTuesOfMonth.isDueOn(dateC1));
        assertTrue(secTuesOfMonth.isDueOn(dateC2));
        assertFalse(secTuesOfMonth.isDueOn(dateC3));
        assertFalse(secTuesOfMonth.isDueOn(dateD));
        assertFalse(secTuesOfMonth.isDueOn(dateE));
        assertFalse(secTuesOfMonth.isDueOn(dateF));

        assertFalse(lastFriOfMonth.isDueOn(dateA));
        assertFalse(lastFriOfMonth.isDueOn(dateB));
        assertFalse(lastFriOfMonth.isDueOn(dateC1));
        assertFalse(lastFriOfMonth.isDueOn(dateC2));
        assertFalse(lastFriOfMonth.isDueOn(dateC3));
        assertFalse(lastFriOfMonth.isDueOn(dateD));
        assertFalse(lastFriOfMonth.isDueOn(dateE));
        assertTrue(lastFriOfMonth.isDueOn(dateF));

        assertFalse(lastDayOfMonth.isDueOn(dateA));
        assertFalse(lastDayOfMonth.isDueOn(dateB));
        assertFalse(lastDayOfMonth.isDueOn(dateC1));
        assertFalse(lastDayOfMonth.isDueOn(dateC2));
        assertFalse(lastDayOfMonth.isDueOn(dateC3));
        assertTrue(lastDayOfMonth.isDueOn(dateD));
        assertTrue(lastDayOfMonth.isDueOn(dateE));
        assertFalse(lastDayOfMonth.isDueOn(dateF));
    }

    public void testConstraints() {

        MonthlyRecurrence fourthOfMonth = new MonthlyRecurrence(4);
        Date startInc = new GregorianCalendar(2006, Calendar.JUNE, 1).getTime();
        Date endEx = new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime();

        fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
        fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

        fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
        fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

        Date dateA = new GregorianCalendar(2006, Calendar.MAY, 4).getTime();
        Date dateB = new GregorianCalendar(2006, Calendar.JUNE, 4).getTime();
        Date dateC = new GregorianCalendar(2006, Calendar.JULY, 4).getTime();
        Date dateD = new GregorianCalendar(2006, Calendar.JULY, 5).getTime();
        Date dateE = new GregorianCalendar(2006, Calendar.AUGUST, 4).getTime();

        assertFalse(fourthOfMonth.isDueOn(dateA));
        assertTrue(fourthOfMonth.isDueOn(dateB));
        assertTrue(fourthOfMonth.isDueOn(dateC));
        assertFalse(fourthOfMonth.isDueOn(dateD));
        assertFalse(fourthOfMonth.isDueOn(dateE));

        MonthlyRecurrence secWedOfMonth = new MonthlyRecurrence(Weekdays.Wednesday, WeekOfMonth.Second);

        Date startEx = new GregorianCalendar(2006, Calendar.MAY, 31).getTime();
        Date endInc = new GregorianCalendar(2006, Calendar.JULY, 31).getTime();

        secWedOfMonth.addConstraint(new After(startEx, secWedOfMonth));
        secWedOfMonth.addConstraint(new NoLaterThan(endInc, secWedOfMonth));

        Date dateF = new GregorianCalendar(2006, Calendar.MAY, 10).getTime();  // wed 10may06 (2nd wed)
        Date dateG = new GregorianCalendar(2006, Calendar.JUNE, 14).getTime();  // wed 14jun06 (2nd wed)
        Date dateH = new GregorianCalendar(2006, Calendar.JUNE, 21).getTime();  // wed 21jun06 (3rd wed)
        Date dateI = new GregorianCalendar(2006, Calendar.JULY, 12).getTime();  // wed 12jul06 (2nd wed)
        Date dateJ = new GregorianCalendar(2006, Calendar.AUGUST, 9).getTime(); // wed 9 aug 05 (2nd wed)

        assertFalse(secWedOfMonth.isDueOn(dateF));
        assertTrue(secWedOfMonth.isDueOn(dateG));
        assertFalse(secWedOfMonth.isDueOn(dateH));
        assertTrue(secWedOfMonth.isDueOn(dateI));
        assertFalse(secWedOfMonth.isDueOn(dateJ));

    }

    public void testCalendar() {

        // determine what happens when selecting an invalid date
        Calendar cal = new GregorianCalendar(2006, 2, 31);

        assertEquals(2006, cal.get(Calendar.YEAR), 2006);
        assertEquals(2, cal.get(Calendar.MONTH), 2);
        assertEquals(28, cal.getActualMaximum(Calendar.DAY_OF_MONTH), 28);
        assertEquals(31, cal.get(Calendar.DAY_OF_MONTH), 31);

    }

    public void testNextOccurence() {

        MonthlyRecurrence firstOfMonth = new MonthlyRecurrence(1);
        MonthlyRecurrence secTuesOfMonth = new MonthlyRecurrence(Weekdays.Tuesday, WeekOfMonth.Second);
        MonthlyRecurrence lastFriOfMonth = new MonthlyRecurrence(Weekdays.Friday, WeekOfMonth.Last);
        MonthlyRecurrence lastDayOfMonth = new MonthlyRecurrence(true);

        Date dateA = new GregorianCalendar(2006, Calendar.MAY, 7).getTime();  // Sunday 7th May 06

        Date date1 = firstOfMonth.getNextOccurence(dateA);
        Date date2 = secTuesOfMonth.getNextOccurence(dateA);
        Date date3 = lastFriOfMonth.getNextOccurence(dateA);
        Date date4 = lastDayOfMonth.getNextOccurence(dateA);

        assertEquals(new GregorianCalendar(2006, Calendar.JUNE, 1).getTime(), date1);
        assertEquals(new GregorianCalendar(2006, Calendar.MAY, 9).getTime(), date2);
        assertEquals(new GregorianCalendar(2006, Calendar.MAY, 26).getTime(), date3);
        assertEquals(new GregorianCalendar(2006, Calendar.MAY, 31).getTime(), date4);

        // now try further into the same series

        Date date1A = firstOfMonth.getNextOccurence(date1);
        Date date2A = secTuesOfMonth.getNextOccurence(date2);
        Date date3A = lastFriOfMonth.getNextOccurence(date3);
        Date date4A = lastDayOfMonth.getNextOccurence(date4);
        assertEquals(new GregorianCalendar(2006, Calendar.JULY, 1).getTime(), date1A);
        assertEquals(new GregorianCalendar(2006, Calendar.JUNE, 13).getTime(), date2A);
        assertEquals(new GregorianCalendar(2006, Calendar.JUNE, 30).getTime(), date3A);
        assertEquals(new GregorianCalendar(2006, Calendar.JUNE, 30).getTime(), date4A);

        // same series again

        Date date1B = firstOfMonth.getNextOccurence(date1A);
        Date date2B = secTuesOfMonth.getNextOccurence(date2A);
        Date date3B = lastFriOfMonth.getNextOccurence(date3A);
        Date date4B = lastDayOfMonth.getNextOccurence(date4A);
        assertEquals(new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime(), date1B);
        assertEquals(new GregorianCalendar(2006, Calendar.JULY, 11).getTime(), date2B);
        assertEquals(new GregorianCalendar(2006, Calendar.JULY, 28).getTime(), date3B);
        assertEquals(new GregorianCalendar(2006, Calendar.JULY, 31).getTime(), date4B);

        // try some special cases
        Date dateB = new GregorianCalendar(2005, Calendar.DECEMBER, 12).getTime();
        MonthlyRecurrence monthly31st = new MonthlyRecurrence(31);
        MonthlyRecurrence monthly29th = new MonthlyRecurrence(29);

        Date date5 = monthly31st.getNextOccurence(dateB);
        Date date6 = monthly29th.getNextOccurence(dateB);
        assertEquals(new GregorianCalendar(2005, Calendar.DECEMBER, 31).getTime(), date5);
        assertEquals(new GregorianCalendar(2005, Calendar.DECEMBER, 29).getTime(), date6);

        Date date5A = monthly31st.getNextOccurence(date5);
        Date date6A = monthly29th.getNextOccurence(date6);
        assertEquals(new GregorianCalendar(2006, Calendar.JANUARY, 31).getTime(), date5A);
        assertEquals(new GregorianCalendar(2006, Calendar.JANUARY, 29).getTime(), date6A);

        // no occurrence in FEB

        Date date5B = monthly31st.getNextOccurence(date5A);
        Date date6B = monthly29th.getNextOccurence(date6A);
        assertEquals(new GregorianCalendar(2006, Calendar.MARCH, 31).getTime(), date5B);
        assertEquals(new GregorianCalendar(2006, Calendar.MARCH, 29).getTime(), date6B);

        Date date5C = monthly31st.getNextOccurence(date5B);
        Date date6C = monthly29th.getNextOccurence(date6B);
        // no occurrence in APR for 31st
        assertEquals(new GregorianCalendar(2006, Calendar.MAY, 31).getTime(), date5C);
        assertEquals(new GregorianCalendar(2006, Calendar.APRIL, 29).getTime(), date6C);

        // try a leap year
        Date dateC = new GregorianCalendar(2003, Calendar.DECEMBER, 12).getTime();

        Date date7 = monthly31st.getNextOccurence(dateC);
        Date date8 = monthly29th.getNextOccurence(dateC);
        assertEquals(new GregorianCalendar(2003, Calendar.DECEMBER, 31).getTime(), date7);
        assertEquals(new GregorianCalendar(2003, Calendar.DECEMBER, 29).getTime(), date8);

        Date date7A = monthly31st.getNextOccurence(date7);
        Date date8A = monthly29th.getNextOccurence(date8);
        assertEquals(new GregorianCalendar(2004, Calendar.JANUARY, 31).getTime(), date7A);
        assertEquals(new GregorianCalendar(2004, Calendar.JANUARY, 29).getTime(), date8A);

        Date date7B = monthly31st.getNextOccurence(date7A);
        Date date8B = monthly29th.getNextOccurence(date7A);
        assertEquals(new GregorianCalendar(2004, Calendar.MARCH, 31).getTime(), date7B);
        assertEquals(new GregorianCalendar(2004, Calendar.FEBRUARY, 29).getTime(), date8B);

        Date date7C = monthly31st.getNextOccurence(date7B);
        Date date8C = monthly29th.getNextOccurence(date8B);
        assertEquals(new GregorianCalendar(2004, Calendar.MAY, 31).getTime(), date7C);
        assertEquals(new GregorianCalendar(2004, Calendar.MARCH, 29).getTime(), date8C);


    }

    public void testRecurrencePersistence() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            MonthlyRecurrence firstOfMonth = new MonthlyRecurrence(1);
            MonthlyRecurrence secTuesOfMonth = new MonthlyRecurrence(Weekdays.Tuesday, WeekOfMonth.Second);
            MonthlyRecurrence lastFriOfMonth = new MonthlyRecurrence(Weekdays.Friday, WeekOfMonth.Last);
            MonthlyRecurrence lastDayOfMonth = new MonthlyRecurrence(true);
            MonthlyRecurrence fourthOfMonth = new MonthlyRecurrence(4);
            Date startInc = new GregorianCalendar(2006, Calendar.JUNE, 1).getTime();
            Date endEx = new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime();

            fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
            fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

            gateway.save(firstOfMonth);
            gateway.save(secTuesOfMonth);
            gateway.save(lastFriOfMonth);
            gateway.save(lastDayOfMonth);
            gateway.save(fourthOfMonth);

            TestTools.printAll(Recurrence.class, gateway);

        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testHourlyRecurrence() {
        Date midday = DateTools.createDate(2007, Calendar.MARCH, 31, 12, 0, 0);

        // every 2 hours and 15 minutes
        Recurrence recurrence = RecurrenceFactory.create(midday, 2, 15);

        Date date1 = recurrence.getNextOccurence(midday);
        Date date2 = recurrence.getNextOccurence(date1);
        Date date3 = recurrence.getNextOccurence(date2);
        Date date4 = recurrence.getNextOccurence(date3);
        Date date5 = recurrence.getNextOccurence(date4);

        assertEquals(DateTools.addToDate(midday, 2, 15, 0), date1);
        assertEquals(DateTools.addToDate(midday, 4, 30, 0), date2);
        assertEquals(DateTools.addToDate(midday, 6, 45, 0), date3);
        assertEquals(DateTools.addToDate(midday, 9, 0, 0), date4);
        assertEquals(DateTools.addToDate(midday, 11, 15, 0), date5);
    }

    public void testHourlyRecurrencePersistence() throws Exception {
        Date midday = DateTools.createDate(2007, Calendar.MARCH, 31, 12, 0, 0);

        // every 2 hours and 15 minutes
        Recurrence recurrence = RecurrenceFactory.create(midday, 2, 15);
        PersistenceService gateway = getPersistenceService();
        gateway.save(recurrence);

        TestTools.printAll(Recurrence.class, gateway);
    }

}
