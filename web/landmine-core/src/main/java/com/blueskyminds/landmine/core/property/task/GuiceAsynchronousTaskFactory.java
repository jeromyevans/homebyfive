package com.blueskyminds.landmine.core.property.task;

import com.blueskyminds.framework.tasks.service.AsynchronousTaskFactory;
import com.blueskyminds.framework.tasks.service.TaskingException;
import com.blueskyminds.framework.tasks.AsynchronousTask;
import com.google.inject.Injector;
import com.google.inject.Inject;

import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Uses Guice to create instances of AsynchronousTasks
 *
 * Date Started: 25/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GuiceAsynchronousTaskFactory implements AsynchronousTaskFactory {

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

     public void populate(AsynchronousTask asynchronousTask, Map<String, Object> params) throws TaskingException {
        if (params != null) {
            try {
                BeanUtils.populate(asynchronousTask, params);
            } catch (IllegalAccessException e) {
                LOG.error(e);
            } catch (InvocationTargetException e) {
                LOG.error(e);
            }
        }
    }

}


