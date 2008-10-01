package com.blueskyminds.homebyfive.framework.core.tasks;

import java.util.concurrent.*;

/**
 * Simply wraps a blocking queue and executor that can be shared between task schedulers
 * It's not compulsory to thare the queue and executor, but this is useful if all tasks should be executed
 *  within the same threadpool.
 *
 * Date Started: 8/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class SimpleExecutorProvider implements ExecutorProvider {

    /** The blocking queue of work used by the executor */
    private BlockingQueue<Runnable> workQueue;

    private Executor executor;

    // todo: changing executor memento at run time, even while tasks are running
    /** Settings used when creating an Executor */
    private static final int CORE_POOL_SIZE     = 5;
    private static final int MAXIMUM_POOL_SIZE  = 10;
    private static final int KEEP_ALIVE_TIME    = 180;

    // ------------------------------------------------------------------------------------------------------

    /** Create a task executor with default properties */
    public SimpleExecutorProvider() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SimpleExecutorProvider with default attributes
     */
    private void init() {
        workQueue = new LinkedBlockingQueue<Runnable>();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Get the executor instance to use */
    public Executor getExecutor() {
        return executor;
    }

    public void shutdownNow() {
        if (executor != null) {
            ((ThreadPoolExecutor) executor).shutdownNow();
        }
    }
}
