package com.blueskyminds.homebyfive.framework.core.substitution;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestSubstitution extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestFrameworkPersistenceUnit";

    public TestSubstitution() {
        super(PERSISTENCE_UNIT);
    }

    public void testSubstitution() throws Exception {
        Substitution substitution = new Substitution("test", "Replace a with b", "a", "b", true, 0, null);
        em.persist(substitution);
        
        em.flush();
    }
}
