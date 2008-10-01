package com.blueskyminds.homebyfive.framework.core.tasks;

import com.blueskyminds.homebyfive.framework.core.tasks.service.TaskingService;
import com.blueskyminds.homebyfive.framework.core.tasks.service.TaskingServiceImpl;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

/**
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class TestTaskingService extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestTaskingService.class);

    private TaskingService taskingService;
    protected void setUp() throws Exception {
        super.setUp();
        taskingService = new TaskingServiceImpl();
    }

    public void testTaskingService() throws Exception {

        taskingService.registerTask("TaskA", VariableLengthTask.class).
                withParameter("taskId", 1).
                withParameter("minSecs", 5).
                withParameter("maxSecs", 7);

        taskingService.registerTask("TaskB", VariableLengthTask.class).
                withParameter("taskId", 2).
                withParameter("minSecs", 5).
                withParameter("maxSecs", 7);


        assertEquals(2, taskingService.listAvailable().size());
        LOG.info("Listing availble tasks: ");
        DebugTools.printCollection(taskingService.listAvailable(), false);
        
        taskingService.start("TaskA");
        taskingService.start("TaskB");
        
        LOG.info("Sleeping for 2 seconds");
        Thread.sleep(2000);
        LOG.info("Listing current tasks (should be running): ");
        DebugTools.printCollection(taskingService.listActive(), false);
        LOG.info("Sleeping for 6 seconds");
        Thread.sleep(6000);
        LOG.info("Listing current tasks (should be complete): ");
        DebugTools.printCollection(taskingService.listActive(), false);
        taskingService.stop("TaskB");
        taskingService.stop("TaskA");
        LOG.info("Sleeping for 2 seconds");
        Thread.sleep(2000);
        LOG.info("Listing current tasks (should be gone): ");
        DebugTools.printCollection(taskingService.listActive(), false);
    }

     public void testIterativeTask() throws Exception {

        TaskingService taskingService = new TaskingServiceImpl();

        taskingService.registerTask("TaskA", LongIterativeTaskExample.class);

        assertEquals(1, taskingService.listAvailable().size());
        LOG.info("Listing availble tasks: ");
        DebugTools.printCollection(taskingService.listAvailable(), false);
        
        taskingService.start("TaskA");

        LOG.info("Sleeping for 6 seconds");
        Thread.sleep(6000);
        LOG.info("Listing current tasks: ");
        DebugTools.printCollection(taskingService.listActive(), false);
         
        taskingService.stop("TaskA");
        LOG.info("Sleeping for 2 seconds");
        Thread.sleep(2000);
        LOG.info("Listing current tasks (should be gone): ");
        DebugTools.printCollection(taskingService.listActive(), false);
    }
}
