package com.blueskyminds.landmine.core.events;

import java.lang.annotation.Annotation;

/**
 * Date Started: 30/04/2008
 */
public interface EventRegistry {

    void registerEventHandler(String eventName, Class<? extends EventHandler> eventHandler);

    /**
     *
     * Register an EventHandler for the specified event name
     *
     * Creates a Key for the EventHandler class and annotation and adds it to the registry.
     *
     * @param eventName
     * @param eventHandler
     */
    void registerEventHandler(String eventName, Class<? extends EventHandler> eventHandler, Class<? extends Annotation> annotation);

    void fire(String eventName, Object object);
}
