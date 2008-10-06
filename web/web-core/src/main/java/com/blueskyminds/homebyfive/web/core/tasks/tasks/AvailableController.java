package com.blueskyminds.homebyfive.web.core.tasks.tasks;

import com.blueskyminds.homebyfive.framework.core.tasks.AvailableTask;
import com.blueskyminds.homebyfive.framework.core.tasks.RuntimeTaskInfo;
import com.blueskyminds.homebyfive.framework.core.tasks.service.TaskingService;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.thoughtworks.xstream.XStream;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

/**
 * List available tasks
 *
 * Date Started: 24/06/2008
 * <p/>
 * History:
 */
public class AvailableController extends ActionSupport implements ModelDriven<List<AvailableTask>> {

    private TaskingService taskingService;
    private List<AvailableTask> tasks;
    private Set<RuntimeTaskInfo> activeTasks;
    private int taskCount;

    public HttpHeaders index() {
        tasks = taskingService.listAvailable();
        activeTasks = taskingService.listActive();
        List<AvailableTask> toRemove = new LinkedList<AvailableTask>();
        // filter out the tasks that are currently active
        for (AvailableTask availableTask : tasks) {
            for (RuntimeTaskInfo activeTask : activeTasks) {
                if (activeTask.getKey().equals(availableTask.getKey())) {
                    toRemove.add(availableTask);
                }
            }
        }
        tasks.removeAll(toRemove);
        taskCount = tasks.size();
        return new DefaultHttpHeaders(ActionSupport.SUCCESS).lastModified(new Date());
    }

    /**
     * Gets the model to be pushed onto the ValueStack instead of the Action itself.
     *
     * @return the model
     */
    public List<AvailableTask> getModel() {
        return tasks;
    }

    public int getTaskCount() {
        return taskCount;
    }

    @Inject
    public void setTaskingService(TaskingService taskingService) {
        this.taskingService = taskingService;
    }


    public String toXML(Object status) {
        return new XStream().toXML(status);
    }

    public Date getNow() {
        return new Date();
    }
}