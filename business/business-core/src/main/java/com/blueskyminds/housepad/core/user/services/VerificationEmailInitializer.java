package com.blueskyminds.housepad.core.user.services;

import com.blueskyminds.landmine.core.events.EventRegistry;
import com.blueskyminds.landmine.core.events.LandmineEvents;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Initializes the VerificationEmailer by registering for the event
 *
 * Date Started: 4/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Singleton
public class VerificationEmailInitializer {

    @Inject
    public VerificationEmailInitializer(EventRegistry eventRegistry) {
        eventRegistry.registerEventHandler(LandmineEvents.NEW_ACCOUNT_REGISTERED, VerificationEmailer.class);
    }
}
