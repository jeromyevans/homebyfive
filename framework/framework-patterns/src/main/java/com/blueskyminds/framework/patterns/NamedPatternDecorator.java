package com.blueskyminds.framework.patterns;

import com.blueskyminds.framework.tools.Named;

/**
 * Decorates a Named object as a Pattern
 *
 * Date Started: 13/10/2007
 * <p/>
 * History:
 */
public class NamedPatternDecorator implements Pattern {

    private Named named;

    public NamedPatternDecorator(Named named) {
        this.named = named;
    }

    public String getPattern() {
        return named.getName();
    }

    public Named getNamed() {
        return named;
    }

    public String getSubstitution() {
        return null;
    }

    public Object getMetadata() {
        return named;
    }

    public boolean isExclusive() {
        return true;
    }

    public int getGroupNo() {
        return 0;
    }

    public String toString() {
        return getNamed().toString();
    }
}
