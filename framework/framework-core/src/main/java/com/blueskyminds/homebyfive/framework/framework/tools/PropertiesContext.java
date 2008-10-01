package com.blueskyminds.homebyfive.framework.framework.tools;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.util.*;
import java.io.InputStream;
import java.io.IOException;

/**
 * A set of properties that have been merged, prioritised and had their cross-references resolved
 *
 * Date Started: 26/05/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertiesContext {

    private static final Log LOG = LogFactory.getLog(PropertiesContext.class);

    /** Properties that have been previously loaded and cached in the configuration */
    private Map<String, Properties> propertyCache;
    private List<String> precedence;

    /**
     * Create a new empty properties context
     */
    public PropertiesContext() {
        init();
    }

    /**
     * Create a new Properties context using the specified file for the initial values
     *
     * @param defaultPropertiesFile
     */
    public PropertiesContext(String defaultPropertiesFile) {
        init();
        loadProperties(defaultPropertiesFile);
    }

    /**
     * Create a new Properties context using the specified file for the initial values
     *
     * @param defaultPropertiesFiles    - properties files to read, first to last, latter files override former
     */
    public PropertiesContext(String... defaultPropertiesFiles) {
        init();
        if (defaultPropertiesFiles != null) {
            for (String file : defaultPropertiesFiles) {
                loadProperties(file);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertiesContext with default attributes
     */
    private void init() {
        propertyCache = new HashMap<String, Properties>();
        precedence = new LinkedList<String>();
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds the specified properties to the context.
     * These properties take highest precedence
     *
     * @param key   - a string used to uniquely identify this property set.  If the properties are loaded
     *  from a file the URL is a good.  If the properties have been set programatically an appropriate
     *  key for the set will need to be generated (if the key exists the existing property set is replaced)
     * @param properties - one or more properties in a set
     **/
    private void cacheProperties(String key, Properties properties) {
        if (precedence.contains(key)) {
            precedence.remove(key);
        }
        precedence.add(0, key);  // add to the top
        propertyCache.put(key, properties);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to load the properties with the specified name using the default ResourceLocator
     *
     * @return the URL to the resource that was loaded to get the properties, or null if not found
     **/
    private URI loadPropertiesResource(String name) {
        Properties properties;
        URI resourceURL = null;

        try {
            // search for the properties file
            resourceURL = ResourceTools.locateResource(name);

            if (resourceURL != null) {
                InputStream in = ResourceTools.openStream(resourceURL);
                if (in != null) {
                    properties = new Properties();
                    properties.load(in);

                    // cache the properties (use the URL as the key)
                    cacheProperties(ResourceTools.externalForm(resourceURL), properties);
                }
            }
        }
        catch (IOException e) {
            // error reading
        }

        return resourceURL;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the property with the specified name
     *
     * The value with highest precedence is returned, or null if it's not found
     *
     **/
    private String lookupProperty(String key) {
        // loop through all the urls in the cache
        for (String index : precedence) {
            Properties properties = propertyCache.get(index);
            if (properties.containsKey(key)) {
                return properties.getProperty(key);
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the property with the specified key
     *
     * The value with highest precedence is returned, or null if it's not found
     *
     */
    public String getProperty(String key) {
        return lookupProperty(key);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the property with the specified key.
     * Returns the defaultValue if the property is not found
     */
    public String getProperty(String key, String defaultValue) {
        String value = lookupProperty(key);
        return (value != null ? value : defaultValue);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the property with the specified key where the values are comma separated
     * Whitespace is trimmed from the values
     *
     * @return list of values for the property
     */
    public String[] getPropertyCSV(String key) {
        String value = lookupProperty(key);
        String[] values;

        values = StringUtils.split(value, ',');
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }

        return values;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to load the specified properties file into the context
     */
    public boolean loadProperties(String fileName) {
        URI url = loadPropertiesResource(fileName);
        if (url != null) {
            LOG.info("Loaded properties: " + url);
            return true;
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Loads a Properties file using the default ResourceLocator
     *
     * @param fileName  name of the properties file
     * @return properties set
     */
    public static Properties loadPropertiesFile(String fileName) {

        Properties properties = null;

        // search for the properties file
        URI resourceURL = ResourceTools.locateResource(fileName);

        try {
            if (resourceURL != null) {
                InputStream in = ResourceTools.openStream(resourceURL);
                if (in != null) {
                    properties = new Properties();
                    properties.load(in);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return properties;
    }

    /**
     * Loads Properties files using the default ResourceLocator.  Latter files are merged into the first but do not
     * override existing properties.  Cross-references are resolved.
     *
     * @param fileNames  name of the properties files
     * @return properties
     */
    public static Properties loadPropertiesFile(String... fileNames) {
        return new PropertiesContext(fileNames).getProperties();
    }

    /**
     * Merge properties from one set into another
     *
     * Properties that already exist in the first are not overwritten.
     *
     * @param into      properties are merged into this set (and returned)
     * @param from      properties are read from this set (unchanged)
     */
    private void mergeProperties(Properties into, Properties from) {
        for (Object key : from.keySet()) {
            if (!into.contains(key)) {
                into.setProperty((String) key, (String) from.get(key));
            }
        }
    }
    /**
     * Return all the current properties values as a Property set
     *
     * @return Properties
     */
    public Properties getProperties() {

        Properties mergedSet = new Properties();

        // loop through all the urls in precedence order
        for (String url : precedence) {
            Properties properties = propertyCache.get(url);
            mergeProperties(mergedSet, properties);
        }
        
        return mergedSet;
    }

    /**
     * Return all the current properties with a name starting with the prefix provided
     *
     * @param prefix of the property name
     * @return Properties
     */
    public Properties getPropertiesStartingWith(String prefix) {

        Properties allProperties = getProperties();
        Properties filteredProperties = new Properties();
        for (Object key : allProperties.keySet()) {
            if (((String) key).startsWith(prefix)) {
                filteredProperties.put(key, allProperties.get(key));
            }
        }
        return filteredProperties;                
    }

    /**
     * Sets an individual property in the Context.
     *
     * This property will take precedence over existing properites with the same name.  If an individual
     *  property with the same name has previously been set (via this method) it is replaced.
     *
     * @param name
     * @param value
     */
    public void setProperty(String name, String value) {
        Properties properties = new Properties();
        properties.setProperty(name, value);
        cacheProperties(name, properties);
    }
}
