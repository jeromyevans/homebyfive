package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;
import java.util.*;

/**
 * An expression defining Tags to evaluate against a Taggable, with support AND, OR and NOT logic and grouping
 * Use the TagExpression
 * 
 * Date Started: 18/11/2008
 * <p/>
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
@Entity
@Table(name="TagExpression")
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("T")
public abstract class TagExpression extends AbstractEntity {

    /**
     * Calculate the OR'd superset of tags for this expression.
     * The superset is used to query Taggables prior to the expression-based filtering
     *
     * eg.  a or (b and c) = [a,b,c] 
     *     
     * NOTE If the expression contains a NOT the set is empty because it can't be calculated
     *
     * @return
     */
    @Transient
    public Set<Tag> getORSuperset() {
        Set<Tag> result = new HashSet<Tag>();
        if (extractORSuperset(result)) {
            return result;
        } else {
            // contains a NOT - need to bail out
            return new HashSet<Tag>();
        }
    }

    /**
     * Add the OR'd superset to the current set, returning true of okay.
     * If this is a NOT, then needs to return false and add nothing
     *
     * @param currentSet
     * @return
     */
    protected abstract boolean extractORSuperset(Set<Tag> currentSet);

    /** Join two tag sets */
    protected Set<Tag> join(Set<Tag> superset1, Set<Tag> superset2) {
        Set<Tag> joined = new HashSet<Tag>(superset1);
        joined.addAll(superset2);
        return joined;
    }

    /**
     * Evaluate the expression against the parameter
     *
     * @param taggable
     * @return true if the expression is true for the paramater
     */
    public abstract boolean evaluate(Taggable taggable);

    /**
     * Filter a Set of Taggables to those that evaluate to true for this expression
     *
     * @param taggableSet
     * @return
     */
    public <T extends Taggable> Set<T> filter(Collection<T> taggableSet) {
        Set<T> filtered = new HashSet<T>(taggableSet.size());
        for (T taggable : taggableSet) {
            if (evaluate(taggable)) {
                filtered.add(taggable);
            }
        }
        return filtered;
    }

    /**
     * Encode the expression as a string
     *
     * @return
     */
    public abstract String asString();

}
