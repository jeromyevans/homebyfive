package com.blueskyminds.homebyfive.framework.core.persistence.schema;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.HypersonicTestCase;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SchemaMigrationGeneratorTest extends HypersonicTestCase {

    public void testMigrator() throws Exception {
        SchemaMigrationGenerator ddlGenerator = new SchemaMigrationGenerator("TestFrameworkPersistenceUnit", "example", "../resources/schema");
        ddlGenerator.setDryRun(true);
        ddlGenerator.start();
    }
}
