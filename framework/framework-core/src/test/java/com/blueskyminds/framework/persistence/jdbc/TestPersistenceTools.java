package com.blueskyminds.framework.persistence.jdbc;

import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.test.DbTestCase;
import com.blueskyminds.framework.persistence.*;
import com.blueskyminds.framework.datetime.Weekdays;
import com.blueskyminds.framework.datetime.WeekOfMonth;
import com.blueskyminds.framework.recurrence.MonthlyRecurrence;
import com.blueskyminds.framework.recurrence.Recurrence;
import com.blueskyminds.framework.recurrence.constraint.NoEarlierThan;
import com.blueskyminds.framework.recurrence.constraint.Before;
import com.blueskyminds.framework.tools.DebugTools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestPersistenceTools extends DbTestCase {

    private static final Log LOG = LogFactory.getLog(TestPersistenceTools.class);

    public TestPersistenceTools(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestPersistenceTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public void persistRecurrenceData() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            MonthlyRecurrence firstOfMonth = new MonthlyRecurrence(1);
            MonthlyRecurrence secTuesOfMonth = new MonthlyRecurrence(Weekdays.Tuesday, WeekOfMonth.Second);
            MonthlyRecurrence lastFriOfMonth = new MonthlyRecurrence(Weekdays.Friday, WeekOfMonth.Last);
            MonthlyRecurrence lastDayOfMonth = new MonthlyRecurrence(true);
            MonthlyRecurrence fourthOfMonth = new MonthlyRecurrence(4);
            Date startInc = new GregorianCalendar(2006, Calendar.JUNE, 1).getTime();
            Date endEx= new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime();

            fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
            fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

            gateway.save(firstOfMonth);
            gateway.save(secTuesOfMonth);
            gateway.save(lastFriOfMonth);
            gateway.save(lastDayOfMonth);
            gateway.save(fourthOfMonth);

            TestTools.printAll(Recurrence.class, gateway);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void testDatabaseMetaData() throws Exception {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");

        // force setup of the empty schema
        PersistenceService persistenceService = getPersistenceService();
        persistenceService.openSession();
        persistRecurrenceData();
        persistenceService.closeSession();

        TestTools.printAll(Recurrence.class, persistenceService);

        long recurrenceItems = persistenceService.count(Recurrence.class);

        DebugTools.printArray(PersistenceTools.getTableNames(c));

        String[] columns = PersistenceTools.getColumnNames(c, "RECURRENCE");
        for (String name : columns) {
            LOG.info(name);
        }


        String[] sql = PersistenceTools.unload(c, "RECURRENCE");
        DebugTools.printArray(sql, false);

        // clear the table

        PersistenceTools.executeUpdate(c, "delete from RecStratNthDayOfMonth");
        PersistenceTools.executeUpdate(c, "delete from RecStratNthDayOfWeekOfMonth");
        PersistenceTools.executeUpdate(c, "delete from RECSTRATLASTDAYOFMONTH");
        PersistenceTools.executeUpdate(c, "delete from RecurrenceStrategy");        
        PersistenceTools.executeUpdate(c, "delete from RecurrenceConstraint");
        PersistenceTools.executeUpdate(c, "delete from RECURRENCE");

        // reload the table
        int rowsInserted = PersistenceTools.executeUpdate(c, sql);

        TestTools.printAll(Recurrence.class, persistenceService);

        assertEquals(recurrenceItems, rowsInserted);

    }
}
