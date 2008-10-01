package com.blueskyminds.homebyfive.framework.framework.tasks;

import com.blueskyminds.homebyfive.framework.framework.memento.XMLMemento;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A group of tasks that are executed as a unit of work
 *
 * A TaskGroup implements its own scheduler that executes the tasks in the group
 *
 * A TaskGroup is itself a Task and Taskable (Caretaker and Originator)
 *
 * Important:
 *   A taskGroup executes all of its subtasks concurrently, except for TaskGroups.  A TaskGroup is
 *     executed on the same thread to ensure the threadpool is not exhausted by only TaskGroups that
 *     cannot execute their tasks
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Group")
public class TaskGroup extends Task implements Taskable {

    public static final Log LOG = LogFactory.getLog(TaskGroup.class);

    private List<Task> tasks;
    private TaskCoordinator taskCoordinator;

    // ------------------------------------------------------------------------------------------------------
    /** Create a new taskgroup */
    public TaskGroup(String name) {
        super(name);
        setOriginator(this);  // this task contains the implementation
        init();
    }

    /** Create a new taskgroup with the specified memento implementation */
    public TaskGroup(String name, XMLMemento memento) {
        super(name, memento);
        setOriginator(this);  // this task contains the implementation
        init();
    }

    /** Default constructor for ORM */
    protected TaskGroup() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TaskGroup with default attributes
     */
    private void init() {
        tasks = new LinkedList<Task>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add a task to this group */
    public boolean addTask(Task task) {
        return tasks.add(task);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Adds all of the specified tasks to this group */
    public boolean addAll(List<Task> tasks) {
        return this.tasks.addAll(tasks);
    }

    // ------------------------------------------------------------------------------------------------------

    /** A TaskGroup is ready if:
     *     it is ready itself; and
     *     if ANY of the tasks in its group are ready */
    @Transient
    @Override
    public boolean isReady() {
        boolean ready = false;

        if (!isComplete()) {
            if (areDependenciesComplete()) {
                for (Task task : tasks) {
                    if (task.isReady()) {
                        ready = true;
                        break;
                    }
                }
            }
        }

        return ready;
    }

    // ------------------------------------------------------------------------------------------------------

    /** A TaskGroup is complete only if ALL of the tasks in the group are complete */
    @Transient
    @Override
    public boolean isComplete() {
        boolean allComplete = true;

        for (Task task : tasks) {
            if (!task.isComplete()) {
                allComplete = false;
                break;
            }
        }

        return allComplete;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Resets all the tasks in this task group.
     * @return true if all tasks managed to reset */
    public boolean reset() {
        boolean allReset = true;
        for (Task task : tasks) {
            if (!task.reset()) {
                allReset = false;
                break;
            }
        }
        return allReset;
    }

    // ------------------------------------------------------------------------------------------------------

    /** A task group creates its own scheduler to schedule and execute the tasks within the group
     * (blocking)
     *
     * If a task in this group it itself a group, the task is executed in this thread first (blocking) to ensure
     *  the thread pool is not exhausted without any actually tasks executing (a deadlock situation where only
     *  group parents are executing and no tasks can be created)
     * */

    public void start(Task envelope) {
        List<Task> concurrentTasks = new ArrayList<Task>(tasks.size());
        for (Task task : tasks) {
            task.setTaskExecutor(getTaskExecutor());           // inject the executor into the Task
            if (task instanceof TaskGroup) {
                // execute the taskgroup
                task.start();
            } else {
                // schedule the task
                concurrentTasks.add(task);
            }
        }
        // now execute the remaining concurrent tasks
        if (concurrentTasks.size() > 0) {
//            taskCoordinator = new TaskCoordinator(concurrentTasks);
//            taskCoordinator.setTaskExecutor(getTaskExecutor());   // inject the executor into the Scheduler
//            LOG.info("Starting scheduler ("+getName()+")");
//            taskCoordinator.startBlocking();
//            taskCoordinator.cancel();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Requests all the tasks in this group to shutdown, and if the task scheduler is running, also
     *  requests the task scheduler to shutdown
     * @return true if all tasks accepted the shutdown command
     */
    public boolean shutdown() {
        boolean allOkay = true;

        for (Task task : tasks) {
            if (!task.shutdown()) {
                allOkay = false;
                // no break - request shutdown of all tasks even if some failed
            }
        }

        if (taskCoordinator != null) {
            taskCoordinator.cancel();
        }

        return allOkay;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get a list of the tasks in this group */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="TaskGroupEntry",
            joinColumns=@JoinColumn(name="TaskGroupId"),
            inverseJoinColumns = @JoinColumn(name="TaskId")
    )
    public List<Task> getTasks() {
        return tasks;
    }

    protected void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void print(PrintStream out, int depth) {
        int newDepth = depth+1;
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            output.append("-");
        }
        output.append(fullName());
        out.println(output.toString());

        for (Task child : tasks) {
            child.print(out, newDepth);
        }
    }

    /**
     * Get static information about this task group and its children.
     *
     * Information on the children is provided by the TaskCoordinator because it can determine whether
     *  the tasks are running or not.
     **/
    @Transient
    public TaskInfo getInfo() {
//        TaskInfo taskInfo = new TaskInfo(getName(), isReady(), isComplete(), null);
//        taskInfo.addChildren(taskCoordinator.listActive());
//        return taskInfo;
        return null;
    }

    @Transient
    public XMLMemento getRuntimeStatus() {
        return null;  
    }
}
