package com.blueskyminds.framework.patterns;

/**
 * Date Started: 13/10/2007
 * <p/>
 * History:
 */
public interface Pattern {
    
    String getPattern();

    String getSubstitution();

    Object getMetadata();

    boolean isExclusive();

    int getGroupNo();
}
