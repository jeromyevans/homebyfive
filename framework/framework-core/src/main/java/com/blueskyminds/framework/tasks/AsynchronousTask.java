package com.blueskyminds.framework.tasks;

/**
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface AsynchronousTask {


    /** Unqiue key identifying this task
     * @param key*/
    void setKey(String key);

    boolean reset();

    boolean isReadyToStart();

    void startBlocking() throws Exception;

    boolean pause();

    boolean unpause();

    boolean stop();

    AsynchronousTaskStatus getStatus();

    /** Get information about a running task */
    RuntimeTaskInfo getInfo();
}
