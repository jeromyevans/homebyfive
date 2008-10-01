package com.blueskyminds.homebyfive.framework.core.persistence.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hsqldb.jdbc.jdbcDataSource;

import java.net.URI;
import java.util.*;
import java.io.IOException;
import java.io.File;
import java.sql.SQLException;
import java.sql.Connection;

import com.blueskyminds.homebyfive.framework.core.tools.*;
import com.blueskyminds.homebyfive.framework.core.tools.filters.PrefixFilter;
import com.blueskyminds.homebyfive.framework.core.tools.filters.NonBlankFilter;
import com.blueskyminds.homebyfive.framework.core.persistence.jdbc.PersistenceTools;

import javax.sql.DataSource;

/**
 *
 * Enables incremental migration of a database schema.
 * Loads and applies incremental sql files.
 *
 */
public class HibernateSchemaMigrator implements SchemaMigrator {

    private static final Log LOG = LogFactory.getLog(HibernateSchemaMigrator.class);

    private static final String SCHEMA_DIR = "schema";
    private static final String JAVA_PLUGIN_PREFIX = "java:";

    private String schemaName;

    public HibernateSchemaMigrator(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     *   Find all of the schema directories
     *   Find all of the specified schemaName subdirectories
     *   Find all of the script directories for the schema, ordered by revisionNo
     *
     * @return list of script directories in order of execution
     */
    public List<URI> locateScriptDirectories() {

        LOG.debug("Searching "+SCHEMA_DIR+" directories in the classpath...");

        // lookup the list of available schema directories in the classpath
        URI[] schemaDataDirs = ResourceTools.locateResources(SCHEMA_DIR);
        LOG.info("Found "+schemaDataDirs.length+" schema directories in classpath/filesystem.");
        final List<URI> classpathSchemaDirs = new LinkedList<URI>();

        if (schemaDataDirs.length > 0) {
            for (URI directory : schemaDataDirs) {
                try {
                    classpathSchemaDirs.addAll(FileTools.listDirectories(directory));
                } catch (IOException e) {
                    // ignored
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("classpathSchemaDirs: "+classpathSchemaDirs.size());
            for (URI url : classpathSchemaDirs) {
                LOG.debug("Found schema directory: "+url.toString());
            }
        }

        // build the list of schema names/module names to include in the migration
        List<String> schemaNames = new LinkedList<String>();


        // locate the migration script directories for each module/schema name,
        final List<URI> moduleScriptDirs = new LinkedList<URI>();

        // locate all of the migration directories for this schema/module
        // (there's usually only zero or one, but it's possible that there's multiple
        // in different jars)
        try {
            LOG.debug("Schema name: "+schemaName);
            for (URI moduleSchemaDir : classpathSchemaDirs) {

                File directory = new File(moduleSchemaDir);
                String path = directory.getAbsolutePath();

                LOG.debug("Searching: "+path);
                if (FileTools.stripTrailingPathSeparator(path).endsWith(schemaName)) {
                    final Map<String, URI> unsortedSubDirs = new HashMap<String, URI>();

                    FileTools.visitFiles(moduleSchemaDir, false, new FileVisitor() {
                        public void visit(URI pathToFile, boolean isDirectory) {
                            if (isDirectory) {
                                String fileName = FileTools.extractLastFilenameOrPathname(pathToFile);
                                if (!fileName.startsWith(".")) { // ignore hidden directories
                                    unsortedSubDirs.put(fileName, pathToFile);
                                }
                            }
                        }
                    });

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("UnsortedSubDirs: "+unsortedSubDirs.size());
                        for (String url : unsortedSubDirs.keySet()) {
                            LOG.debug("   sub-directory: "+url);
                        }
                    }

                    // sort by revisionNo
                    List<String> orderedRevisions = new ArrayList<String>(unsortedSubDirs.keySet());
                    Collections.sort(orderedRevisions, new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            int i1 = -1;
                            int i2 = -1;

                            try {
                                i1 = Integer.parseInt(o1);
                                try {
                                    i2 = Integer.parseInt(o2);
                                    return ((Integer) i1).compareTo(i2);
                                } catch (NumberFormatException e) {
                                    return -1;
                                }
                            } catch (NumberFormatException e) {
                                try {
                                    i2 = Integer.parseInt(o2);
                                    return 1;
                                } catch (NumberFormatException e2) {
                                    return o1.compareToIgnoreCase(o2);
                                }
                            }
                        }
                    });

                    // add the revision directories in order to the list of all scripts
                    for (String revisionNo : orderedRevisions) {
                        moduleScriptDirs.add(unsortedSubDirs.get(revisionNo));
                    }
                } else {
                    LOG.debug("Ignoring directory: "+path);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }


        if (LOG.isDebugEnabled()) {
            LOG.debug("moduleScriptDirs: "+moduleScriptDirs.size());
            for (URI url : moduleScriptDirs) {
                LOG.debug("   script directory: "+url.toString());
            }
        }

        return moduleScriptDirs;
    }

    /**
     * Sets up an in-memory hypersonic database to create the sql for the specified dialect:
     *    - creates the empty database
     *    - applies the existing sql scripts to the database
     *    - uses hibernate to detect the schema changes required using the current codebase
     *    - writes the changes to the target file */
    public List<String> calculateMigrationSql(Dialect dialect) throws SQLException {
        DataSource ds = createDataSource();
        Connection con = ds.getConnection();



        return null; // todo
    }

    private DataSource createDataSource() {
        jdbcDataSource ds = new jdbcDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:migrator");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    /**
     * Generates a list of the SQL scripts that need to be executed to migrate between the two
     *  specified revisions.
     *
     * @param targetDialect
     * @param fromRevision   (-1 for a new installation)
     * @param toRevision
     * @return
     */
    public List<MigrationScript> locateMigrationScripts(String targetDialect, int fromRevision, int toRevision) {
        List<URI> scriptDirs = locateScriptDirectories();
        List<MigrationDir> revisionSpecificDirs = filterDirectoriesToRevisions(scriptDirs, fromRevision, toRevision);
        List<MigrationDir> dialectSpecificDirs = filterDirectoriesToDialect(revisionSpecificDirs, targetDialect);
        List<MigrationScript> scripts = new LinkedList<MigrationScript>();

        // read the order files to get the list of scripts
        for (MigrationDir baseDir : dialectSpecificDirs) {
            URI orderFile = FileTools.concatenateURI(baseDir.getDirectory(), "order.txt");
            LOG.debug("Reading order file:"+orderFile.toString());
            try {
                String[] lines = FileTools.readTextFile(orderFile, new PrefixFilter("#"));
                for (String filename : lines) {
                    if (filename.startsWith(JAVA_PLUGIN_PREFIX)) {
                        String className = StringUtils.substringAfter(filename, JAVA_PLUGIN_PREFIX);
                        scripts.add(new MigrationScript(baseDir.getRevisionNo(), className));
                    } else {
                        URI scriptFile = FileTools.concatenateURI(baseDir.getDirectory(), filename);
                        LOG.debug("Reading script:"+scriptFile.toString());
                        scripts.add(new MigrationScript(baseDir.getRevisionNo(), scriptFile));
                    }
                }
            } catch (IOException e) {
                //ignore this file
                if (orderFile != null) {
                    LOG.warn("Could not read: "+orderFile.toString(), e);
                }
            }
        }
        return scripts;
    }

    /** Filters a list of script directories down to the ones applicable to migration from one revision
     *  to another
     *
     * @param scriptDirs
     * @param fromRevision  -1 means from start
     * @param toRevision    -1 means to end
     * @return
     */
    private List<MigrationDir> filterDirectoriesToRevisions(List<URI> scriptDirs, int fromRevision, int toRevision) {
        List<MigrationDir> revisionSpecificDirs = new LinkedList<MigrationDir>();

        // filter the list down to the partiular revisios
        for (URI uri : scriptDirs) {
            String pathName = FileTools.extractLastFilenameOrPathname(uri);
            int revision;
            try {
                revision = Integer.parseInt(pathName);
                if (revision > fromRevision) {
                    if ((toRevision == -1) || (revision <= toRevision)) {
                        revisionSpecificDirs.add(new MigrationDir(revision, uri));
                    }
                }
            } catch(NumberFormatException e) {
                // ignore this directory
                LOG.warn("Ignored unrecognised script directory: "+pathName);
            }

        }
        if (LOG.isDebugEnabled()) {
            for (MigrationDir url : revisionSpecificDirs) {
                LOG.debug("Filtered to revision dir:"+url.getDirectory().toString());
            }
        }
        return revisionSpecificDirs;
    }

    /**
     * Obtains the list of dialect-specific directories from a list of script dirs
     *
     * @param scriptDirs
     * @param dialectName
     * @return
     */
    private List<MigrationDir> filterDirectoriesToDialect(List<MigrationDir> scriptDirs, String dialectName) {
        List<MigrationDir> dialectSpecificDirs = new LinkedList<MigrationDir>();

        for (MigrationDir scriptDir : scriptDirs) {
            try {
                List<URI> directories = FileTools.listDirectories(scriptDir.getDirectory());
                for (URI directory : directories) {
                    String pathName = FileTools.extractLastFilenameOrPathname(directory);
                    if (pathName.equalsIgnoreCase(dialectName)) {
                        dialectSpecificDirs.add(new MigrationDir(scriptDir.getRevisionNo(), directory));
                    }
                }
            } catch(IOException e) {
                // ignored
            }
        }

        if (LOG.isDebugEnabled()) {
            for (MigrationDir url : dialectSpecificDirs) {
                LOG.debug("Filtered to dialect dir:"+url.getDirectory().toString());
            }
        }
        return dialectSpecificDirs;
    }

    /**
     * Migrate the database schema
     *
     */
    public boolean migrate(Connection connection, String migrationDialect) throws MigrationFailedException {

        boolean completed = false;
        SchemaRevision schemaRevision = null;
        Integer currentRevision = null;
        Exception firstException = null;

        try {
            schemaRevision = new SchemaRevision(connection, migrationDialect);
            if (schemaRevision.hasSchemaRevisionTable()) {
                currentRevision = schemaRevision.lookupRevision(schemaName);
                if (currentRevision == null) {
                    currentRevision = -1;
                }
            } else {
                // create the schema revision table
                schemaRevision.createSchemaRevisionTable();
                connection.commit();
                currentRevision = -1;

            }
            LOG.info("Current '"+schemaName+"' schema revision: "+currentRevision);
        } catch(SQLException e) {
            throw new MigrationFailedException("Unable to determine current schema revision: ", e);
        }

        if (connection != null) {
            List<MigrationScript> scripts = locateMigrationScripts(migrationDialect, currentRevision, -1);
            boolean abort = false;

            if (scripts.size() > 0) {
                try {
                    // we need a transaction
                    connection.setAutoCommit(false);

                } catch(SQLException e) {
                    LOG.error("Unable to start a transaction", e);
                    abort = true;
                }

                if (!abort) {
                    int latestRevision = -1;
                    int lastRevision = -1;
                    // execute the scripts
                    for (MigrationScript script : scripts) {

                        latestRevision = script.getRevisionNo();

                        if ((latestRevision != lastRevision) && (lastRevision > -1)) {
                            // a revision change has been completed.  Commit it.
                            // update the SchemaRevision table
                            try {
                                schemaRevision.updateRevision(schemaName, lastRevision);
                                connection.commit();
                                LOG.info("Migrated schema to revision: "+lastRevision);
                                completed = true;
                            } catch(SQLException e) {
                                LOG.error("Failed to update SchemaRevision, Rolling back", e);
                                firstException = e;
                                try {
                                    LOG.error("Attempting transaction rollback");
                                    connection.rollback();
                                } catch(SQLException sqle) {
                                    LOG.error("Rollback failed", sqle);
                                }
                                abort = true;
                            }
                        }

                        if (!abort) {
                            try {
                                if (!script.isSqlFile()) {
                                    LOG.info("Applying java plugin migration: "+ script.getClassName());

                                    SchemaMigratorPlugin plugin = (SchemaMigratorPlugin) ReflectionTools.createInstanceOf(script.getClassName());
                                    if (plugin != null) {
                                        if (!plugin.migrate(script, connection, migrationDialect)) {
                                            LOG.error("   Migration aborted by plugin");
                                            abort = true;
                                        }
                                    }
                                } else {
                                    LOG.info("Applying SQL migration script: "+ script.getScript().toString());
                                    String[] sql = FileTools.readTextFile(script.getScript(), new NonBlankFilter());
                                    PersistenceTools.executeUpdate(connection, sql);
                                }

                            } catch(SQLException e) {
                                LOG.error("Error running script: "+script, e);
                                abort = true;
                                firstException = e;
                                break;
                            } catch (IOException e) {
                                LOG.error("Error accessing script: "+script, e);
                                abort = true;
                                firstException = e;
                                break;
                            } catch (ClassCastException e) {
                                LOG.error("Error running script when attempting to create a SchemaMigratorPlugin: "+script, e);
                            } catch (ReflectionException e) {
                                LOG.error("Error creating a SchemaMigratorPlugin: "+script, e);
                            }
                        }
                        lastRevision = latestRevision;

                        if (abort) {
                            break;
                        }
                    }

                    if (!abort) {
                        // update the SchemaRevision table
                        try {
                            schemaRevision.updateRevision(schemaName, latestRevision);
                            connection.commit();
                            LOG.info("Migrated schema to revision: "+latestRevision);
                            completed = true;
                        } catch(SQLException e) {
                            LOG.error("Failed to update SchemaRevision, Rolling back", e);
                            firstException = e;
                            try {
                                LOG.error("Attempting transaction rollback");
                                connection.rollback();
                            } catch(SQLException sqle) {
                                LOG.error("Rollback failed", sqle);
                            }
                        }
                    } else {
                        try {
                            LOG.error("Attempting transaction rollback");
                            connection.rollback();
                        } catch(SQLException e) {
                            LOG.error("Rollback failed", e);
                        }
                    }

                    try {
                        connection.setAutoCommit(false);
                    } catch(SQLException e) {
                        // can ignore this one
                    }
                    if (abort) {
                        if (firstException != null) {
                            throw new MigrationFailedException(firstException);
                        }
                    }
                }
            } else {
                LOG.info("No schema migration scripts to apply");
            }
        }
        return completed;
    }

}
