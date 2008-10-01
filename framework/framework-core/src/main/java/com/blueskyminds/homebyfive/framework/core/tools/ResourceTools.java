package com.blueskyminds.homebyfive.framework.core.tools;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.net.*;

/**
 * Common interfaces to configuration data
 *
 * Configuration has application scope.
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class ResourceTools {

    private static final Log LOG = LogFactory.getLog(ResourceTools.class);

    // ------------------------------------------------------------------------------------------------------

    private static final String ROOT = "/";
    public static final String DEFAULT_RUNTIME_CONF_PATH = "conf";
    public static final String DEFAULT_RESOURCE_CONF_PATH = "resources";
    public static final String DEFAULT_DEV_RESOURCES_PATH = "src/main/resources";
    public static final String DEFAULT_DEV_TEST_RESOURCES_PATH = "src/test/resources";

    /** A singleton instance of the configuration used to cache properties */
    private static ResourceTools soleInstance;

    private List<String> modulePaths;
    private List<String> searchPaths;

    /** If this flag is true, then development paths are added to the search paths */
    private boolean developmentMode = false;
    private static final String IDEA_RT_JAR = "idea_rt.jar";

    // ------------------------------------------------------------------------------------------------------

    private ResourceTools() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String UP_DIR = "..";

    /**
     * Determines whether currently running in a development environment or a production
     * environment.
     *
     * In the development environment, additional search paths are enabled (to load files from source directories)
     *
     * This implementation only detects the presence of IntelliJ IDEA.
     */
    private void evaluateDeploymentMode() {
        modulePaths = new LinkedList<String>();

        ClassPath cp = new ClassPath();
        if (cp.includesJar(IDEA_RT_JAR)) {
            developmentMode = true;

            // search for other development modules in the classpath
            // get a list of the directories in the last path that are derived from module dependencies
            List<String> dirs = cp.getDirsContainingPattern("target"+ File.separator+"classes");
            for (String dir : dirs) {
                File moduleDir = new File(dir+File.separator+UP_DIR+File.separator+UP_DIR);
                if (moduleDir.exists()) {
                    try {
                        modulePaths.add(moduleDir.getCanonicalPath());
                    } catch(IOException e) {
                        // doesn't exit
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the configuration with default attributes */
    private void init() {
        searchPaths = new LinkedList<String>();

        evaluateDeploymentMode();
        defineSearchPath("");  // current path
        defineSearchPath(ROOT);
        defineSearchPath(DEFAULT_RUNTIME_CONF_PATH);
        defineSearchPath(ROOT+DEFAULT_RUNTIME_CONF_PATH);
        defineSearchPath(DEFAULT_RESOURCE_CONF_PATH);
        if (developmentMode) {
            defineSearchPath(DEFAULT_RESOURCE_CONF_PATH);
            defineSearchPath(DEFAULT_DEV_RESOURCES_PATH);
            defineSearchPath(DEFAULT_DEV_TEST_RESOURCES_PATH);

            if (modulePaths.size() > 0) {
                for (String modulePath : modulePaths) {
                    // add search paths for all the modules
                    defineSearchPath(modulePath+File.separator+DEFAULT_DEV_RESOURCES_PATH);
                    defineSearchPath(modulePath+File.separator+DEFAULT_DEV_TEST_RESOURCES_PATH);
                }
            }

        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Define a path to search for configuration */
    private void defineSearchPath(String path) {
        searchPaths.add(path);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the classLoader:
     *   Returns the thread context classloader, if available; or
     * the classloader that loaded this class
     * @return ClassLoader
     */
    protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			// No thread context class loader so  use class loader of this class.
			classLoader = getClass().getClassLoader();
		}
		return classLoader;
	}

    // ------------------------------------------------------------------------------------------------------

    /** Searches for a file of the specified name in the classpath of the application.
     * @return the URL to all instances found */
    private List<URI> findInClassPath(String name) throws IOException {
        Iterator<String> iterator = searchPaths.iterator();
        boolean absolute = false;
        List<URI> foundURLs = new LinkedList<URI>();
        URI foundURL = null;

        if (name != null) {

            if ((name.startsWith("/")) || (name.startsWith("\\"))) {
                absolute = true;
            }

            if (!absolute) {
                // search all of the search paths within the context of the classpath
                while (iterator.hasNext()) {
                    String nextPath = iterator.next();
                    if ((nextPath.length() > 0) && (!nextPath.endsWith("/"))) {
                        // add the separator suffix (if not intended to be the 'current' dir)
                        nextPath = nextPath+"/";
                    }

                    Enumeration<URL> trialURLs = getClassLoader().getResources(nextPath+name);

                    if (trialURLs != null) {
                        while (trialURLs.hasMoreElements()) {
                            foundURL = toURI(trialURLs.nextElement());
                            // add the URL if it's not already included
                            if (!foundURLs.contains(foundURL)) {
                                foundURLs.add(foundURL);
                            }
                        }
                    }
                }
            }
            else {
                // the path is abolute, so only try it directly
                Enumeration<URL> trialURLs = getClassLoader().getResources(name);

                if (trialURLs != null) {
                    while (trialURLs.hasMoreElements()) {
                        foundURL = toURI(trialURLs.nextElement());

                        // add the URL if it's not already included
                        if (!foundURLs.contains(foundURL)) {
                            foundURLs.add(foundURL);
                        }
                   }
                }
            }
        }

        return foundURLs;
    }



    // ------------------------------------------------------------------------------------------------------

    /** Searches for a file of the specified name in the searchpath of the filesystem */
    private List<URI> findInFileSystem(String name) {
        Iterator<String> iterator = searchPaths.iterator();
        List<URI> foundURLs = new LinkedList<URI>();
        URI foundURL = null;

        if (name != null) {
            if (!FileTools.isAbsolutePath(name)) {
                // search all of the search paths for the filename
                while (iterator.hasNext()) {
                    try {
                        String nextPath = iterator.next();
                        String trialPath = FileTools.concatenateCanonicalPath(nextPath, name);

                        File trialFile = new File(trialPath);
                        // check if the file exists
                        if (trialFile.exists()) {
                            foundURL = toURI(trialFile.toURL());

                            // add the url to the file if it hasn't already been encountered
                            if (!foundURLs.contains(foundURL)) {
                                foundURLs.add(foundURL);
                            }
                        }
                    }
                    catch (MalformedURLException e) {
                        // try next search path
                    }
                    catch (IOException e) {
                        // try next search path
                    }
                }
            }
            else {
                // if it's an abolute filename there's no option but to try the abolute path
                try {
                    File trialFile = new File(name);
                    if (trialFile.exists()) {
                        foundURL = toURI(trialFile.toURL());
                        foundURLs.add(foundURL);
                    }
                }
                catch (MalformedURLException e) {
                    // try next search path
                }
                catch (IOException e) {
                    // try next search path
                }
            }
        }

        return foundURLs;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a URL to the resource with the given named, searched for in the precedence of the search
     * path (local file system and then the class path).
     *
     *
     * @param name
     * @return URL to the resource (the FIRST encountered instance of the resource)
     */
    public static URI locateResource(String name) {
        URI[] candidateUrls = locateResources(name);

        // this method only needs to return the first result
        if (candidateUrls.length > 0) {
            return candidateUrls[0];
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a URL to the resource in the classpath
     * @param name
     * @return
     */
    public static URI[] locateResourcesInClasspath(String name) {
        List<URI> candidateUrls = new LinkedList<URI>();

        try {
            candidateUrls.addAll(instance().findInClassPath(name));
        } catch (IOException e) {
            // ignore the IO exception - means there was a problem searching the classpath
        }

        URI[] urls = new URI[candidateUrls.size()];
        return candidateUrls.toArray(urls);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a URL to the resource in the classpath
     * @param name
     * @return returns the first found instance, or null if not found
     */
    public static URI locateResourceInClasspath(String name) {
        URI[] candidateUrls = locateResourcesInClasspath(name);

        if (candidateUrls.length > 0) {
            return candidateUrls[0];
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns an array of URLs to the resources with the given name, searched for in the precedence of the search
     * path (local file system and then the class path).
     *
     * Multiple results are returned if multiple instances of the named resource are found.
     *
     * @param name
     * @return An array of all the URLs to the resource(s) with the given name
     */
    public static URI[] locateResources(String name) {
        List<URI> candidateUrls = new LinkedList<URI>();

        try {
            candidateUrls.addAll(instance().findInFileSystem(name));
            candidateUrls.addAll(instance().findInClassPath(name));
        } catch (IOException e) {
            // ignore the IO exception - means there was a problem searching the classpath
        }

        URI[] urls = new URI[candidateUrls.size()];
        return candidateUrls.toArray(urls);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a URL to the resource identified by the VALUE of the given property
     *
     * @param key of the property identifying the resource name
     * @return URL to the resource
     */
    public static URI locateResourceByProperty(String key, PropertiesContext propertiesContext) {
        return locateResource(propertiesContext.getProperty(key));
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Loads an instance of the configuration with the given arguments
     * @param resourceTools
     */
    private static void load(ResourceTools resourceTools) {
        soleInstance = resourceTools;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Ensure an instance of the Configuration has been initialised */
    private static void forceInit() {
        if (soleInstance == null) {
           load(new ResourceTools());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the configuration */
    private static ResourceTools instance() {
        forceInit();
        return soleInstance;
    }
   
    // ------------------------------------------------------------------------------------------------------

    /** Returns a ';' seperated list of the paths that are searched for a resource */
    public static String printSearchPaths() {
        return StringUtils.join(instance().searchPaths.iterator(), ";");
    }

    // ------------------------------------------------------------------------------------------------------

    /** Open an InputStream to the named resource located by this ResourceLocator */
    public static InputStream openStream(String resourceName) throws IOException {
        URI uri = locateResource(resourceName);
        if (uri != null) {
            return openStream(uri);
        } else {
            throw new FileNotFoundException("Could not locate resource: "+resourceName);
        }
    }

     public static InputStream openStream(URI uri) throws IOException {
        if (uri != null) {
            return uri.toURL().openStream();
        } else {
            return null;
        }
    }

    public static URLConnection openConnection(URI uri) throws IOException {
        if (uri != null) {
            return uri.toURL().openConnection();
        } else {
            return null;
        }
    }


    public static String externalForm(URI uri) throws MalformedURLException {
        return uri.toURL().toExternalForm();
    }

    /** Convert a URL to a URI and swallow syntax exceptions */
    public static URI toURI(URL url) {
        try {
            if (url != null) {
                return url.toURI();
            }
        } catch (URISyntaxException e) {
            // ignored
        }
        return null;
    }

    /** Convert a URI to a URL and swallow malformed exceptions */
    public static URL toURL(URI uri) {
        try {
            if (uri != null) {
                return uri.toURL();
            }
        } catch (MalformedURLException e) {
            // ignored
        }
        return null;
    }
}
