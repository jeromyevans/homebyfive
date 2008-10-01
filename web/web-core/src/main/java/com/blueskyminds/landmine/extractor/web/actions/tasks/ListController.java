package com.blueskyminds.landmine.extractor.web.actions.tasks;

import com.blueskyminds.homebyfive.framework.core.tasks.RuntimeTaskInfo;
import com.blueskyminds.homebyfive.framework.core.tasks.service.TaskingService;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.thoughtworks.xstream.XStream;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import java.util.Date;
import java.util.Set;

/**
 * List active tasks
 *
 * Date Started: 28/06/2008
 * <p/>
 * History:
 */
public class ListController extends ActionSupport implements ModelDriven<Set<RuntimeTaskInfo>> {

    private TaskingService taskingService;
    private Set<RuntimeTaskInfo> tasks;
    private int taskCount;

    public HttpHeaders index() {
        tasks = taskingService.listActive();
        taskCount = tasks.size();
        return new DefaultHttpHeaders(ActionSupport.SUCCESS).disableCaching();
    }

    /**
     * Gets the model to be pushed onto the ValueStack instead of the Action itself.
     *
     * @return the model
     */
    public Set<RuntimeTaskInfo> getModel() {
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