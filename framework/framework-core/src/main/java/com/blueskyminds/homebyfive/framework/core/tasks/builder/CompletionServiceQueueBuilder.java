package com.blueskyminds.homebyfive.framework.core.tasks.builder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

/**
 * Used to select the type of queue to use
 *
 * Date Started: 1/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CompletionServiceQueueBuilder {

    private boolean linkedBlockingQueue;
    private boolean synchronousQueue;
    private boolean concurrentLinkedQueue;

    public static CompletionServiceQueueBuilder create() {
        return new CompletionServiceQueueBuilder();
    }

    public CompletionServiceThreadPoolBuilder withLinkedBlockingQueue() {
        linkedBlockingQueue = true;
        return new CompletionServiceThreadPoolBuilder(this);
    }

    public CompletionServiceThreadPoolBuilder withSyncronousQueue() {
        synchronousQueue = true;
        return new CompletionServiceThreadPoolBuilder(this);
    }

    public CompletionServiceThreadPoolBuilder withConcurrentLinkedQueue() {
        concurrentLinkedQueue = true;
        return new CompletionServiceThreadPoolBuilder(this);
    }

    protected Queue<Runnable> build() {
        Queue<Runnable> workQueue = null;

        if (linkedBlockingQueue) {
            workQueue = new LinkedBlockingQueue<Runnable>();
        }

        if (synchronousQueue) {
            workQueue = new SynchronousQueue<Runnable>();
        }

        if (concurrentLinkedQueue) {
            workQueue = new ConcurrentLinkedQueue<Runnable>();
        }

        return workQueue;
    }
}
