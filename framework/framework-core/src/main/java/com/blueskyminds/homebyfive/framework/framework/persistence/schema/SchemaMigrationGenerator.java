package com.blueskyminds.homebyfive.framework.framework.persistence.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.dialect.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.ejb.Ejb3Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.io.File;
import java.util.*;

import com.blueskyminds.homebyfive.framework.framework.tools.FileTools;
import com.blueskyminds.homebyfive.framework.framework.tools.text.StringTools;
import com.blueskyminds.homebyfive.framework.framework.persistence.hsqldb.HsqlDbMemoryDataSourceFactory;

import javax.sql.DataSource;

/**
 *
 * This program:
 *   creates an empty hsqldb database
 *   applies the existing migration scripts to it
 *   calculates the difference
 *   writes the difference to the target directory in each dialect
 *
 */
public class SchemaMigrationGenerator {

    private static final Log LOG = LogFactory.getLog(SchemaMigrationGenerator.class);

    private static final String DELTA_SQL_FILENAME = "delta.sql";
    private static final String ORDER_FILENAME = "order.txt";
    private static final String UPDATE_SCHEMA_REVISION_TABLE_FILENAME = "updateSchemaRevisionTable.sql";
    private static final String CREATE_SCHEMA_REVISION_TABLE_FILENAME = "createSchemaRevisionTable.sql";
    private static final String BASELINE_SCHEMA_FILENAME= "hibernate_baseline.txt";

    private String schemaName;
    private String persistenceUnitName;
    private String targetPath;

    public SchemaMigrationGenerator(String persistenceUnitName, String schemaName, String targetPath) {
        this.persistenceUnitName = persistenceUnitName;
        this.schemaName = schemaName;
        this.targetPath = targetPath;
    }

    private String[] generateUpdateSchemaScript(Connection connection, Configuration configuration, Dialect dialect) throws SQLException{
        DatabaseMetadata dm = new DatabaseMetadata(connection, dialect);
        String[] updateSql = configuration.generateSchemaUpdateScript(dialect, dm);
        return updateSql;
    }

    private String[] generateCreateSchemaScript(Configuration configuration, Dialect dialect) throws SQLException{
        String[] createSql = configuration.generateSchemaCreationScript(dialect);
        return createSql;
    }

    public void start() {

        SchemaMigrator schemaMigrator = new HibernateSchemaMigrator(schemaName);

        Map<String, Dialect> dialects = new HashMap<String, Dialect>();
        dialects.put("hsql", new HSQLDialect());
        //dialects.put("postgres", new PostgreSQLDialect());
        dialects.put("mysql5", new MySQL5Dialect());
        dialects.put("innodb", new MySQL5InnoDBDialect());

        try {
            DataSource hsqlDataSource = HsqlDbMemoryDataSourceFactory.createDataSource("migrate", "sa", "");
            System.out.println("Creating HSQLDB in-memory database...");

            Connection connection = hsqlDataSource.getConnection();    // this connection needs to stay alive for hsqldb

            System.out.println("Migrating the schema...");
            schemaMigrator.migrate(connection, "hsql");
            SchemaRevision schemaRevision = new SchemaRevision(connection, "hsql");

            Integer revision = schemaRevision.lookupRevision(schemaName);
            System.out.println("Created an empty schema at revision: "+revision);

            System.out.println("Initialising PersistenceUnit...");
            Ejb3Configuration cfg = new Ejb3Configuration().configure(persistenceUnitName, new Properties());
            Configuration config = cfg.getHibernateConfiguration();

            for (Map.Entry<String, Dialect> dialect : dialects.entrySet()) {
                System.out.println("Generating migration scripts for "+dialect.getKey()+":");
                String[] updateSql = StringTools.appendSuffix(generateUpdateSchemaScript(connection, config, dialect.getValue()), ";");
                String[] createSql = StringTools.appendSuffix(generateCreateSchemaScript(config, dialect.getValue()), ";");

                writeDeltaScript(revision+1, dialect.getKey(), updateSql, createSql);
            }
        } catch(SQLException e) {
            LOG.error(e);
        } catch(IOException e) {
            LOG.error(e);
        } catch(MigrationFailedException e) {
            LOG.error(e);
        }
    }

