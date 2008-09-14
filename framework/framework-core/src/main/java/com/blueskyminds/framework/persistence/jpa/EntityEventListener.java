package com.blueskyminds.framework.persistence.jpa;

import com.blueskyminds.framework.DomainObject;

/**
 * Interface to a Listener that can be notified by the EntityEventNotifier on JPA EntityListener events
 * <p/>
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface EntityEventListener {

    void onPostLoad(DomainObject entity);

    void onPostPersist(DomainObject entity);

    void onPostRemove(DomainObject entity);

}
