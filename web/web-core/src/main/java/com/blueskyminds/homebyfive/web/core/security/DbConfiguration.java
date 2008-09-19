package com.blueskyminds.homebyfive.web.core.security;

import javax.security.auth.login.Configuration;
import javax.security.auth.login.AppConfigurationEntry;

/**
 * JAAS configuration for authentication via a database
 *
 * Date Started: 13/08/2007
 * <p/>
 * History:
 */
public class DbConfiguration extends Configuration {
    
    /**
     * Retrieve the AppConfigurationEntries for the specified <i>name</i>
     * from this Configuration.
     * <p/>
     * <p/>
     *
     * @param name the name used to index the Configuration.
     * @return an array of AppConfigurationEntries for the specified <i>name</i>
     *         from this Configuration, or null if there are no entries
     *         for the specified <i>name</i>
     */
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        return new AppConfigurationEntry[0];
    }

    /**
     * Refresh and reload the Configuration.
     * <p/>
     * <p> This method causes this Configuration object to refresh/reload its
     * contents in an implementation-dependent manner.
     * For example, if this Configuration object stores its entries in a file,
     * calling <code>refresh</code> may cause the file to be re-read.
     * <p/>
     * <p/>
     *
     * @throws SecurityException if the caller does not have permission
     *                           to refresh its Configuration.
     */
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
