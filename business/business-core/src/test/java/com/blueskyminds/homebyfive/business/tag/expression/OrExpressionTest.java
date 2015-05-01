package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.party.Individual;

/**
 * Date Started: 15/03/2009
 */
public class OrExpressionTest extends TestCase {

    private OrExpression aOrB;
    private OrExpression aOrBorC;
    private Tag a,b,c;
    private OrExpression aOrBandC;
    private OrExpression aOrNotB;
    private Individual i,j,k,l;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = new Tag("A");
        b = new Tag("B");
        c = new Tag("C");

        i = new Individual("Trev").withTag(a);
        j = new Individual("Trev").withTags(a, b);
        k = new Individual("Trev").withTags(c);
        l = new Individual("Trev").withTags(b, c);

        aOrB = OrExpression.build(a, b);
        aOrBorC = OrExpression.build(a, b, c);
        aOrBandC = OrExpression.build(a, AndExpression.build(b, c));
        aOrNotB = OrExpression.build(a, NotExpression.build(b));
    }

    public void testEvaluate() {
        assertTrue(aOrB.evaluate(i));
        assertTrue(aOrB.evaluate(j));
        assertFalse(aOrB.evaluate(k));
        assertTrue(aOrB.evaluate(l));

        assertTrue(aOrBorC.evaluate(i));
        assertTrue(aOrBorC.evaluate(j));
        assertTrue(aOrBorC.evaluate(k));
        assertTrue(aOrBorC.evaluate(l));

        assertTrue(aOrBandC.evaluate(i));
        assertTrue(aOrBandC.evaluate(j));
        assertFalse(aOrBandC.evaluate(k));
        assertTrue(aOrBandC.evaluate(l));

        assertTrue(aOrNotB.evaluate(i));
        assertTrue(aOrNotB.evaluate(j));
        assertTrue(aOrNotB.evaluate(k));
        assertFalse(aOrNotB.evaluate(l));
    }


    public void testSuperset() {
        assertEquals(2, aOrB.getORSuperset().size());
        assertEquals(3, aOrBorC.getORSuperset().size());
        assertEquals(3, aOrBandC.getORSuperset().size());
        assertEquals(0, aOrNotB.getORSuperset().size());             // empty set for not
    }

    public void testAsString() {
        assertEquals("(a or b)", aOrB.asString());
        assertEquals("(a or (b or c))", aOrBorC.asString());
        assertEquals("(a or (b and c))", aOrBandC.asString());
        assertEquals("(a or (not b))", aOrNotB.asString());
    }
}
