package com.blueskyminds.homebyfive.framework.core.jpa;

import com.google.inject.Provider;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

/**
 * Provides an instance of an EntityManagerFactory
 *
 * The instance is stored in ThreadLocal
 *
 * Date Started: 26/12/2007
 */
public class ThreadLocalEntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private static ThreadLocal threadLocal = new ThreadLocal();

    private String persistenceUnitName;
    private Map properties;
    
    /**
     * Get the EntityManagerFactory from ThreadLocal, or create a new one if one doesn't exist for
     * this thread
     *
     * @return an open EntityManager
     */
    public EntityManagerFactory get() {
        EntityManagerFactory emf = (EntityManagerFactory) threadLocal.get();
        if ((emf != null) && (emf.isOpen())) {
            return emf;
        } else {
            if (properties != null) {
                emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
            } else {
                emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            }
            threadLocal.set(emf);
            return emf;
        }
    }

    @Inject
    public void setPersistenceUnit(@Named("jpa.persistenceUnit") String persistenceUnit) {
        this.persistenceUnitName = persistenceUnit;
    }

    @Inject(optional = true)
    public void setPersistenceUnit(@PersistenceUnitProperties Map properties) {
        this.properties = properties;
    }
}