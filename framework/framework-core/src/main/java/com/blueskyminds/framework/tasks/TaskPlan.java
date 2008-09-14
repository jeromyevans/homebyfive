package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.*;
import java.io.PrintStream;

/**
 * Contains one of more tasks to be executed.
 * References the top-level tasks
 *
 * The TaskPlan is presistent
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated 
@Entity
@Table(name="TaskPlan")
public class TaskPlan extends AbstractDomainObject {

    private String name;
    private Task rootTask;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new task plan with the specified name */
    public TaskPlan(String name) {
        this.name = name;
        init();
    }

    /** Default constructor for ORM */
    protected TaskPlan() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TaskPlan with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of this task plan */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the root-level task in this plan */
    @OneToOne(cascade = CascadeType.ALL) // note: this is unidirectional (no mappedBy parameter)
    @JoinColumn(name="TaskId")
    public Task getRootTask() {
        return rootTask;
    }

    public void setRootTask(Task rootTask) {
        this.rootTask = rootTask;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Execute the task plan, blocking until completed.
     *
     * Uses a default executor implementation
     *
     * Note that the tasks in the plan may be executed asynchronously if their dependences allow it.
     *
     * @return true if completed, false if exited scynchronously or failed */
    public boolean start() {
        return start(null);
    }
    
    // ------------------------------------------------------------------------------------------------------

    /**
     * Execute the task plan, blocking until completed.
     *
     * Note however that the tasks in the plan may be executed asynchronously if their dependences allow it.
     *
     * @return true if completed, false if exited scynchronously or failed
     **/
    public boolean start(ExecutorProvider executorProvider) {
        if (rootTask != null) {
            rootTask.setTaskExecutor(executorProvider);
            rootTask.start();
            return rootTask.isComplete();
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

     /**
      * Shutdown the taskplan.
     *
     * @return true of the shutdown command was accepted (not necessarily completed)
     */
    public boolean shutdown() {
         if (rootTask != null) {
             return rootTask.shutdown();
         } else {
             return true;
         }
     }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- TaskPlan: "+getIdentityName()+" ---");
        rootTask.print(out, 1);
    }

}
