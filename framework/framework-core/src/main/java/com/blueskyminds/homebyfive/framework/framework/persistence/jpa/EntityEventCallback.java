package com.blueskyminds.homebyfive.framework.framework.persistence.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

import com.blueskyminds.homebyfive.framework.framework.DomainObject;

/**
 * Default implementation used to listen to events on the PersistentEntities
 * This class is instantiated directly by the JPA Provider calling the default constructor.
 * It accesses the EntityEventNotifier singleton
 * <p/>
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EntityEventCallback {

    private static final Log LOG = LogFactory.getLog(EntityEventCallback.class);

    @PostLoad
    public void onPostLoad(Object entity) {
        EntityEventNotifier.getInstance().notifyOnPostLoadEvent((DomainObject) entity);
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        EntityEventNotifier.getInstance().notifyOnPostPersistEvent((DomainObject) entity);
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        EntityEventNotifier.getInstance().notifyOnPostRemoveEvent((DomainObject) entity);
    }
}
