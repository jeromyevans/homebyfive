package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.tag.factory.InMemoryTagFactory;

/**
 * Date Started: 20/03/2009
 */
public class TagExpressionFactoryTest extends TestCase {
    
    private TagExpressionFactory tagExpressionFactory;
    private InMemoryTagFactory tagFactory;

    public void setUp() {
        tagFactory = new InMemoryTagFactory();
        tagExpressionFactory = new TagExpressionFactory(tagFactory);
    }

    public void testEvaluate() throws Exception {
        assertEquals("(a or (b and c))", tagExpressionFactory.evaluate("a or (b and c)").asString());
        assertEquals("(not b)", tagExpressionFactory.evaluate("not b").asString());
        assertEquals("(a or (b or c))", tagExpressionFactory.evaluate("a or b or c").asString());
    }
}
