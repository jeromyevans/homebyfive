package com.blueskyminds.homebyfive.framework.core.tasks.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Injector;
import com.google.inject.Inject;
import com.blueskyminds.homebyfive.framework.core.tasks.AsynchronousTask;

import java.util.Map;


/**
 * Uses Guice to create instances of AsynchronousTasks
 *
 * Date Started: 25/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GuiceAsynchronousTaskFactory extends AbstractAsynchronousTaskFactory {

    private static final Log LOG = LogFactory.getLog(GuiceAsynchronousTaskFactory.class);

    private Injector injector;

    @Inject
    public GuiceAsynchronousTaskFactory(Injector injector) {
        this.injector = injector;
    }

    public AsynchronousTask create(String key, Class<? extends AsynchronousTask> aClass, Map<String, Object> params) throws TaskingException {
        AsynchronousTask asynchronousTask = injector.getInstance(aClass);
        if (asynchronousTask != null) {
            asynchronousTask.setKey(key);
            populate(asynchronousTask, params);
        }
        return asynchronousTask;
    }

}



