package com.blueskyminds.landmine.core.events;

/**
 * Date Started: 30/04/2008
 */
public interface EventHandler {

    public void fire(String eventName, Object payload);
}