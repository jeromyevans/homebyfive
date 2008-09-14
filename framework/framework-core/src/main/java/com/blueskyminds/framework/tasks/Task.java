package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.memento.CaretakerDomainObject;
import com.blueskyminds.framework.memento.XMLMemento;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A Task is a CaretakerDomainObject that allows behaviour implementing the Taskable interface to be
 *  persisted, restored and scheduled within a TaskPlan
 *
 * Tasks are intended to be long (seconds to hours duration) operations
 *
 * A task can be scheduled, potentially be executed concurrently and can be can be synchronized by defining
 *  dependencies between them (eg. task not to start until the dependencies are complete)
 *
 * The Task is a CaretakerDomainObject, meaning it implements the XMLMemento design pattern where this
 *  caretaker is responsible for persistence of the XMLMemento and 'taskable' originator
 *
 * To create a new type of task, the developer should :
 *   implement the Taskable interface, or optionally just extend SimpleTask
 *   optionally extend and set XMLMemento to persist the task's state
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Type")
@DiscriminatorValue("Task")
public class Task extends CaretakerDomainObject {

    private static final Log LOG = LogFactory.getLog(Task.class);

    private String name;

    /** These tasks must be complete before this task tasks */
    private List<Task> dependencies;

    /** List of TaskLifecycleListeners listening for when a task is started */
    private List<TaskLifecycleListener> listeners;

    private ExecutorProvider executorProvider;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Create a new task implemented by the Taskable operation */
    public Task(Taskable operation) {
        super(operation, null);
        this.name = operation.getName();
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new task implemented by the Taskable operation */
    public Task(Taskable operation, XMLMemento memento) {
        super(operation, memento);
        this.name = operation.getName();
        init();
    }

    /** Create a new task with the specified name yet. No implementation has been specified yet */
    protected Task(String name) {
        super(null, null);
        this.name = name;
        init();
    }

    /** Create a new task with the specified name and memento. No implementation has been specified yet */
    protected Task(String name, XMLMemento memento) {
        super(null, memento);
        this.name = name;
        init();
    }

    /** Default constructor for ORM */
    protected Task() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the Task with default attributes
     */
    private void init() {
        dependencies = new LinkedList<Task>();
        listeners = new LinkedList<TaskLifecycleListener>();
        executorProvider = null;
    }

    private Taskable taskable() {
        return (Taskable) getOriginator();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the task, prior to starting.  Reset should be performed even if the
     *   task is not ready to start.
     *
      * @return true if reset okay
     */
    public boolean reset() {
        if (taskable() != null) {
            return taskable().reset();
        } else {
            return true;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Start the task (the implemention may block) */
    public void start() {
        if (taskable() != null) {
            injectTaskableSettings();
            taskable().start(this);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Injects memento into the taskable implementation based on the memento loaded from
     * persistence for this task
     */
    private void injectTaskableSettings() {
        if (taskable() != null) {
            taskable().setName(name);
            //taskable().setMemento(memento);
            notifyInjectionListeners(taskable());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Notifies any listeners that the specified Taskable has been instantiated and is about to be
     *  executed.  The listener should perform any necessary dependency injection
     *
     * @param taskable
     */
    private void notifyInjectionListeners(Taskable taskable) {
        if (listeners != null) {
            for (TaskLifecycleListener listener : listeners) {
                listener.onStart(taskable);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Shutdown the task.
     *
     * @return true of the shutdown command was accepted (not necessarily completed)
     */
    public boolean shutdown() {
        if (taskable() != null) {
            return taskable().shutdown();
        } else {
            return true;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** True if this task has already been completed */
    @Transient
    public boolean isComplete() {
        if (taskable() != null) {
            return taskable().isComplete();
        } else {
            return true;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of this task */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines if the dependencies of this task are already complete or not */
    protected boolean areDependenciesComplete() {
        boolean allComplete = true;

        if (dependencies != null) {
            for (Task task : dependencies) {
                if (!task.isComplete()) {
                    // at least one dependency is not complete yet
                    allComplete = false;
                    break;
                }
            }
        }
        return allComplete;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines whether this task is ready to execute.  It it only ready if:
     *    - it is not already complete
     *    - all of the dependencies are complete
     *
     * @return true if this task is ready to start
     */
    @Transient
    public boolean isReady() {
        boolean ready = false;
        if (!isComplete()) {
            if (areDependenciesComplete()) {
                ready = true;
            }
        }
        return ready;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the dependencies for this task.  All dependencies must be complete before this task will
     *  be ready to start
     * @return the list of dependencies
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="TaskDependency",
            joinColumns=@JoinColumn(name="TaskId"),
            inverseJoinColumns = @JoinColumn(name="DependencyId")
    )
    public List<Task> getDependencies() {
        return dependencies;
    }

    protected void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add a dependency to this task on the specified task */
    public boolean addDependency(Task task) {
        return dependencies.add(task);
    }

    // ------------------------------------------------------------------------------------------------------
    /*@Override
    public void print(PrintStream out) {
        out.println(getIdentityName()+" "+name+" ("+getSettingsXml()+")");
    }
*/

    // ------------------------------------------------------------------------------------------------------

    /** Convenient factory method to create a task for a Taskable operation */
    public static Task newInstance(Taskable taskable) {
        return new Task(taskable);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Convenient factory method to create a task for a Taskable operation */
    public static Task newInstance(Taskable taskable, XMLMemento Memento) {
        return new Task(taskable, Memento);
    }

    // ------------------------------------------------------------------------------------------------------

    protected String fullName() {
        return getIdentityName()+" "+getOriginatorName()+" "+getName()+" ("+getSerializedMemento()+")";
    }

    @Override
    public void print(PrintStream out) {
        out.println(fullName());
    }

    public void print(PrintStream out, int depth) {
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            output.append("-");
        }
        output.append(fullName());
        out.println(output.toString());
    }

    /** Registers a listener for the lifecycle events */
    public boolean addLifecycleListener(TaskLifecycleListener taskLifecycleListener) {
        return listeners.add(taskLifecycleListener);
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    protected ExecutorProvider getTaskExecutor() {
        return executorProvider;
    }

    /** Specify a ExecutorProvider instance that the the task's scheduler implementation should use (if applicable) */
    public void setTaskExecutor(ExecutorProvider executorProvider) {
        this.executorProvider = executorProvider;
    }

    @Transient
    public TaskInfo getInfo() {
        //return new TaskInfo(name, isReady(), isComplete(), taskable().getRuntimeStatus());
        return null;
    }

}
