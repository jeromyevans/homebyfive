package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.party.Individual;

/**
 * Date Started: 15/03/2009
 */
public class EqualsExpressionTest extends TestCase {

     private Tag a,b,c;
   private EqualsExpression equalsA;
   private Individual i,j;

    @Override
   protected void setUp() throws Exception {
       super.setUp();
       a = new Tag("A");
       b = new Tag("B");
       c = new Tag("C");

       i = new Individual("Trev").withTag(a);
       j = new Individual("Trev").withTag(b);

       equalsA = EqualsExpression.build(a);

   }

    public void testEvaluate() {
        assertTrue(equalsA.evaluate(i));
        assertFalse(equalsA.evaluate(j));
    }

    public void testSuperset() {
        assertEquals(1, equalsA.getORSuperset().size());
    }


    public void testAsString() {
        assertEquals("a", equalsA.asString());
    }
}
