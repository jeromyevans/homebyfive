package com.blueskyminds.framework.tools;

//import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.net.URI;

/**
 * Tools to setup/configure the logging engine
 *
 * Date Started: 25/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class LoggerTools {

    public static final String DEFAULT_CONFIG_FILENAME = "log4j.xml";

    private static boolean configuredDefault = false;

    // ------------------------------------------------------------------------------------------------------

    /** Determine if the specified file exists in the local file system */
    private static boolean localFileExists(String fileName) {
        try {
            File file = new File(fileName);
            return file.exists();
        }
        catch (Exception e) {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Configures the logging engine using config with the default filename
     *
     * See configure(String configFileName)
     *
     * If this method has already been called the request is ignored.
     */
    public static void configure() {
        if (!configuredDefault) {
            configure(DEFAULT_CONFIG_FILENAME);
        }
        configuredDefault = true;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Configures the logging engine using the specified logging config filename.
     * Searches all of the Configuration source paths for the resource */
    public static void configure(String configFileName) {        
        if (configFileName == null) {
            configFileName = DEFAULT_CONFIG_FILENAME;
        }

        URI configFileUrl = ResourceTools.locateResource(configFileName);

        if (configFileUrl != null) {
            configureLogger(configFileUrl);
            System.out.println("Logger using configuration: "+configFileUrl);
        } else {
            System.out.println("Logger configuration not found.  Searched for '"+configFileName+"' in "+ ResourceTools.printSearchPaths());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Configure and watch the specified file */
    private static void configureLogger(String configFile) {
   //     DOMConfigurator.configureAndWatch(configFile);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Configure using a URL - don't watch */
    private static void configureLogger(URI configFile) {
   //     DOMConfigurator.configure(ResourceTools.toURL(configFile));
    }
}
