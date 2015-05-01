package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.party.Individual;

/**
 * Date Started: 15/03/2009
 */
public class NotExpressionTest extends TestCase {

   private Tag a,b,c;
   private NotExpression notA;
   private Individual i,j;

   @Override
   protected void setUp() throws Exception {
       super.setUp();
       a = new Tag("A");
       b = new Tag("B");
       c = new Tag("C");

       i = new Individual("Trev").withTag(a);
       j = new Individual("Trev").withTag(b);

       notA = NotExpression.build(a);
   
   }

    public void testEvaluate() {
        assertFalse(notA.evaluate(i));
        assertTrue(notA.evaluate(j));
    }

    public void testSuperset() {
        assertEquals(0, notA.getORSuperset().size());  // empty set for not
    }


    public void testAsString() {
        assertEquals("(not a)", notA.asString());
    }
}
