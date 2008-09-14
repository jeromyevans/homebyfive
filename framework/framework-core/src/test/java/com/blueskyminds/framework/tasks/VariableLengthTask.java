package com.blueskyminds.framework.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.framework.tools.RandomTools;

import java.util.Date;

/**
 * An AsynchronusTask that sleeps for a variable amount of time
 *
 * Date Started: 1/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class VariableLengthTask implements AsynchronousTask {

    private static final Log LOG = LogFactory.getLog(VariableLengthTask.class);

    private String key;
    private int taskId;
    private int duration;
    private int minSecs;
    private int maxSecs;
    private AsynchronousTaskStatus status;

    public VariableLengthTask() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getMinSecs() {
        return minSecs;
    }

    public void setMinSecs(int minSecs) {
        this.minSecs = minSecs;
    }

    public int getMaxSecs() {
        return maxSecs;
    }

    public void setMaxSecs(int maxSecs) {
        this.maxSecs = maxSecs;
    }

    public boolean reset() {
        duration = RandomTools.randomInt(minSecs, maxSecs);
        return true;
    }

    public boolean isReadyToStart() {
        return true;
    }

    public void startBlocking() throws Exception {
        status = AsynchronousTaskStatus.Running;

        LOG.info("Running for "+duration+" seconds");
        try {
            Thread.sleep(duration*1000);
            status = AsynchronousTaskStatus.Complete;
        } catch(InterruptedException e) {
            LOG.info("  sleep interrupted");
            status = AsynchronousTaskStatus.Interrupted;
        }                
    }

    public boolean pause() {
        return false;
    }

    public boolean unpause() {
        return false;
    }

    public boolean stop() {
        return false;
    }

    public AsynchronousTaskStatus getStatus() {
        return status;
    }

    public RuntimeTaskInfo getInfo() {
        return new SimpleRuntimeTaskInfo(key, new Date(), status, duration);
    }

}