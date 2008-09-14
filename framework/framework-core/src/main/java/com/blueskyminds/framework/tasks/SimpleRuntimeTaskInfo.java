package com.blueskyminds.framework.tasks;

import java.util.Date;

/**
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SimpleRuntimeTaskInfo implements RuntimeTaskInfo {

    private String key;
    private Date timestamp;
    private AsynchronousTaskStatus status;
    private Object model;

    public SimpleRuntimeTaskInfo(String key, Date timestamp, AsynchronousTaskStatus status, Object model) {
        this.key = key;
        this.timestamp = timestamp;
        this.status = status;
        this.model = model;
    }

    public String getKey() {
        return key;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public AsynchronousTaskStatus getStatus() {
        return status;
    }

    public Object getModel() {
        return model;
    }

    public String toString() {
        return status+" "+timestamp+" "+model;
    }
}
