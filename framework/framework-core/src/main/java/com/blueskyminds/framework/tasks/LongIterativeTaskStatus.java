package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.memento.XMLMemento;

/**
 * Default run-time status for a LongIterativeTask
 *
 * Date Started: 19/03/2008
 * <p/>
 * History:
 */
public class LongIterativeTaskStatus extends XMLMemento {

    private long iterations;
    private boolean paused;
    private boolean shuttingDown;

    public LongIterativeTaskStatus(long iterations, boolean paused, boolean shuttingDown) {
        this.iterations = iterations;
        this.paused = paused;
        this.shuttingDown = shuttingDown;
    }

    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public void setShuttingDown(boolean shuttingDown) {
        this.shuttingDown = shuttingDown;
    }
}
