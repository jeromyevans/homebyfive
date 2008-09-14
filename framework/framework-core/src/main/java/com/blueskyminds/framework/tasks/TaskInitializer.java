package com.blueskyminds.framework.tasks;

/**
 * Date Started: 28/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface TaskInitializer {

    /**
     * Initialise the asynchronous task that is about to start
     */     
    void initialize(AsynchronousTask asynchronousTask);
}
