package com.blueskyminds.homebyfive.framework.core.tasks.service;

import com.blueskyminds.homebyfive.framework.core.tasks.AsynchronousTask;

import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;

/**
 * Date Started: 7/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class AbstractAsynchronousTaskFactory implements AsynchronousTaskFactory {

    private static final Log LOG = LogFactory.getLog(AbstractAsynchronousTaskFactory.class);
    private static final String[] RESERVED_PARAMS = {"info", "status", "key"};

    public void populate(AsynchronousTask asynchronousTask, Map<String, Object> params) throws TaskingException {         
        if (params != null) {

            validateParameters(asynchronousTask, params);

            try {
                BeanUtils.populate(asynchronousTask, params);
            } catch (IllegalAccessException e) {
                throw new TaskingException("Failed to set task properties ("+asynchronousTask.getClass().getName()+")", e);
            } catch (InvocationTargetException e) {
                throw new TaskingException("Failed to set task properties ("+asynchronousTask.getClass().getName()+")", e);
            }
        }
    }

    /**
     * Assert that we aren't trying to set reserved parameters.
     * This is mostly to assist as setting them will fail or be overwritten.
     *
     * @param asynchronousTask
     * @param params
     * @throws TaskingException
     */
    protected void validateParameters(AsynchronousTask asynchronousTask, Map<String, Object> params) throws TaskingException {

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (ArrayUtils.contains(RESERVED_PARAMS, entry.getKey())) {
                LOG.error("The parameter '"+entry.getKey()+"' is reserved by the TaskingService and cannot be set when creating an AsynchronousTask");
                throw new TaskingException("THe param '"+entry.getKey()+" is reserved by the TaskingService.  "+asynchronousTask.getClass().getName()+" not created.");
            }
        }
    }
}
