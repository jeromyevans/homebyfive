package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.party.Individual;

/**
 * Date Started: 14/03/2009
 */
public class AndExpressionTest extends TestCase {

    private AndExpression aAndB;
    private AndExpression aAndBandC;
    private Tag a,b,c;
    private AndExpression aAndBorC;
    private AndExpression aAndNotB;
    private Individual i, j, k, l;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = new Tag("A");
        b = new Tag("B");
        c = new Tag("C");

        i = new Individual("Trev").withTag(a);
        j = new Individual("Trev").withTags(a, b);
        k = new Individual("Trev").withTags(a, c);
        l = new Individual("Trev").withTags(a, b, c);

        aAndB = AndExpression.build(a, b);
        aAndBandC = AndExpression.build(a, b, c);
        aAndBorC = AndExpression.build(a, OrExpression.build(b, c));
        aAndNotB = AndExpression.build(a, NotExpression.build(b));
    }

    public void testEvaluate() {
        assertFalse(aAndB.evaluate(i));
        assertTrue(aAndB.evaluate(j));
        assertFalse(aAndB.evaluate(k));
        assertTrue(aAndB.evaluate(l));

        assertFalse(aAndBandC.evaluate(i));
        assertFalse(aAndBandC.evaluate(j));
        assertFalse(aAndBandC.evaluate(k));
        assertTrue(aAndBandC.evaluate(l));

        assertFalse(aAndBorC.evaluate(i));
        assertTrue(aAndBorC.evaluate(j));
        assertTrue(aAndBorC.evaluate(k));
        assertTrue(aAndBorC.evaluate(l));

        assertTrue(aAndNotB.evaluate(i));
        assertFalse(aAndNotB.evaluate(j));
        assertTrue(aAndNotB.evaluate(k));
        assertFalse(aAndNotB.evaluate(l));
    }

    public void testSuperset() {
        assertEquals(2, aAndB.getORSuperset().size());
        assertEquals(3, aAndBandC.getORSuperset().size());
        assertEquals(3, aAndBorC.getORSuperset().size());
        assertEquals(0, aAndNotB.getORSuperset().size());     // empty set because of the NOT
    }

    public void testAsString() {
        assertEquals("(a and b)", aAndB.asString());
        assertEquals("(a and (b and c))", aAndBandC.asString());
        assertEquals("(a and (b or c))", aAndBorC.asString());
        assertEquals("(a and (not b))", aAndNotB.asString());
    }
}
