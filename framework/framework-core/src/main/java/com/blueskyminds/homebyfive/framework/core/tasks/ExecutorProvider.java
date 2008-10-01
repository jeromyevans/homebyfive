package com.blueskyminds.homebyfive.framework.core.tasks;

import java.util.concurrent.Executor;

/**
 * An interface to a provider of an Executor that the TaskCoordinator(s) can use
 *
 * Date Started: 8/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface ExecutorProvider {

    /** Get the executor instance to use */
    Executor getExecutor();
}
