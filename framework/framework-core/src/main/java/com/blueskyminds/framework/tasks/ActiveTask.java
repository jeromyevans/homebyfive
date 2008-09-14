package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.tasks.service.AsynchronousTaskInvocation;

import java.util.concurrent.Future;
import java.util.Date;

/**
 * Holds information about a task that's currently active
 *
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class ActiveTask {

    private String key;
    private final AsynchronousTaskInvocation asynchronousTaskInvocation;
    private final Future future;

    public ActiveTask(String key, AsynchronousTaskInvocation asynchronousTaskInvocation, Future future) {
        this.key = key;
        this.asynchronousTaskInvocation = asynchronousTaskInvocation;
        this.future = future;
    }

    public AsynchronousTaskInvocation getAsynchronousTaskInvocation() {
        return asynchronousTaskInvocation;
    }

    public Future getFuture() {
        return future;
    }

    public boolean isRunning() {
        return future != null && !future.isCancelled() && !future.isDone();
    }

    /** Gets TaskInfo describing this task and it children */
    public RuntimeTaskInfo getInfo() {
        CompositeRuntimeTaskInfo root = new CompositeRuntimeTaskInfo(key);
        TaskCoordinator taskCoordinator = asynchronousTaskInvocation.getTaskCoordinator();
        if (taskCoordinator != null) {
            root.addAll(taskCoordinator.list());
        } else {
            if (asynchronousTaskInvocation.isErrors()) {
                root.add(new SimpleRuntimeTaskInfo(key, new Date(), AsynchronousTaskStatus.Failed, asynchronousTaskInvocation.getLastException()));
            } else {
                root.add(new SimpleRuntimeTaskInfo(key, new Date(), AsynchronousTaskStatus.Failed, null));
            }
        }
        return root;
    }
}
