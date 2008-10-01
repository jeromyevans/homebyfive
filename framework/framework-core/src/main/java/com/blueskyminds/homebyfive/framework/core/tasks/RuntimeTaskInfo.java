package com.blueskyminds.homebyfive.framework.core.tasks;

import java.util.Date;

/**
 * Run-time information about a task
 *
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface RuntimeTaskInfo {

    String getKey();
    Date getTimestamp();
    AsynchronousTaskStatus getStatus();
    Object getModel();
}
