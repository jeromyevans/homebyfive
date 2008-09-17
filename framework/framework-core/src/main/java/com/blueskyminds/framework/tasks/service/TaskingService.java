package com.blueskyminds.framework.tasks.service;

import com.blueskyminds.framework.tasks.*;

import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 *
 * A service responsible for CRUD and execution of Task's
 *
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2006 Blue Sky Minds Pty Ltd<br/>
 */
public interface TaskingService {

    /**
     * Instruct the tasking service to use the specified factory when instantiating tasks
     * @param asynchronousTaskFactory
     */
    void setAsynchronousTaskFactory(AsynchronousTaskFactory asynchronousTaskFactory);

    /**
     * Register a task with the service
     *
     * @param key
     * @param clazz
     */
    AvailableTask registerTask(String key, Class<? extends AsynchronousTask> clazz);

    /** Get a list containing the available tasks */
    List<AvailableTask> listAvailable();

    /**
     * Asynchronously start the task identified by its unique key
     *
     * @param key unique key of the task to start
     * @throws TaskingException if the task is already running
     */
    void start(String key) throws TaskingException;

    /**
     * Asynchronously start the task identified by its unique key, setting the named properties of the task
     *
     * @param key unique key of the task to start
     * @throws TaskingException if the task is already running
     */
    void start(String key, Map<String, Object> runtimeParams) throws TaskingException;

    /**
     * Asynchronously start the task identified by its unique key.  The initializer will be invoked prior
     * to starting the task
     *
     * @param key unique key of the task to start
     * @param initializer
     * @throws TaskingException if the task is already running
     */
    void start(String key, TaskInitializer initializer) throws TaskingException;

    /**
     * Attempt to stop the task with the specified key
     *
     * @param key unique key for the task.  If not recognised the request will be ignored
     */
    void stop(String key) throws TaskingException;

    /**
     * List the currently active tasks
     *
     * @return information about the current tasks
     */
    Set<RuntimeTaskInfo> listActive();

    void shutdownNow();
}
