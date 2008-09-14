package com.blueskyminds.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.Date;

import com.blueskyminds.framework.tools.ResourceLocator;

/**
 * Test program for Hibernate 3.2 with annotations
 *
 * Date Started: 25/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestHibernate {

    /** Singleton instance of the sessionFactory */
    private static SessionFactory sessionFactory;

    // ------------------------------------------------------------------------------------------------------

    private static void initialise () {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new AnnotationConfiguration().configure(ResourceLocator.locateResource("hibernate/hibernate.hsql.cfg.xml")).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a reference to the session factory
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initialise();
        }
        return sessionFactory;
    }

    // ------------------------------------------------------------------------------------------------------

    private void createAndStoreEvent(String title, Date theDate) {

        Session session = getSessionFactory().getCurrentSession();

        session.beginTransaction();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);

        session.save(theEvent);

        session.getTransaction().commit();
    }

    // ------------------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        TestHibernate testHibernate = new TestHibernate();
        testHibernate.createAndStoreEvent("Test Event", new Date());

        getSessionFactory().close();
    }
}
