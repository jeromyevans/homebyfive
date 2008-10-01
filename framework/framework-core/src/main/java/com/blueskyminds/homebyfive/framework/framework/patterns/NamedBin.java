package com.blueskyminds.homebyfive.framework.framework.patterns;

import com.blueskyminds.homebyfive.framework.framework.tools.Named;
import com.blueskyminds.homebyfive.framework.framework.alias.Aliased;

import java.util.*;

import org.apache.commons.collections.FastHashMap;

/**
 * A NamedBin uses Named objects as the patterns.
 *
 * It uses fuzzy matching against the Named objects and their aliases (if any).
 *
 * The Named objects can be grouped using a hash to optimize processing.
 *
 * Date Started: 13/10/2007
 * <p/>
 * History:
 */
public class NamedBin extends Bin {

    /** All Named objects */
    private Set<Named> defaultGroup;

    /** Named objects grouped by a key for faster matching */
    private Map<Object, Set<Named>> groupedObjects;

    public NamedBin() throws PatternMatcherInitialisationException {
        super();
        defaultGroup = new HashSet<Named>();
        groupedObjects = new FastHashMap();
    }

    /**
     * Get the group specified by the key.  Create it if it doesn't exist
     * @param groupKey
     * @return
     */
    private Set<Named> getGroup(Object groupKey) {
        Set<Named> group = groupedObjects.get(groupKey);
        if (group == null) {
            group = new HashSet<Named>();
            groupedObjects.put(groupKey, group);
        }
        return group;
    }

    /**
     * Adds the name objects to the default group
     *
     * @param named
     */
    public void addNamed(Collection<? extends Named> named) {
        defaultGroup.addAll(named);
    }

    /**
     * Adds the named object to the default group
     *
     * @param named
     */
    public void addNamed(Named named) {
        defaultGroup.add(named);
    }

    /**
     * Adds the name objects to the default group
     *
     * @param named
     */
    public void addNamed(String groupKey, Collection<? extends Named> named) {
        defaultGroup.addAll(named);
        getGroup(groupKey).addAll(named);
    }

    /**
     * Adds the named object to the default group
     *
     * @param named
     */
    public void addNamed(String groupKey, Named named) {
        defaultGroup.add(named);
        getGroup(groupKey).add(named);
    }

    /**
     * Group the named by metaphones calculated from all names and aliases for faster matching
     * A single Named object may belong to multiple groups if its Aliased
     **/
    public void addNamedGroupByMetaphone(Collection<? extends Named> namedObjects) {
        String[] metaphones;
        for (Named named : namedObjects) {
            // group by metaphone for faster matching
            if (named instanceof Aliased) {
                metaphones = LevensteinDistanceTools.calculateDoubleMetaphones(((Aliased) named).getNames());
                for (String metaphone : metaphones) {
                    addNamed(metaphone, named);
                }
            } else {
                addNamed(LevensteinDistanceTools.calculateDoubleMetaphone(named.getName()), named);
            }
        }
    }

    /**
     * Calculate which group the word belongs to to limit the Named patterns used in the matching
     *
     * The default implementation does nothing (uses the default group)
     *
     * @param word
     * @return
     */
    protected Object calculateGroup(String word) {
        return null;
    }

    /**
     * Determines if the given word matches any of the Named objects or their aliases using
     *  a fuzzy matching algorithm
     *
     * @param word
     * @return All of the named objects that were matched (may be zero, one or more)
     */
    protected Set<PatternMatch> wordMatchesPattern(String word) {
        boolean exclusive = true;
        List<Named> matchingNamedObjects;
        Set<PatternMatch> matches = new HashSet<PatternMatch>();

        Object group = calculateGroup(word);
        if (group == null) {
            // use the default group
            matchingNamedObjects = LevensteinDistanceTools.matchName(word, defaultGroup);
        } else {
            Set<Named> grouped = getGroup(group);
            matchingNamedObjects = LevensteinDistanceTools.matchName(word, grouped);
        }

        for (Named match : matchingNamedObjects) {
            // todo: also matches exact
            matches.add(new PatternMatch(new NamedPatternDecorator(match), exclusive, null, PatternMatchType.Fuzzy));
        }

        return matches;
    }
}
