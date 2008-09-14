package com.blueskyminds.framework.tasks.service;

import com.blueskyminds.framework.tasks.Taskable;
import com.blueskyminds.framework.tasks.AsynchronousTask;

import java.util.Map;

/**
 * An Object Factory for Asynchronous Tasks
 *
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface AsynchronousTaskFactory {

    /**
     * Create/get an instance of the specified clazz and inject its parameters
     *
     * This method will always be invoked within the task's own thread
     *
     * @param key   key to assign to the instance
     * @param clazz @return
     */
    AsynchronousTask create(String key, Class<? extends AsynchronousTask> clazz, Map<String, Object> params) throws TaskingException;

    /**
     * Populate properties of the asynchronous task instance
     *
     * @param asynchronousTask
     * @throws TaskingException
     */
    void populate(AsynchronousTask asynchronousTask, Map<String, Object> params) throws TaskingException;
}
