package com.blueskyminds.landmine.core.startup;


import com.google.inject.Module;

/**
 * Configures the landmine services
 */
public class LandmineService {

    public static Module buildModule() {
        return new LandmineCoreModule();
    }
}
