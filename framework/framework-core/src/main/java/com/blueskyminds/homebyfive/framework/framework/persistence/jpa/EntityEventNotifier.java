package com.blueskyminds.homebyfive.framework.framework.persistence.jpa;

import com.blueskyminds.homebyfive.framework.framework.DomainObject;

import java.util.List;
import java.util.LinkedList;

/**
 * A class that can be used to listen to Entity events
 *
 * This service is a singleton as its accessed by the EntityEventCallback's
 *
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EntityEventNotifier {

    private static final EntityEventNotifier soleInstance = new EntityEventNotifier();
    private List<EntityEventListener> listeners;

    // ------------------------------------------------------------------------------------------------------

    public EntityEventNotifier() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the EntityEventNotifier with default attributes
     */
    private void init() {
        listeners = new LinkedList<EntityEventListener>();
    }

    // ------------------------------------------------------------------------------------------------------

    public static EntityEventNotifier getInstance() {
        return soleInstance;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Register a listener with the service */
    public EntityEventListener addListener(EntityEventListener listener) {
        listeners.add(listener);
        return listener;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Deregister a listener from the service
     * @return the removed listener if successful */
    public EntityEventListener removeListener(EntityEventListener listener) {
        if (listeners.remove(listener)) {
            return listener;
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Notify this service that an OnPostLoad event has occurred */
    protected void notifyOnPostLoadEvent(DomainObject entity) {
        for (EntityEventListener listener : listeners) {
            listener.onPostLoad(entity);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Notify this service that an OnPostPersist event has occurred */
    protected void notifyOnPostPersistEvent(DomainObject entity) {
        for (EntityEventListener listener : listeners) {
            listener.onPostPersist(entity);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Notify this service that an OnPostPersist event has occurred */
    protected void notifyOnPostRemoveEvent(DomainObject entity) {
        for (EntityEventListener listener : listeners) {
            listener.onPostRemove(entity);
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
