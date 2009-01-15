package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.Taggable;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * An expression defining Tags to match, with AND, OR and NOT semantics
 *
 * For now, it's just an OR
 * 
 * Date Started: 18/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TagExpression {

    private List<Tag> tags;

    public TagExpression() {
        tags = new LinkedList<Tag>();
    }

    public void include(Tag tag) {
        tags.add(tag);
    }

    public Set<Tag> getSuperset() {
        return new HashSet<Tag>(tags);
    }

    /**
     * Evaluate the expression against the parameter
     *
     * @param taggable
     * @return
     */
    public boolean evaluate(Taggable taggable) {
        for (Tag tag : tags) {
            if (taggable.hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter a Set of Taggables to those that evaluate to true for this expression
     *
     * @param taggableSet
     * @return
     */
    public <T extends Taggable> Set<T> filter(Set<T> taggableSet) {
        Set<T> filtered = new HashSet<T>(taggableSet.size());
        for (T taggable : taggableSet) {
            if (evaluate(taggable)) {
                filtered.add(taggable);
            }
        }
        return filtered;
    }

    /**
     * Todo: encode the expression as a string
     *
     * @return
     */
    public String asString() {
        return null;
    }
}
