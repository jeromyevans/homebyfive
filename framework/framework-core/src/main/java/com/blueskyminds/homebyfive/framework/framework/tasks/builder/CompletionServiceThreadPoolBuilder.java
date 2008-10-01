package com.blueskyminds.homebyfive.framework.framework.tasks.builder;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.Queue;

/**
 * Used to select the type of queue to use
 *
 * Date Started: 1/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CompletionServiceThreadPoolBuilder {

    private CompletionServiceQueueBuilder queueBuilder;
    private int minPoolSize;
    private int maxPoolSize;
    private int keepAliveSecs;

    protected CompletionServiceThreadPoolBuilder(CompletionServiceQueueBuilder queueBuilder) {
        this.queueBuilder = queueBuilder;
    }

    public CompletionServiceBuilder withThreadPool(int minSize, int maxSize, int keepAliveInSeconds) {
        this.minPoolSize = minSize;
        this.maxPoolSize = maxSize;
        this.keepAliveSecs = keepAliveInSeconds;
        return new CompletionServiceBuilder(this);
    }

    protected ThreadPoolExecutor build() {
        Queue<Runnable> queue = queueBuilder.build();
        ThreadPoolExecutor executor = null;

        if (queue != null) {
            if (BlockingQueue.class.isAssignableFrom(queue.getClass())) {
              executor = new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveSecs, TimeUnit.SECONDS, (BlockingQueue<Runnable>) queue);
            } else {
                
            }
        }

        return executor;
    }
}