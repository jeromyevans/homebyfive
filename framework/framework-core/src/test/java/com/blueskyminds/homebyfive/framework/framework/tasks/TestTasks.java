package com.blueskyminds.homebyfive.framework.framework.tasks;

import com.blueskyminds.homebyfive.framework.framework.test.DbTestCase;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests the tasks package of the framework
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestTasks extends DbTestCase {

    private static final Log LOG = LogFactory.getLog(TestTasks.class);

    public TestTasks(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------------------

    /** A task that completes almost immediately */
    private class TestTask extends SimpleTask {

        public TestTask(String name) {
            super(name);
        }

        public boolean process() {
            LOG.info("Ran "+getName());
            return true;
        }

    }

    // ------------------------------------------------------------------------------------------------------

    /** Contains memento that need to be persisted */
    /*private class MySettings extends TaskMemento {

        private int duration;

        public MySettings(int duration) {
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }*/

    /** A task that sleeps for a variable amount of time */
    /*
    public class VariableLengthTask extends SimpleTask {

        public VariableLengthTask() {
        }

        public VariableLengthTask(int taskId) {
            super("Task#"+taskId, new MySettings(RandomTools.randomInt(1, 5)));
        }

        public boolean process() {
            boolean complete = false;
            LOG.info("Runing "+getNamed()+" for "+getDuration()+" seconds");
            try {
                Thread.sleep(getDuration()*1000);
                complete = true;
            } catch(InterruptedException e) {
                LOG.info("   "+getNamed()+" sleep interrupted");
            }
            return complete;
        }

        public int getDuration() {
            return ((MySettings) getMemento()).getDuration();
        }
    }*/

    // ------------------------------------------------------------------------------------------------------

    /** Creates a bunch of short tasks and runs them all.  The tasks will be run in arbitary order as
     * determined by the executor.  The number of current tasks will be determined by the executor
     */
    public void testTaskList() {
        int count = 50;
        List<Task> taskList = new LinkedList<Task>();

        // create some tasks
        for (int taskNo = 0; taskNo < count; taskNo++) {
            taskList.add(new TestTask("Task#"+taskNo).asTask());
        }

        // create a top-level group
        TaskGroup all = new TaskGroup("All");
        all.addAll(taskList);

        // run the top-level task - this will block until all are complete
        all.start();

        assertTrue(all.isComplete());

    }

    /** Creates a bunch of variable length tasks and runs them all.  The tasks will be run in arbitary order as
     * determined by the executor.  The number of current tasks will be determined by the executor
     */
//    public void testTaskList2() {
//        int count = 20;
//        int totalDuration = 0;
//        List<Task> taskList = new LinkedList<Task>();
//
//        // create some tasks
//        for (int taskNo = 0; taskNo < count; taskNo++) {
//            VariableLengthTask v = new VariableLengthTask(taskNo);
//            totalDuration += v.getDuration();
//            taskList.add(v.asTask());
//        }
//
//        // create a top-level group
//        TaskGroup all = new TaskGroup("All");
//        all.addAll(taskList);
//
//        // run the top-level task - this will block until all tasks are complete
//        long startTime = System.currentTimeMillis();
//        all.start();
//        long endTime = System.currentTimeMillis();
//        int executionTime = (int) (endTime - startTime) / 1000;
//
//        assertTrue(all.isComplete());
//
//        LOG.info("Total execution time: "+totalDuration+" seconds");
//        LOG.info("Executed in: "+executionTime+" seconds");
//    }

    /** Creates a bunch of variable length tasks with some dependencies between them.  Runs them all.
     */
//    public void testTaskDependencies() {
//        int group1Count = 10;
//        int group2Count = 10;
//        int totalDuration = 0;
//
//        // create a top-level group
//        TaskGroup all = new TaskGroup("all");
//        TaskGroup group1 = new TaskGroup("Group1");
//        TaskGroup group2 = new TaskGroup("Group2");
//        all.addTask(group1);
//        all.addTask(group2);
//        group1.addDependency(group2);
//
//        // create some tasks for group 1
//        for (int taskNo = 0; taskNo < group1Count; taskNo++) {
//            group1.addTask(new VariableLengthTask(taskNo).asTask());
//        }
//
//        // create some tasks for group 2
//        for (int taskNo = 0; taskNo < group2Count; taskNo++) {
//            group2.addTask(new VariableLengthTask(group1Count+taskNo).asTask());
//        }
//
//        LOG.info("Group 1: "+group1Count+" tasks");
//        LOG.info("Group 2: "+group2Count+" tasks");
//        LOG.info("Group1 is dependent on completion of group2");
//
//        // run the top-level task - this will block until all tasks are complete
//        long startTime = System.currentTimeMillis();
//        all.start();
//        long endTime = System.currentTimeMillis();
//        int executionTime = (int) (endTime - startTime) / 1000;
//
//        assertTrue(all.isComplete());
//
//        LOG.info("Total execution time: "+totalDuration+" seconds");
//        LOG.info("Executed in: "+executionTime+" seconds");
//    }

    /** Creates a bunch of variable length tasks, runs them and invokes an asynchronous shutdown
     */
//    public void testTaskShutown() {
//        int count = 20;
//        List<Task> taskList = new LinkedList<Task>();
//
//        // create some tasks
//        for (int taskNo = 0; taskNo < count; taskNo++) {
//            taskList.add(new VariableLengthTask(taskNo).asTask());
//        }
//
//        // create a top-level group
//        TaskGroup all = new TaskGroup("All");
//        all.addAll(taskList);
//
//        // start running the tasks asynchronously
//        TaskCoordinator.runInBackground(all);
//
//        try {
//            LOG.info("Sleeping for 4 seconds...");
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            //
//        }
//
//        LOG.info("Shutdown...");
//        // now attempt to shutdown
//        assertTrue(all.shutdown());
//
//        try {
//            LOG.info("Sleeping for 10 seconds...");
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            //
//        }
//        LOG.info("Finished");
//    }

    /** Creates a bunch of tasks for testing, and persists them */
//    private void initialiseTestTasks(int count) {
//        int tasksCreated = 0;
//        int groupsCreated = 0;
//        Task task;
//
//        TaskGroup lastGroup;
//        TaskGroup all = new TaskGroup("All");
//
//        // add a bunch of tasks to the root task
//        lastGroup = all;
//        while (tasksCreated < count) {
//            // one in five tasks will be a group
//            if (RandomTools.randomInt(0, 4) == 0) {
//                task = new TaskGroup("Group"+groupsCreated++);
//                lastGroup.addTask(task);
//                lastGroup = (TaskGroup) task;
//            } else {
//                lastGroup.addTask(new VariableLengthTask(tasksCreated++).asTask());
//
//                // on occasions, drop back up to the top
//                if (RandomTools.randomInt(0, 3) == 0) {
//                    lastGroup = all;
//                }
//            }
//        }
//
//        // now persist the tasks
//        PersistenceService gateway = getPersistenceService();
//        try {
//            gateway.save(taskPlan);
//        } catch (PersistenceServiceException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

//    public void testTaskPersistence() {
//        initialiseTestTasks(2);
//
//        PersistenceService gateway = getPersistenceService();
//
//        TestTools.printAll(Task.class, gateway);
//
//
//        try {
//            List<Task> allTasks = gateway.findAll(Task.class);
//            assertNotNull(allTasks);
//        } catch(PersistenceServiceException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

//    public void testTaskPlan() {
//        initialiseTestTasks(30);
//
//        PersistenceService gateway = getPersistenceService();
//        try {
//            PersistenceSession session = gateway.openSession();
//            List<TaskPlan> allTaskPlans = gateway.findAll(TaskPlan.class);
//            assertNotNull(allTaskPlans);
//
//            LOG.info("--- executing task plans ---");
//            for (TaskPlan taskPlan : allTaskPlans) {
//                taskPlan.start();
//            }
//            session.close();
//        } catch(PersistenceServiceException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

}
