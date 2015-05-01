package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.Taggable;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Date Started: 14/03/2009
 */
@Entity
@DiscriminatorValue("E")
public class EqualsExpression extends TagExpression {

    private Tag equalsOp;

    public EqualsExpression(Tag tag) {
        this.equalsOp = tag;
    }

    public EqualsExpression() {
    }

    @ManyToOne
    @JoinColumn(name="EqualsOp")
    public Tag getEqualsOp() {
        return equalsOp;
    }

    public void setEqualsOp(Tag equalsOp) {
        this.equalsOp = equalsOp;
    }

    /**
     * Returns true if the parameter has this expression's tag
     *
     * @param taggable
     * @return
     */
    public boolean evaluate(Taggable taggable) {
        if (taggable != null) {
            return taggable.hasTag(equalsOp);
        } else {
            return false;
        }
    }

    public String asString() {
        return equalsOp.getKey();
    }

     /**
     * The referenced Tag is added to the superset
     *
     * @param currentSet
     * @return
     */
    protected boolean extractORSuperset(Set<Tag> currentSet) {
        currentSet.add(equalsOp);
        return true;
    }

    public static EqualsExpression build(Tag tag) {
        return new EqualsExpression(tag);
    }

}
