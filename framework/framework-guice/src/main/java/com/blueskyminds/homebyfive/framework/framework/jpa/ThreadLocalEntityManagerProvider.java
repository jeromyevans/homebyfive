package com.blueskyminds.homebyfive.framework.framework.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;

/**
 * Wraps an EntityManagerFactory to provide EntityManager instances
 *
 * Date Started: 23/10/2007
 * <p/>
 * History:
 */
public class ThreadLocalEntityManagerProvider implements Provider<EntityManager> {

    private EntityManagerFactory emf;

    private static ThreadLocal threadLocal = new ThreadLocal();

    /**
     * Get the current EntityManager from ThreadLocal, or create a new one if one doesn't exist for
     * this thread
     *
     * @return an open EntityManager
     */
    public EntityManager get() {
        EntityManager em = (EntityManager) threadLocal.get();
        if ((em != null) && (em.isOpen())) {
            return em;
        } else {
            em = emf.createEntityManager();            
            threadLocal.set(em);
            return em;
        }
    }

    @Inject
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
