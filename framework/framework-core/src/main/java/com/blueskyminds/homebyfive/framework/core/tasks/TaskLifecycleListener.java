package com.blueskyminds.homebyfive.framework.core.tasks;

/**
 * This interface may be implemented by any class that wishes to listen when a new Taskable is
 *  instantiated and about to be executed.
 *
 * Implementors may use this opportunity to inject dependencies into the Task
 *
 * Date Started: 4/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface TaskLifecycleListener {

    /** This method is called immediately prior to starting a task.  It may be used to inject
     *  dependencies
     * @param taskable
     */
    void onStart(Taskable taskable);

}
