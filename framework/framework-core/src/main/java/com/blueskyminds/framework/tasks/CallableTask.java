package com.blueskyminds.framework.tasks;

import java.util.concurrent.Callable;

/**
 *
 * Executes a Task asynchronously
 * Encapusulates the underlying concurrency implementation
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class CallableTask implements Callable<Task> {

    private Task task;

    /** Create a service ready to execute the task */
    public CallableTask(Task task) {
        this.task = task;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the CallableTask with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** A blocking call that starts the wrapped task.  This method should be called by the
     * Executor service
     *
     * @return the task that was executed */
    public Task call() throws Exception {
        task.start();
        return task;
    }

    // ------------------------------------------------------------------------------------------------------
}
