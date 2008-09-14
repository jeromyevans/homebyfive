package com.blueskyminds.framework.hierarchy;

import com.blueskyminds.framework.test.BaseTestCase;
import com.blueskyminds.framework.AbstractManyParentsDomainObject;

/**
 * Methods for testing the hierarchy framework
 *
 * Date Started: 2/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestHierarchy extends BaseTestCase {

    public TestHierarchy(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestHierarchy with default attributes
     */
    private void init() {
    }

    private class TestMany extends AbstractManyParentsDomainObject<TestMany> {

        String value;

        public TestMany(String value) {
            this.value = value;
        }

    }

    // ------------------------------------------------------------------------------------------------------

    public void testManyParents() {
        TestMany A = new TestMany("A");
        TestMany A1 = new TestMany("A.1");
        TestMany A2 = new TestMany("A.2");
        TestMany A11 = new TestMany("A.1.1");
        TestMany A12 = new TestMany("A.1.2");
        TestMany F = new TestMany("F");

        A.addChild(A1);
        A.addChild(A2);

        A1.addChild(A11);
        A1.addChild(A12);

        // now get complicated - A12 has a second parent F
        A12.addParent(F);

        assertTrue(A.isAncestor(A11));
        assertTrue(A.isAncestor(A12));
        assertTrue(F.isAncestor(A12));
        assertFalse(F.isAncestor(A11));
        assertTrue(A2.isSibling(A1));
        assertFalse(F.isSibling(A1));
        assertTrue(F.hasChild(A12));
        assertFalse(A.hasDescendant(F));
        assertTrue(A11.hasParent(A1));
        assertFalse(A11.hasParent(A2));
    }
}
