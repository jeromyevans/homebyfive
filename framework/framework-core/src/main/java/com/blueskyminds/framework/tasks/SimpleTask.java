package com.blueskyminds.framework.tasks;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import com.blueskyminds.framework.memento.XMLMemento;

/**
 * An abstract Task implementation that implements most of the requirements for the Task interface, allowing
 *  the basic task memento to be persisted without the implementor needing to understand the persistence
 *  mechanism.
 *
 * Tasks that extend SimpleTask just need to implement the process() method, returning true if complete.
 *  They may opt to override the other methods though.
 *
 * An important aspect of SimpleTask is that is is NOT actually a Task, which means the persistence
 *  mechanism does not need to know about it and its extensions.  A Task instance is created with the
 *  toTask() method, which creates a Task pointing back to this 'Taskable' implementation
 *
 * In effect, each instance of a SimpleTask results in a Task entity. The className of the
 *  implementation of the SimpleTask is persisted and invoked via reflection.
 *
 * Extend and set the XMLMemento if the SimpleTask needs to persist memento.
 *
 * Shutdown and reset have no effect in the default implementation.
 *
 * Date Started: 1/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class SimpleTask implements Taskable {

    private static final Log LOG = LogFactory.getLog(SimpleTask.class);
    private String name;
    private boolean complete;
    private XMLMemento memento;

    // ------------------------------------------------------------------------------------------------------

    protected SimpleTask() {
        this.name = "Anonymous";
        init();
    }

    protected SimpleTask(String name) {
        this.name = name;
        init();
    }

    protected SimpleTask(String name, XMLMemento memento) {
        this.name = name;
        this.memento = memento;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SimpleTask with default attributes
     */
    private void init() {
        complete = false;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Executes the process (intended to block) */
    public void start(Task envelope) {
        LOG.info("Starting SimpleTask ("+getName()+")");
        setComplete(process());
    }

    // ------------------------------------------------------------------------------------------------------

    /** Implement an operation here.
     *
     * @return true if completed
     */
    public abstract boolean process();

    // ------------------------------------------------------------------------------------------------------

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /** The default shutdown implementation exits immediately, doing nothing */
    public boolean shutdown() {
        return true;
    }

    /** The default reset implementation clears the complete flag and exits immediately */
    public boolean reset() {
        setComplete(false);
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XMLMemento getMemento() {
        return this.memento;
    }

    public void setMemento(XMLMemento memento) {
        this.memento = memento;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return this SimpleTask as an actual Task implementation. */
    public Task asTask() {
        return Task.newInstance(this, memento);
    }

    /**
     * No status available for a SimpleTask.
     *
     * Override this method to provide useful information
     *
     * @return
     */
    public XMLMemento getRuntimeStatus() {
        return null;
    }
}
