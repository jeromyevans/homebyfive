package com.blueskyminds.framework.persistence.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.dialect.HSQLDialect;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.List;
import java.net.URI;

import junit.framework.TestCase;

/**
 * Date Started: 5/02/2007
 */
public class HibernateSchemaMigratorTest extends TestCase {

    public static final Log LOG = LogFactory.getLog(HibernateSchemaMigratorTest.class);

    private HibernateSchemaMigrator schemaMigrator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        schemaMigrator = new HibernateSchemaMigrator("example");
    }

    /** The migration path identifies the all script directories the execution order */
    public void testMigrationPath() throws Exception {

        List<URI> scriptDirectories = schemaMigrator.locateScriptDirectories();

        assertNotNull(scriptDirectories);
        assertEquals(2, scriptDirectories.size());
    }

    /** Generate a list of all the scripts that need to be executed */
    public void testSciptList() throws Exception {

        List<MigrationScript> scripts = schemaMigrator.locateMigrationScripts("hsql", -1, -1);

        assertNotNull(scripts);
        assertEquals(2, scripts.size());
    }

}
