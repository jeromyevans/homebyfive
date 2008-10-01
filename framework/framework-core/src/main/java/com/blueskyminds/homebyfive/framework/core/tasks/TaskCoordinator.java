package com.blueskyminds.homebyfive.framework.core.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Coordinates the execution of one or more Tasks.
 *
 * Execution of the tasks is a blocking operation, however the tasks will be executed concurrently if the Executor
 *  and Task depencencies allow them to be.
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TaskCoordinator {

    private static final Log LOG = LogFactory.getLog(TaskCoordinator.class);

    private String name;

    private final List<AsynchronousTask> tasks;
    private final List<Future<AsynchronousTask>> futures;   // access to futures is synchronized

    private boolean shutdown;

    private boolean createdExecutor;
    private ExecutorProvider executorProvider;

    /** A flag that can be used to turn off the asynchronous behaviour of the scheduler, useful for debugging */
    private boolean runSynchronous;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     *  Setup the scheduler to execute the list of tasks.
     *
     *  The tasks may be executed concurrently.
     *
     * @param tasks
     */
    public TaskCoordinator(List<AsynchronousTask> tasks) {
        this.name = "["+tasks+"]";
        this.tasks = tasks;
        futures = Collections.synchronizedList(new LinkedList<Future<AsynchronousTask>>());

        init();
    }

    /**
     * Setup a single task to be executed.
     * 
     * @param rootTask
     */
    public TaskCoordinator(String name, AsynchronousTask rootTask) {
        this.name = name;
        this.tasks = new LinkedList<AsynchronousTask>();
        futures = Collections.synchronizedList(new LinkedList<Future<AsynchronousTask>>());
        tasks.add(rootTask);

        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TaskCoordinator with default attributes
     */
    private void init() {
        shutdown = false;
        runSynchronous = false;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Set the SimpleExecutorProvider to use.  May be useful if all scheduler instances should use the same
     * executor service.
     * The scheduler will create its own executor if one is not set
     *
     *
     * @param executorProvider
     */
    public void setTaskExecutor(ExecutorProvider executorProvider) {
        this.executorProvider = executorProvider;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the executor the task scheduler should use.  The executor controls how many tasks
     *  can be run concurrently.  If a ExecutorProvider instance has not been set, a SimpleExecutorProvider  is
     * created
     *
     * @return the executor to use
     */
    private Executor executor() {
        if (executorProvider == null) {
            executorProvider = new SimpleExecutorProvider();
            createdExecutor = true;
        }

        return executorProvider.getExecutor();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Instruct the scheduler to create it's own executor.
     * Creation will occur automatically if no other executor is specified
      */
    public Executor createExecutor() {
        return executor();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Starts execution of the tasks
     * <p/>
     * Reset all the tasks<p/>
     * Wraps the tasks in a Callable interface, and submits them to a CompletionService<p/>
     *    The CompletionSevice implements a blocking queue that stores task information as
     * each completes (potentially out-of-order).  The completed tasks are tracked, and the
     * loop of scheduling tasks and waiting until they complete continues until all have
     * completed successfully.<p/>
     *
     * Note: there is potential for deadlock here if a circular loop exists in the task dependencies<p/>
     *
     * The maximum concurrent tasks is controlled by:
     *    task dependencies; and
     *    the memento of the Executor
     *<p/>
     * This is a BLOCKING operation.<p/>
     *
     * @return true if all tasks completed ok
     *
     *  */
    public boolean startBlocking() {
        boolean allComplete = false;
        int resultNo;
        int tasksSubmitted;
        List<AsynchronousTaskProxy> readyTasks = new ArrayList<AsynchronousTaskProxy>(tasks.size());
        List<AsynchronousTask> tasksCompleted = new LinkedList<AsynchronousTask>();
        List<AsynchronousTask> tasksRemaining;
        CompletionService<AsynchronousTask> ecs = new ExecutorCompletionService<AsynchronousTask>(executor());

        if (resetAll()) {

            // iterate until all tasks have completed
            while (!allComplete) {
                readyTasks.clear();

                futures.clear();

                // recalculate the list of remaining tasks
                tasksRemaining = new LinkedList<AsynchronousTask>(tasks);
                tasksRemaining.removeAll(tasksCompleted);

                // create a CallableTask for each READY task so it's Callable by the executor
                for (AsynchronousTask task : tasksRemaining) {
                    if (task.isReadyToStart()) {
                        readyTasks.add(new AsynchronousTaskProxy(task, executorProvider));
                    }
                }

                if (readyTasks.size() == 0) {
                    // no tasks are ready for processing - this could be due to a deadlock situation
                } else {

                    // try to avoid starting tasks if shutting down (race condition, so no guarantees)
                    if (!shutdown) {
                        if (!runSynchronous) {
                            // submit all the tasks for asynchronous processing
                            for (AsynchronousTaskProxy task : readyTasks) {
                                futures.add(ecs.submit(task));
                            }
                        } else {
                            // execute the tasks synchronously - call them directly
                            for (AsynchronousTaskProxy task : readyTasks) {
                                try {
                                    AsynchronousTask t = task.call();
                                    tasksCompleted.add(t);
                                } catch (Exception e) {
                                    LOG.error("Task "+task+" threw an exception during synchronous execution",e);
                                }
                            }
                        }
                    }

                    if (!runSynchronous) {
                        // get the results from the tasks submitted to the completion service
                        // - the tasks may complete out-of-order
                        // - this loop will block for the duration of the longest task
                        resultNo = 0;
                        tasksSubmitted = readyTasks.size();
                        while ((resultNo < tasksSubmitted) && (!shutdown)) {
                            try {
                                // get the next completed task from the blocking queue
                                AsynchronousTask task = ecs.take().get();

                                if (AsynchronousTaskStatus.Complete.equals(task.getStatus())) {
                                    // add this task to the list of completed tasks
                                    tasksCompleted.add(task);
                                } else {
                                    LOG.warn("Task "+task+" executed but did not complete.  Rescheduling if not shutting down.");
                                }
                            } catch (InterruptedException e) {
                                LOG.warn("A task was interrupted.  Rescheduling if not shutting down.");
                            } catch (ExecutionException e) {
                                LOG.error("A task failed execution.  Rescheduling if not shutting down.", e);   // todo: remove task?/abort all?
                            } catch (CancellationException e) {
                                LOG.warn("A task was cancelled.  Rescheduling if not shutting down.");
                            }

                            resultNo++;
                        }
                    }
                }

                // determine if all tasks are complete - this is evaluated from the list of completed tasks
                allComplete = (tasksCompleted.size() == tasks.size()) || (shutdown);
            }
            if (shutdown) {
                LOG.info("TaskCoordinator("+name+") has completed due to shutdown");
            } else {
                LOG.info("TaskCoordinator("+name+") has completed all tasks");
            }
        }

        return allComplete;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise all the tasks in this scheduler
     * @return true if all tasks initialised okay
     * */
    private boolean resetAll() {
        boolean okay = true;
        for (AsynchronousTask task : tasks) {
            if (!task.reset()) {
                okay = false;
                // no break (attempt shutdown on all, even if some failed)
                break;
            }
        }
        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Instructs the scheduler to cancel all future tasks and shutdown.
     * The scheduler DOES NOT instruct the tasks to shutdown as well - it only shuts down the excutor's loop
     * and cancels any future tasks not yet executing.  A task in progress will not be cancelled (instead,
     * shut it down gracefully via an alternative means)
     *
     * @return true if shutdown accepted
     */
    public boolean cancel() {

        // setting the shutdown flag will break out of the processing loop
        shutdown = true;

        // iteration over futures must be synchronized
        synchronized (futures) {
            if (futures != null) {
                for (Future<AsynchronousTask> future : futures) {
                    // cancel the future if it's not currently running
                    future.cancel(false);
                }
            }
        }
      
        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Cancels all future tasks (see @cancel) and requests ALL  tasks to shutdown (whether running or not)
     *
     * Shutdown is not guaranteed as the tasks may not implement a shutdown.
     * The method blocks until all shutdown requests exit.
     * 
     * @return
     */
    public void shutdown() {
        cancel();
        for (AsynchronousTask task : tasks) {
            task.stop();
        }

        if (createdExecutor) {
            ((SimpleExecutorProvider) executorProvider).shutdownNow();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Executes the specified task asynchronously and returns a Future to monitor its completion
     * This method is really only intended for testing  - background tasks should normally be executed
     * by an appropriately configured Executor
     *
     * @param task
     * @return Future<Task>
     */
    public static Future<Task> runInBackground(Task task) {
        ExecutorService localExecutor = Executors.newFixedThreadPool(1);
        return localExecutor.submit(new CallableTask(task));
    }

    // ------------------------------------------------------------------------------------------------------

    /** If runSynchronous is set to true, the scheduler will not create asynchronous tasks and will instead
     *  execute each task sequentially and in a blocking mode in the current thread.  This is useful for
     *  debugging.
     * @param runSynchronous - set to true to disable the asynchronous behaviour
     */
    public void setRunSynchronous(boolean runSynchronous) {
        this.runSynchronous = runSynchronous;
    }

    public String getName() {
        return name;
    }

    /** List information about the tasks being coordinated by this instance */
    public List<RuntimeTaskInfo> list() {
        List<RuntimeTaskInfo> activeTaskInfos = new LinkedList<RuntimeTaskInfo>();
        synchronized (tasks) {
            for (AsynchronousTask task : tasks) {
                RuntimeTaskInfo taskInfo = task.getInfo();
                activeTaskInfos.add(taskInfo);
            }
        }
        return activeTaskInfos;
    }

    /** True if all tasks are complete */
    public boolean isComplete() {
        boolean complete = true;
        synchronized (tasks) {
            for (AsynchronousTask task : tasks) {
                if (!AsynchronousTaskStatus.Complete.equals(task.getStatus())) {
                    complete = false;
                    break;
                }
            }
        }
        return complete;
    }
  
}
