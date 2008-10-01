package com.blueskyminds.entitymanager;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.persistence.*;
import java.util.*;

import com.blueskyminds.homebyfive.framework.core.tools.LoggerTools;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.EntityEventListener;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.EntityEventNotifier;
import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.datetime.Weekdays;
import com.blueskyminds.homebyfive.framework.core.datetime.WeekOfMonth;
import com.blueskyminds.homebyfive.framework.core.recurrence.MonthlyRecurrence;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;
import com.blueskyminds.homebyfive.framework.core.recurrence.constraint.NoEarlierThan;
import com.blueskyminds.homebyfive.framework.core.recurrence.constraint.Before;

/**
 * Tests setup of an EntityManager
 *
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2006 Blue Sky Minds Pty Ltd<br/>
 */
public class TestEntityManager {

    private static final Log LOG = LogFactory.getLog(TestEntityManager.class);
    private EntityManagerFactory emf;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestEntityManager with default attributes
     */
    private void init() {
    }

    public void start() {
        emf = Persistence.createEntityManagerFactory("DefaultEntityManager");

        testRecurrencePersistence();
    }

    public class EntityListener implements EntityEventListener {

        public void onPostLoad(DomainObject entity) {
            LOG.info("loaded "+entity);
        }

        public void onPostPersist(DomainObject entity) {
            LOG.info("persisted "+entity);
        }

        public void onPostRemove(DomainObject entity) {
            LOG.info("removed "+entity);
        }
    }
     public void testRecurrencePersistence() {

         EntityManager em = emf.createEntityManager();
         EntityEventNotifier.getInstance().addListener(new EntityListener());
         
         MonthlyRecurrence firstOfMonth = new MonthlyRecurrence(1);
         MonthlyRecurrence secTuesOfMonth = new MonthlyRecurrence(Weekdays.Tuesday, WeekOfMonth.Second);
         MonthlyRecurrence lastFriOfMonth = new MonthlyRecurrence(Weekdays.Friday, WeekOfMonth.Last);
         MonthlyRecurrence lastDayOfMonth = new MonthlyRecurrence(true);
         MonthlyRecurrence fourthOfMonth = new MonthlyRecurrence(4);
         Date startInc = new GregorianCalendar(2006, Calendar.JUNE, 1).getTime();
         Date endEx= new GregorianCalendar(2006, Calendar.AUGUST, 1).getTime();

         fourthOfMonth.addConstraint(new NoEarlierThan(startInc, fourthOfMonth));
         fourthOfMonth.addConstraint(new Before(endEx, fourthOfMonth));

         EntityTransaction et = em.getTransaction();
         et.begin();
         em.setFlushMode(FlushModeType.COMMIT);
         em.persist(firstOfMonth);
         em.persist(secTuesOfMonth);
         em.persist(lastFriOfMonth);
         em.persist(lastDayOfMonth);
         em.persist(fourthOfMonth);
         et.commit();
         
         TestTools.printAll(em, Recurrence.class);

         emf.close();
     }



    // ------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        LoggerTools.configure();
        TestEntityManager test = new TestEntityManager();
        test.start();
    }
}
