package com.blueskyminds.homebyfive.business.tools;

import org.apache.commons.lang.StringUtils;

/**
 * Derives a key from a string that is suitable for use in a URI
 *
 * Date Started: 21/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class KeyGenerator {

    /**
     * Generate a URL-friendly ID from the key
     *
     * @param key
     * @return
     */
    public static String generateId(String key) {        
        return generateID(key, "[^(\\w|\\d|\\+|\\-)]");
    }

    private static String generateID(String key, String punctuationPattern) {
        if (key != null) {
            key = StringUtils.strip(key);           
            // replace spaces with +
            key = key.replaceAll("\\s+", "+");
            // strip all other punctuation
            key = key.replaceAll(punctuationPattern, "");
            // convert to lowercase
            key = StringUtils.lowerCase(key);
        }
        return key;
    }

    /**
     *
     * Removes escape characters from the id
     *
     * @param key
     * @return
     */
    public static String unescapeId(String key) {
        // replace + with spaces
        key = StringUtils.trim(key.replaceAll("\\+", " "));
        return key;    
    }

     /**
     * Generate a URI-friendly ID from the key, preserving slashes
     *
     * @param key
     * @return
     */
    public static String generateIdPerservingSlashes(String key) {
        return generateID(key, "[^(\\w|\\d|\\+|\\-|\\/)]");
    }
    
}
