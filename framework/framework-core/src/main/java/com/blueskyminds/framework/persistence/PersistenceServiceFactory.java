package com.blueskyminds.framework.persistence;

import com.blueskyminds.framework.tools.PropertiesContext;
import com.blueskyminds.framework.tools.ReflectionTools;
import com.blueskyminds.framework.tools.ReflectionException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Creates an instance of a PersistenceService
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PersistenceServiceFactory {

    private static final Log LOG = LogFactory.getLog(PersistenceServiceFactory.class);

    /** The property that names the persistence service implementation */
    public static final String PERSISTENCE_SERVICE = "persistence.service";

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PersistenceServiceFactory with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates an instance of a PersistenceService
     *
     * These properties are used:
     *      persistence.service    - name of the implementation
     *
     * @param propertiesContext
     * @return
     * @throws PersistenceServiceException
     */
    public static PersistenceService createPersistenceService(PropertiesContext propertiesContext) throws PersistenceServiceException {

        PersistenceService persistenceService;

        String gateway = propertiesContext.getProperty(PERSISTENCE_SERVICE);
        if (gateway != null) {
            LOG.info(PERSISTENCE_SERVICE+": "+gateway);
            
            persistenceService = (PersistenceService) newInstance(gateway);
            persistenceService.setProperties(propertiesContext.getProperties());
        } else {
           throw new PersistenceServiceException("Configuration is incomplete: '"+ PERSISTENCE_SERVICE +"' is not defined.");
        }

        return persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to create a new instance of the class specified by name.  This is used to load
     * services into the service locator.
     *
     * Calls the default zero parameter constructor of the class
     *
     * @param className
     * @return the instance, or null if it failed
     */
    private static Object newInstance(String className) throws PersistenceServiceException {
        Object instance;
        try {
            instance = ReflectionTools.createInstanceOf(className);
        } catch(ReflectionException e) {
            throw new PersistenceServiceException("Attempted create an instance of a class with 'null' className", e);
        }
        return instance;

    }
}
