package com.blueskyminds.homebyfive.framework.core.events;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a quick-and-dirty implementation of an event regsitry
 *
 * Date Started: 30/04/2008
 */
public class EventRegistryImpl implements EventRegistry {

    private static final Log LOG = LogFactory.getLog(EventRegistryImpl.class);
    private Map<String, List<Key>> handlers;

    private Injector injector;

    public EventRegistryImpl() {
        handlers = new HashMap<String, List<Key>>();
    }

    /**
     *
     * Register an EventHandler for the specified event name
     *
     * Creates a Key for the EventHandler and adds it to the registry. t the
     *
     * @param eventName
     * @param eventHandler
     */
    public void registerEventHandler(String eventName, Class<? extends EventHandler> eventHandler) {
        synchronized (handlers) {
            Key key = Key.get(eventHandler);
            createEntry(eventName, key);
        }
    }

    /**
     *
     * Register an EventHandler for the specified event name
     *
     * Creates a Key for the EventHandler class and annotation and adds it to the registry.
     *
     * @param eventName
     * @param eventHandler
     */
    public void registerEventHandler(String eventName, Class<? extends EventHandler> eventHandler, Class<? extends Annotation> annotation) {
        synchronized (handlers) {
            Key key = Key.get(eventHandler, annotation);

            createEntry(eventName, key);
        }
    }

    /** Prepare an entry in event registry */
    private void createEntry(String eventName, Key key) {
        List<Key> eventHandlers = handlers.get(key);
        if (eventHandlers == null) {
            eventHandlers = new LinkedList<Key>();
            handlers.put(eventName, eventHandlers);
        }
        eventHandlers.add(key);

    }

    public void fire(String eventName, Object object) {
        int listenersFired = 0;
        synchronized (handlers) {
            List<Key> eventHandlers = handlers.get(eventName);
            if (eventHandlers != null) {
                for (Key key : eventHandlers) {
                    EventHandler eventHandler = (EventHandler) injector.getInstance(key);
                    if (eventHandler != null) {
                        eventHandler.fire(eventName, object);
                        listenersFired++;
                    }
                }
            } 
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Fired event '"+eventName+"' triggering "+listenersFired+" eventHandlers");
        }
    }

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
