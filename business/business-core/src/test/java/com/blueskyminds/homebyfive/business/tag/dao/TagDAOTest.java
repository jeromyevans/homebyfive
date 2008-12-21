package com.blueskyminds.homebyfive.business.tag.dao;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.tag.Tag;

/**
 * Date Started: 21/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TagDAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";
    private TagDAO tagDAO;

    public TagDAOTest() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        tagDAO = new TagDAO(em);
    }

    public void testPersist() {
        Tag tag = new Tag("test");
        tagDAO.persist(tag);
        em.flush();
    }
}
