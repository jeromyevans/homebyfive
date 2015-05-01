package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.tag.Tag;

import javax.persistence.*;

import java.util.Set;

/**
 * Date Started: 14/03/2009
 */
@Entity
@DiscriminatorValue("A")
public class AndExpression extends TagExpression {

    private TagExpression andOp1;
    private TagExpression andOp2;

    public AndExpression(TagExpression andOp1, TagExpression andOp2) {
        this.andOp1 = andOp1;
        this.andOp2 = andOp2;
    }

    public AndExpression() {
    }

    @ManyToOne
    @JoinColumn(name="AndOp1")
    public TagExpression getAndOp1() {
        return andOp1;
    }

    public void setAndOp1(TagExpression andOp1) {
        this.andOp1 = andOp1;
    }

    @ManyToOne
    @JoinColumn(name="AndOp2")
    public TagExpression getAndOp2() {
        return andOp2;
    }

    public void setAndOp2(TagExpression andOp2) {
        this.andOp2 = andOp2;
    }

    /**
     * Both operands of the AND are added to the superet
     *
     * @param currentSet
     * @return
     */
    protected boolean extractORSuperset(Set<Tag> currentSet) {
        return andOp1.extractORSuperset(currentSet) & andOp2.extractORSuperset(currentSet);
    }

    /**
     * Returns true if both operands evaluate true against the parameter
     * 
     * @param taggable
     * @return
     */
    public boolean evaluate(Taggable taggable) {
        return andOp1.evaluate(taggable) && andOp2.evaluate(taggable);
    }

    public String asString() {
        return "("+andOp1.asString() +" and "+andOp2.asString()+")";
    }

    public static AndExpression build(Tag a, Tag b) {
        return new AndExpression(new EqualsExpression(a), new EqualsExpression(b));
    }


    public static AndExpression build(Tag a, TagExpression b) {
        return new AndExpression(new EqualsExpression(a), b);
    }

    /** Creates nested AndExpressions to and all the tags */
    public static AndExpression build(Tag... tags) {
        if (tags.length > 2) {
            return AndExpression.build(tags[0], build(tags, 1, tags.length-1));
        } else {
            if (tags.length == 2) {
                return AndExpression.build(tags[0], tags[1]);
            }
            return null;
        }
    }

    /**
     * Creates nested AndExpression's to AND all the tags. Indexes used to avoid array copies
     *
     * @param tags
     * @param startIndexInc
     * @param endIndexInc
     * @return
     */
    private static AndExpression build(Tag[] tags, int startIndexInc, int endIndexInc) {
        int length = endIndexInc - startIndexInc + 1;
        if (length > 2) {
            return AndExpression.build(tags[startIndexInc], build(tags, startIndexInc+1, length-1));
        } else {
            if (length == 2) {
                return AndExpression.build(tags[startIndexInc], tags[startIndexInc+1]);
            }
            return null;
        }
    }

    public static AndExpression build(TagExpression a, TagExpression b) {
        return new AndExpression(a, b);
    }
}
