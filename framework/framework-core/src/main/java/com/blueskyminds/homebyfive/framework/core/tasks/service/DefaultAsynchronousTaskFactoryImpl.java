package com.blueskyminds.homebyfive.framework.core.tasks.service;

import com.blueskyminds.homebyfive.framework.core.tasks.AsynchronousTask;
import com.blueskyminds.homebyfive.framework.core.tools.ReflectionTools;
import com.blueskyminds.homebyfive.framework.core.tools.ReflectionException;

import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DefaultAsynchronousTaskFactoryImpl extends AbstractAsynchronousTaskFactory {

    /**
     * Create/get an instance of the specified clazz
     *
     * @param key
     *@param clazz @return
     */
    public AsynchronousTask create(String key, Class<? extends AsynchronousTask> clazz, Map<String, Object> params) throws TaskingException {
        try {
            AsynchronousTask asynchronousTask = ReflectionTools.createInstanceOf(clazz);
            if (asynchronousTask != null) {
                asynchronousTask.setKey(key);
                populate(asynchronousTask, params);
            }
            return asynchronousTask;
        } catch (ReflectionException e) {
            throw new TaskingException(e);
        }
    }

}
