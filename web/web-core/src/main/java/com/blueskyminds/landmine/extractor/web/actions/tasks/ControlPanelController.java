package com.blueskyminds.landmine.extractor.web.actions.tasks;

import com.blueskyminds.framework.tasks.service.TaskingException;
import com.blueskyminds.framework.tasks.service.TaskingService;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.NullResult;

import java.util.Map;
import java.util.HashMap;

/**
 * Asynchronous Task Controller
 *
 * Date Started: 28/05/2008
 * <p/>
 * History:
 */
@Result(name="result", type= NullResult.class, value = "")
public class ControlPanelController extends ActionSupport {

    private static final Log LOG = LogFactory.getLog(ControlPanelController.class);

    private TaskingService taskingService;

    private String id;

    private Map<String, String> params = new HashMap<String, String>();

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String update() {
        String result = "result";
        LOG.info("Starting "+ id);
        try {
            Map<String, Object> runParams = new HashMap<String, Object>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                runParams.put(entry.getKey(), entry.getValue());
            }
            taskingService.start(id, runParams);

            LOG.info(id + " started okay");
        } catch (TaskingException e) {
            LOG.error("Could not start the "+ id +" task", e);
        }
        return result;
    }

    public String destroy() {
        String result = "result";
        LOG.info("Stopping "+ id);
        try {
            taskingService.stop(id);

            LOG.info(id + " stopped okay");
        } catch (TaskingException e) {
            LOG.error("Could not stop the "+ id +" task", e);
        }
        return result;
    }

    @Inject
    public void setTaskingService(TaskingService taskingService) {
        this.taskingService = taskingService;
    }

}