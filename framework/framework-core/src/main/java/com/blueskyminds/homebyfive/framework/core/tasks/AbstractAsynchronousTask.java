package com.blueskyminds.homebyfive.framework.core.tasks;

import java.util.Date;

/**
 * Implements some common behaviour for an AsynchronousTask
 * <p/>
 * Date Started: 21/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class AbstractAsynchronousTask implements AsynchronousTask {

    protected String key;
    protected boolean paused;
    protected boolean shuttingDown;
    protected Date startTime;
    protected AsynchronousTaskStatus status;

    protected AbstractAsynchronousTask() {
        init();
    }

    private void init() {
        paused = false;
        shuttingDown = false;
        status = AsynchronousTaskStatus.Idle;
    }

    /**
     * Will return true if the status is not running and not paused
     *
     * @return
     */
    public synchronized boolean isReadyToStart() {
        return !AsynchronousTaskStatus.Running.equals(status) && !AsynchronousTaskStatus.Paused.equals(status);
    }

    public synchronized boolean pause() {
        if (AsynchronousTaskStatus.Running.equals(status)) {
            paused = true;
            status = AsynchronousTaskStatus.Paused;
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean unpause() {
        if (AsynchronousTaskStatus.Paused.equals(status)) {
            paused = false;
            status = AsynchronousTaskStatus.Running;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Instructs the LongIterativeTask to shutdown.  It will exit on the next processing iteration
     *
     * @return true
     */
    public boolean stop() {
        shuttingDown = true;
        return true;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * The default reset implementation clears the flags and exits immediately
     */
    public boolean reset() {
        init();
        return true;
    }

    protected synchronized void setStatus(AsynchronousTaskStatus status) {
        this.status = status;
    }

    public AsynchronousTaskStatus getStatus() {
        return status;
    }


}
