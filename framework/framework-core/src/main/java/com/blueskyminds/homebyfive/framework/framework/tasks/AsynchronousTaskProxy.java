package com.blueskyminds.homebyfive.framework.framework.tasks;

import java.util.concurrent.Callable;
import java.util.Date;

/**
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AsynchronousTaskProxy implements Callable<AsynchronousTask> {

    private final AsynchronousTask asynchronousTask;
    private ExecutorProvider executorProvider;
    private Date timeStarted;

    public AsynchronousTaskProxy(AsynchronousTask asynchronousTask, ExecutorProvider executorProvider) {
        this.asynchronousTask = asynchronousTask;
        this.executorProvider = executorProvider;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public AsynchronousTask call() throws Exception {
        timeStarted = new Date();
        asynchronousTask.startBlocking();
        return asynchronousTask;
    }
        
}
