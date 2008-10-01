package com.blueskyminds.homebyfive.framework.framework.tasks;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CompositeRuntimeTaskInfo implements RuntimeTaskInfo {

    private String key;
    private List<RuntimeTaskInfo> taskInfos;

    public CompositeRuntimeTaskInfo(String key) {
        this.key = key;
        taskInfos = new LinkedList<RuntimeTaskInfo>();
    }

    public void addAll(List<RuntimeTaskInfo> list) {
        taskInfos.addAll(list);
    }

    public void add(RuntimeTaskInfo runtimeTaskInfo) {
        taskInfos.add(runtimeTaskInfo);
    }

    public String getKey() {
        return key;
    }

    public Date getTimestamp() {
        return null;
    }

    public AsynchronousTaskStatus getStatus() {
        return null;
    }

    /**
     * Returns a list containing all the models of subtasks 
     * @return
     */
    public Object getModel() {
        List<Object> models = new LinkedList<Object>();

        for (RuntimeTaskInfo info : taskInfos) {
            models.add(info.getModel());
        }

        return models;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("--- Composite ("+key+") ---\n");
        for (RuntimeTaskInfo info : taskInfos) {
            result.append(info.toString());
            result.append("\n");
        }
        return result.toString();
    }
}