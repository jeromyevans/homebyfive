package com.blueskyminds.hibernate;

import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceTransaction;
import com.blueskyminds.framework.tasks.*;
import com.blueskyminds.framework.datetime.Weekdays;
import com.blueskyminds.framework.datetime.WeekOfMonth;
import com.blueskyminds.framework.recurrence.MonthlyRecurrence;
import com.blueskyminds.framework.recurrence.Recurrence;
import com.blueskyminds.framework.recurrence.constraint.NoEarlierThan;
import com.blueskyminds.framework.recurrence.constraint.Before;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.test.DbTestCase;
import com.blueskyminds.framework.tools.RandomTools;

import java.util.*;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestPersistenceLayerGateway extends DbTestCase {

    private static final Log LOG = LogFactory.getLog(TestPersistenceLayerGateway.class);

    public TestPersistenceLayerGateway(String string) {
        super(string);
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
            Date endEx= new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime();

            fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
            fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

            session = gateway.openSession();
            PersistenceTransaction transaction = session.beginTransaction();
            gateway.save(firstOfMonth);
            gateway.save(secTuesOfMonth);
            gateway.save(lastFriOfMonth);
            gateway.save(lastDayOfMonth);
            gateway.save(fourthOfMonth);
            transaction.commit();
            session.close();
            TestTools.printAll(Recurrence.class, gateway);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
     }

    public void testCount() {
        PersistenceSession session;

        // setup some data in the database
        testRecurrencePersistence();

        // count the entries
        try {
            PersistenceService gateway = getPersistenceService();

            long count = gateway.count(Recurrence.class);

            assertEquals(5, count);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    public void testRandom() {
        PersistenceSession session;

        // setup some data in the database
        testRecurrencePersistence();

        // count the entries
        try {
            PersistenceService gateway = getPersistenceService();

            Recurrence recurrence1 = gateway.random(Recurrence.class);
            Recurrence recurrence2 = gateway.random(Recurrence.class);
            Recurrence recurrence3 = gateway.random(Recurrence.class);
            Recurrence recurrence4 = gateway.random(Recurrence.class);

            assertNotNull(recurrence1);
            assertNotNull(recurrence2);
            assertNotNull(recurrence3);
            assertNotNull(recurrence4);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    private class TestTask extends SimpleTask {

        public TestTask(String name) {
            super(name);
        }

        public boolean process() {
            PersistenceService gateway = getPersistenceService();

            PersistenceSession session = null;
            try {
                session = gateway.openSession();

                MonthlyRecurrence testRecurrence = new MonthlyRecurrence(RandomTools.randomInt(1, 28));
                gateway.save(testRecurrence);
            } catch (PersistenceServiceException e) {
                e.printStackTrace();
            } finally {
                if (session != null) {
                    try {
                        session.close();
                    } catch(PersistenceServiceException e) {
                        //
                    }
                }
            }
            return true;
        }
    }

    /** Test multiple threads using the gateway concurrently */
    public void testConcurrentAccess() {
        int count = 200;
        TaskGroup all = new TaskGroup("all");
        for (int i = 0; i < count; i++) {
            all.addTask(new TestTask("Task #"+i).asTask());
        }

        PersistenceService gateway = getPersistenceService();
        PersistenceSession session = null;
        try {
            session = gateway.openSession();
            TaskCoordinator.runInBackground(all);
            LOG.info("Waiting 10 seconds...");
            try {
                Thread.sleep(10*1000);
            } catch(InterruptedException e) {
                //
            }
            LOG.info("Finished...");
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch(PersistenceServiceException e) {

                }
            }
        }

        try {
            session = gateway.openSession();
            List<MonthlyRecurrence> results = gateway.findAll(MonthlyRecurrence.class);
            assertNotNull(results);
            assertEquals(count, results.size());
            List<Long> ids = new LinkedList<Long>();
            for (MonthlyRecurrence recurrence : results) {
                recurrence.print();
                if (!ids.contains(recurrence.getId())) {
                    ids.add(recurrence.getId());
                } else {
                    fail("Duplicate identity encountered");
                }
            }
        } catch (PersistenceServiceException e) {

        }
    }
}
