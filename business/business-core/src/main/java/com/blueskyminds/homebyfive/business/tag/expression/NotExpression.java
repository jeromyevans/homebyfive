package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.tag.Tag;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Date Started: 14/03/2009
 */
@Entity
@DiscriminatorValue("N")
public class NotExpression extends TagExpression {

    private TagExpression notOp1;

    public NotExpression(TagExpression notOp1) {
        this.notOp1 = notOp1;
    }

    public NotExpression() {
    }

    @ManyToOne
    @JoinColumn(name="NotOp1")
    public TagExpression getNotOp1() {
        return notOp1;
    }

    public void setNotOp1(TagExpression notOp1) {
        this.notOp1 = notOp1;
    }


    /**
     * Returns true if the operand evaluates to false
     *
     * @param taggable
     * @return
     */
    public boolean evaluate(Taggable taggable) {
        return !notOp1.evaluate(taggable);
    }

    public String asString() {
        return "(not "+notOp1.asString()+")";
    }

    /**
     * This is a NOT so th operands can't be added to the set. Force bail-out of the proceses
     * as the superset can't be calculated from here (ie. everything NOT inside the operand is unknown)
     *
     * @param currentSet
     * @return
     */
    protected boolean extractORSuperset(Set<Tag> currentSet) {
        return false;
    }

    public static NotExpression build(Tag tag) {
        return new NotExpression(EqualsExpression.build(tag));
    }

    public static NotExpression build(TagExpression a) {
        return new NotExpression(a);
    }
}