    private void writeDeltaScript(int nextRevision, String name, String[] script, String[] createScript) throws IOException {

        name = StringUtils.lowerCase(name);

        String basePath = FileTools.concatenatePaths(File.separatorChar, targetPath, schemaName, Integer.toString(nextRevision)+"_next");
        String path = FileTools.concatenatePaths(basePath, name, File.separatorChar);
        if (script.length > 0) {
            LOG.info("Writing the "+name+" delta DDL to: "+new File(path).getAbsolutePath());
        } else {
            LOG.info(name+" schema is up-to-date.");
        }

        // write the file, even if its empty, so old versions are replaced
        FileTools.createDirectory(path);
        FileTools.writeTextFile(FileTools.concatenatePaths(path, DELTA_SQL_FILENAME), script);

        if (!FileTools.fileExists(FileTools.concatenatePaths(path, ORDER_FILENAME))) {
            // create a ready-to-use order file
            String[] orderLines = new String[1];
            orderLines[0] = DELTA_SQL_FILENAME;

            FileTools.writeTextFile(FileTools.concatenatePaths(path, ORDER_FILENAME), orderLines);
        }

        String updateScript;
        if (nextRevision == 0) {
            // create the example createSchemaRevisionTable script
            if (!FileTools.fileExists(FileTools.concatenatePaths(path, CREATE_SCHEMA_REVISION_TABLE_FILENAME))) {
                String[] updateLines = new String[2];
                updateLines[0] = "-- This table is required by the SchemaMigrator.  It will be created automatically if the SchemaMigrator is used.  " +
                        "If you create the tables manually and want the SchemaMigrator to resume from that point must run this script";
                updateLines[1] = SchemaRevision.CREATE_SCHEMA_REVISION_TABLE+";";
                FileTools.writeTextFile(FileTools.concatenatePaths(path, CREATE_SCHEMA_REVISION_TABLE_FILENAME), updateLines);
            }

            updateScript = "insert into SchemaRevision (revNo, schemaName) values (0, '"+schemaName+"');";
        } else {
            updateScript = "update SchemaRevision set revNo = "+nextRevision+" where schemaName = '"+schemaName+";'";
        }

        // create the example updateSchemaRevision script
        if (!FileTools.fileExists(FileTools.concatenatePaths(path, UPDATE_SCHEMA_REVISION_TABLE_FILENAME))) {
            String[] updateLines = new String[2];
            updateLines[0] = "-- If you apply the migration manually, use this script to update the SchemaRevision table to allow the SchemaMigrator to resume from this revision";
            updateLines[1] = updateScript;
            FileTools.writeTextFile(FileTools.concatenatePaths(path, UPDATE_SCHEMA_REVISION_TABLE_FILENAME), updateLines);
        }

        // create the baseline script
        FileTools.writeTextFile(FileTools.concatenatePaths(path, BASELINE_SCHEMA_FILENAME), createScript);

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the DDLGenerator with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("SchemaMigrationGenerator creates an empty in-memory database using the existing SQL migration scripts, compares it to the expected schema and writes new scripts containing the delta DDL.");
        if (args.length >= 3) {
            SchemaMigrationGenerator ddlGenerator = new SchemaMigrationGenerator(args[0], args[1], args[2]);
            System.out.println("persistenceUnitName: "+args[1]);
            System.out.println("schemaName: "+args[1]);
            System.out.println("targetPath: "+args[2]);
            ddlGenerator.start();
        } else {
            System.out.println("Usage: SchemaMigrationGenerator persistenceUnitName schemaName targetPath");
        }
    }
}
