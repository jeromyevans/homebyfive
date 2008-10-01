package com.blueskyminds.homebyfive.framework.core.tasks.service;

/**
 *
 * Indicates an exception occured trying to control a Task
 *
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class TaskingException extends Exception {

    public TaskingException() {
    }

    public TaskingException(String message) {
        super(message);
    }

    public TaskingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskingException(Throwable cause) {
        super(cause);
    }
}
