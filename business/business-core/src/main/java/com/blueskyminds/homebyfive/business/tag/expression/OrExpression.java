package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.tag.Tag;

import javax.persistence.*;
import java.util.Set;

/**
 * Date Started: 14/03/2009
 */
@Entity
@DiscriminatorValue("O")
public class OrExpression extends TagExpression {

    private TagExpression orOp1;
    private TagExpression orOp2;

    public OrExpression(TagExpression orOp1, TagExpression orOp2) {
        this.orOp1 = orOp1;
        this.orOp2 = orOp2;
    }

    public OrExpression() {
    }

    @ManyToOne
    @JoinColumn(name="OrOp1")
    public TagExpression getOrOp1() {
        return orOp1;
    }

    public void setOrOp1(TagExpression orOp1) {
        this.orOp1 = orOp1;
    }

    @ManyToOne
    @JoinColumn(name="OrOp2")
    public TagExpression getOrOp2() {
        return orOp2;
    }

    public void setOrOp2(TagExpression orOp2) {
        this.orOp2 = orOp2;
    }

    /**
     * Returns true if either operand evaluate true against the parameter
     *
     * @param taggable
     * @return
     */
    public boolean evaluate(Taggable taggable) {
        return orOp1.evaluate(taggable) || orOp2.evaluate(taggable);
    }

    public String asString() {
        return "("+orOp1.asString() +" or "+orOp2.asString()+")";
    }

    /**
     * Both operands of the OR are added to the superet
     *
     * @param currentSet
     * @return
     */
    protected boolean extractORSuperset(Set<Tag> currentSet) {
        return orOp1.extractORSuperset(currentSet) & orOp2.extractORSuperset(currentSet);
    }
    
    public static OrExpression build(Tag a, Tag b) {
        return new OrExpression(new EqualsExpression(a), new EqualsExpression(b));
    }


    public static OrExpression build(Tag a, TagExpression b) {
        return new OrExpression(new EqualsExpression(a), b);
    }

    /** Creates nested OrExpressions to or all the tags */
    public static OrExpression build(Tag... tags) {
        if (tags.length > 2) {
            return OrExpression.build(tags[0], build(tags, 1, tags.length-1));
        } else {
            if (tags.length == 2) {
                return OrExpression.build(tags[0], tags[1]);
            }
            return null;
        }
    }

    /**
     * Creates nested OrExpression's to or all the tags. Indexes used to avoid array copies
     *
     * @param tags
     * @param startIndexInc
     * @param endIndexInc
     * @return
     */
    private static OrExpression build(Tag[] tags, int startIndexInc, int endIndexInc) {
        int length = endIndexInc - startIndexInc + 1;
        if (length > 2) {
            return OrExpression.build(tags[startIndexInc], build(tags, startIndexInc+1, length-1));
        } else {
            if (length == 2) {
                return OrExpression.build(tags[startIndexInc], tags[startIndexInc+1]);
            }
            return null;
        }
    }

    public static OrExpression build(TagExpression a, TagExpression b) {
        return new OrExpression(a, b);
    }
}