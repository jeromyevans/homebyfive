package com.blueskyminds.homebyfive.framework.framework.persistence.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

/**
 * Create a standalone EntityManagerFactory
 *
 * Date Started: 23/10/2007
 * <p/>
 * History:
 */
public class JpaHelper {

    /**
     * Creates a new EntityManagerFactory instance for the specified persistence unit
     *
     * @param persistenceUnitName
     * @param persistenceUnitProperties     override the default properties if provided
     * @return
     */
    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Properties persistenceUnitProperties) {
        EntityManagerFactory emf;
         if (persistenceUnitProperties == null) {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        } else {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName, persistenceUnitProperties);
        }
        return emf;
    }

    /**
     * Creates a new EntityManagerFactory instance for the specified persistence unit
     *
     * @param persistenceUnitName
     * @return
     */
    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, null);
    }
}
