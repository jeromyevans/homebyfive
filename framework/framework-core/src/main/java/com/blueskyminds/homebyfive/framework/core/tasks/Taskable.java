package com.blueskyminds.homebyfive.framework.core.tasks;

import com.blueskyminds.homebyfive.framework.core.memento.MementoOriginator;
import com.blueskyminds.homebyfive.framework.core.memento.XMLMemento;

/**
 * A class that implements the Taskable interface can be executed as a Task
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Taskable extends MementoOriginator {

    /** Get the name given to this task */
    String getName();

    void setName(String name);

    /** Start the task (blocking is permitted).
     * The implementor must ensure the method updates the Complete flag on completion */
    void start(Task envelope);

    /** This flag indicates whether the task is complete */
    boolean isComplete();

    /**
     * Shutdown the task, if possible
     *
     * @return true of the shutdown command was accepted (not necessarily completed)
     */
    boolean shutdown();

    /** Resets the task so its ready to start
     * @return true if the task was able to reset */
    boolean reset();

    /** Get the run-time status of the task */
    XMLMemento getRuntimeStatus();

}
