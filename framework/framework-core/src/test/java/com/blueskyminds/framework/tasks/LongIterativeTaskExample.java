package com.blueskyminds.framework.tasks;

/**
 * Date Started: 25/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LongIterativeTaskExample extends LongIterativeTask {

    private long iterations;

    /**
     * Prepare the processing operation.  This is a good place to extract any useful information from the
     * memento
     *
     * @return true if all work is completed
     */
    protected void prepare() {
        iterations = 0;
    }

    /**
     * Perform the next iteration of work.
     *
     * @return true if all work is completed
     */
    protected boolean iterate() throws CannotCompleteException {
        iterations++;
        return iterations > 50;
    }

    protected Object getStatusModel() {
        return iterations;
    }
 
}
