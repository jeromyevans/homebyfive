package com.blueskyminds.framework.persistence.spooler;

/**
 * A Spooler is used to spool Pages of data from persistence into a processing task
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Spooler {

    /**
     * Start the spooler process:
     *     - retrieve domain objects from persistence and commence processing them
     */
    void start();
}
