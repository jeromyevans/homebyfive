package com.blueskyminds.homebyfive.framework.framework.tasks.service;

import com.blueskyminds.homebyfive.framework.framework.tasks.*;

import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * This is a stateful implementation and should be created as a singleton.
 * <p/>
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class TaskingServiceImpl implements TaskingService {

    private static final Log LOG = LogFactory.getLog(TaskingServiceImpl.class);

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int KEEP_ALIVE_TIME = 180;

    private final Map<String, AvailableTask> availableTasks;
    private final Map<String, ActiveTask> activeTasks;    // access to active tasks is synchronized

    private AsynchronousTaskFactory asynchronousTaskFactory;

    private ExecutorService executorService;
    private BlockingQueue<Runnable> workQueue;

    public TaskingServiceImpl() {
        activeTasks = new HashMap<String, ActiveTask>();
        availableTasks = new HashMap<String, AvailableTask>();
        workQueue = new LinkedBlockingQueue<Runnable>();
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.asynchronousTaskFactory = new DefaultAsynchronousTaskFactoryImpl();
    }

    public void setAsynchronousTaskFactory(AsynchronousTaskFactory asynchronousTaskFactory) {
        this.asynchronousTaskFactory = asynchronousTaskFactory;
    }

    /**
     * Register a task with the service
     **/
    public AvailableTask registerTask(String key, Class<? extends AsynchronousTask> clazz) {
        AvailableTask availableTask = new AvailableTask(key, clazz);
        availableTasks.put(key, availableTask);
        return availableTask;
    }

    /** Get a list containing the available tasks */
    public synchronized List<AvailableTask> listAvailable() {
        return new ArrayList<AvailableTask>(availableTasks.values());
    }

    /**
     * Asynchronously start the task identified by its unique key
     *
     * @param key unique key of the task to start
     * @throws TaskingException if the task does not exist or the task is already running
     */
    public void start(String key, Map<String, Object> runtimeParams, TaskInitializer initializer) throws TaskingException {

        final AvailableTask availableTask = availableTasks.get(key);

        if (availableTask != null) {
            AsynchronousTaskInvocation asynchronousTaskInvocation = new AsynchronousTaskInvocation(asynchronousTaskFactory, key, availableTask, runtimeParams, initializer);
            
            Future future = executorService.submit(asynchronousTaskInvocation);

            activeTasks.put(key, new ActiveTask(key, asynchronousTaskInvocation, future));
        } else {
            LOG.error("Task "+key+" is not registered");
            throw new TaskingException("Task "+key+" is not registered");
        }
    }

    /**
     * Asynchronously start the task identified by its unique key, setting the named properties of the task
     *
     * @param key unique key of the task to start
     * @throws com.blueskyminds.homebyfive.framework.framework.tasks.service.TaskingException
     *          if the task is already running
     */
    public void start(String key, Map<String, Object> runtimeParams) throws TaskingException {
        start(key, runtimeParams, null);
    }

    public void start(String key, TaskInitializer initializer) throws TaskingException {
        start(key, null, initializer);
    }

    public void start(String key) throws TaskingException {
        start(key, null, null);
    }

    /**
     * Attempt to stop the task with the specified key
     *
     * @param key unique key for the task.  If not recognised the request will be ignored
     */
    public void stop(String key) throws TaskingException {
        LOG.info("STOP");
        ActiveTask activeTask = activeTasks.get(key);

        if (activeTask != null) {
            AsynchronousTaskInvocation asynchronousTaskInvocation = activeTask.getAsynchronousTaskInvocation();
            if (asynchronousTaskInvocation != null) {
                asynchronousTaskInvocation.shutdown();
            }
            activeTask.getFuture().cancel(false);
            activeTasks.remove(key);
        }
    }

    /**
     * List the currently active tasks
     *
     * @return map of active tasks, with unique key as the key and an ActiveTask as the value
     */
    public Set<RuntimeTaskInfo> listActive() {

        Set<RuntimeTaskInfo> activeTaskInfos = new HashSet<RuntimeTaskInfo>();
        synchronized (activeTasks) {
            for (ActiveTask activeTask : activeTasks.values()) {
                activeTaskInfos.add(activeTask.getInfo());
            }
        }

        return activeTaskInfos;
    }


    @Override
    protected void finalize() throws Throwable {
        shutdownNow();
    }

    public void shutdownNow() {
        synchronized (activeTasks) {
            Set<String> tasksToStop = new HashSet<String>(activeTasks.keySet());
            for (String task : tasksToStop) {
                try {
                    stop(task);
                } catch (TaskingException e) {
                    //
                }
            }
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
