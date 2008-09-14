package com.blueskyminds.framework.alias;

/**
 * Identifies a class that has aliases (alternative names)
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public interface Aliased {

    /** Get the aliases for this object, excluding its actual name */
    String[] getAliases();

    /** Get all names and aliases */
    String[] getNames();
}
