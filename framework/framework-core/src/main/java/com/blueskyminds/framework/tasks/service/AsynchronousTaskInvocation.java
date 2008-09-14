package com.blueskyminds.framework.tasks.service;

import com.blueskyminds.framework.tasks.*;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates a new task instance and invokes it within the current thread
 *
 * Date Started: 30/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */ 
public class AsynchronousTaskInvocation implements Runnable {

    private static final Log LOG = LogFactory.getLog(AsynchronousTaskInvocation.class);

    private final AsynchronousTaskFactory asynchronousTaskFactory;
    
    private final String key;
    private final AvailableTask availableTask;
    private final Map<String, Object> runtimeParams;
    private final TaskInitializer initializer;

    private TaskingException lastException;
    private TaskCoordinator taskCoordinator;

    public AsynchronousTaskInvocation(AsynchronousTaskFactory asynchronousTaskFactory, String key, AvailableTask availableTask, Map<String, Object> runtimeParams, TaskInitializer initializer) {
        this.asynchronousTaskFactory = asynchronousTaskFactory;
        this.key = key;
        this.availableTask = availableTask;
        this.runtimeParams = runtimeParams;
        this.initializer = initializer;
    }

    public void run() {
        try {
            Class<? extends AsynchronousTask> aClass = availableTask.getTaskClass();
            if (aClass != null) {
                if (asynchronousTaskFactory != null) {
                    final AsynchronousTask asynchronousTask = asynchronousTaskFactory.create(key, aClass, availableTask.getParams());
                    if (asynchronousTask != null) {
                        // populate the task with runtime parameters
                        if (runtimeParams != null) {
                            asynchronousTaskFactory.populate(asynchronousTask, runtimeParams);
                        }

                        // invoke the initializer
                        if (initializer != null) {
                            initializer.initialize(asynchronousTask);
                        }

                        taskCoordinator = new TaskCoordinator(key, asynchronousTask);
                        asynchronousTask.reset();

                        taskCoordinator.startBlocking();
                    } else {
                        LOG.error("Failed to instantiate AsynchronousTask "+key+" (factory returned null)");
                        lastException = new TaskingException("Failed to instantiate AsynchronousTask "+key+" (factory returned null)");
                    }
                } else {
                    LOG.error("AsynchronousTaskFactory not available");
                    lastException = new TaskingException("AsynchronousTaskFactory not available");
                }
            } else {
                lastException = new TaskingException("AsynchronousTask class not defined");
            }
        } catch (TaskingException e) {
            lastException = e;
        } catch (Throwable e) {
            LOG.error(e);
            lastException = new TaskingException(e);
        }
    }

    public TaskCoordinator getTaskCoordinator() {
        return taskCoordinator;
    }

    public TaskingException getLastException() {
        return lastException;
    }

    public boolean isErrors() {
        return lastException != null;
    }

    public void shutdown() {
        if (taskCoordinator != null) {
            taskCoordinator.shutdown();
        }
    }
}
