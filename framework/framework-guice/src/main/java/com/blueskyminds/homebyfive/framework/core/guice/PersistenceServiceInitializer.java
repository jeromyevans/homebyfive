package com.blueskyminds.homebyfive.framework.core.guice;

import com.google.inject.Inject;
import com.wideplay.warp.persist.PersistenceService;

/**
 *
 * Initialise warp-persist when constructed
 *
 * Date Started: 16/03/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PersistenceServiceInitializer {

    @Inject
    public PersistenceServiceInitializer(PersistenceService service) {
        service.start();
    }
}
