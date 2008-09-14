package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.tools.text.StringTools;
import com.blueskyminds.framework.memento.XMLMemento;

import java.util.List;
import java.util.Date;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * Simple static information about a task (and its children, if applicable)
 *
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = 8196000475792331939L;

    private Date timestamp;
    private String name;
    private boolean ready;
    private boolean complete;
    private boolean running;
    private RuntimeTaskInfo status;

    private List<TaskInfo> children;

    public TaskInfo(String name, boolean ready, boolean complete, RuntimeTaskInfo status) {
        timestamp = new Date();
        this.name = name;
        this.ready = ready;
        this.complete = complete;
        this.running = false;
        this.status = status;
        children = new LinkedList<TaskInfo>();
    }

    public TaskInfo(String name, boolean ready, boolean complete, boolean running, RuntimeTaskInfo status) {
        this(name, ready, complete, status);
        this.running = running;
    }

    public void addChild(TaskInfo taskInfo) {
        children.add(taskInfo);
    }

    public void addChildren(List<? extends TaskInfo> taskInfo) {
        children.addAll(taskInfo);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isRunning() {
        return running;
    }

    public RuntimeTaskInfo getStatus() {
        return status;
    }

    public List<TaskInfo> getChildren() {
        return children;
    }

    /** Generates a string description that recrisively includes children indented by one space */
    protected String toString(int depth) {
        StringBuilder result = new StringBuilder(StringTools.fill(" ", depth));
        result.append(name+" ready: "+ready+" complete: "+complete+" running: "+running+ " hasChildren: "+hasChildren()+"\n");
        result.append("status="+status+"\n");
        depth++;
        for (TaskInfo child : getChildren()) {            
            result.append(child.toString(depth));
        }
        return result.toString();
    }

    public String toString() {
        return toString(0);
    }
}
