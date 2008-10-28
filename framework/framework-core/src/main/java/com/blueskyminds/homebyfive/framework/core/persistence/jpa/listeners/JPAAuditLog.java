package com.blueskyminds.homebyfive.framework.core.persistence.jpa.listeners;

import javax.persistence.PostPersist;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class JPAAuditLog {

    @PostPersist
    public void saveCopy(Object object) {
        
    }
}
