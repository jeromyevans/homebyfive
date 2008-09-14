package com.blueskyminds.framework.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * A task that tasks a long time to execute and has start, stop, pause and unpause semantics.
 *
 * Subclasses can split the work up into interative packages implemented in the iterate() method.
 * The iterate() method will be called if the task is started, not paused, not shutting down and not completed.
 *
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public abstract class LongIterativeTask extends AbstractAsynchronousTask {

    private static final Log LOG = LogFactory.getLog(LongIterativeTask.class);

    protected LongIterativeTask() {
        init();
    }

    private void init() {
        paused = false;
        shuttingDown = false;
        status = AsynchronousTaskStatus.Idle;
    }

    /**
     * Iteratively calls the implementation's iterate method unless paused, shutting down, complete or aborted
     */
    public void startBlocking() {
        boolean complete = false;
        boolean aborted = false;

        startTime = new Date();
        LOG.debug("prepare...");
        prepare();

        setStatus(AsynchronousTaskStatus.Running);
        while (((!shuttingDown) && (!complete)) && (!aborted)) {
            try {
                if (paused) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        LOG.debug("Pause interrupted");
                    }
                } else {
                    LOG.debug("   calling iterate...");
                    complete = iterate();
                }
            } catch (CannotCompleteException e) {
                aborted = true;
                setStatus(AsynchronousTaskStatus.Failed);
            } catch (RuntimeException e) {
                setStatus(AsynchronousTaskStatus.Failed);
                throw e;
            }
        }

        if (complete) {
            setStatus(AsynchronousTaskStatus.Complete);
        } else {
            if (shuttingDown) {
                setStatus(AsynchronousTaskStatus.Interrupted);
            }
        }

        LOG.debug("complete");
    }
    
    /**
     * Get information about a running task
     */
    public RuntimeTaskInfo getInfo() {
        return new SimpleRuntimeTaskInfo(key, new Date(), status, getStatusModel());
    }

    /**
     * Prepare the processing operation.  This is a good place to extract any useful information from the
     * memento
     */
    protected abstract void prepare();

    /**
     * Perform the next iteration of work.
     *
     * @return true if all work is completed
     */
    protected abstract boolean iterate() throws CannotCompleteException;

    /** Get information about the run-time status of the task */
    protected abstract Object getStatusModel();
}
