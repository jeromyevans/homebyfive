package com.blueskyminds.homebyfive.framework.core.guice;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.blueskyminds.homebyfive.framework.core.persistence.schema.MigrationFailedException;
import com.blueskyminds.homebyfive.framework.core.persistence.schema.SchemaMigrator;
import com.blueskyminds.homebyfive.framework.core.persistence.schema.HibernateSchemaMigrator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.HibernateException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Execute DDL to bring the schema up to the correct revision.
 *
 * Date Started: 18/03/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SchemaMigrationInitializer {

    private EntityManagerFactory entityManagerFactory;

    private String schemaName;
    private String dialect;

    @Inject
    public SchemaMigrationInitializer(@Named("migrator.schemaName") String schemaName, @Named("migrator.dialect") String dialect) {
        this.schemaName = schemaName;
        this.dialect = dialect;
    }

    public void checkSchema() throws MigrationFailedException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session = (Session) em.getDelegate();
        try {
            Connection connection = session.connection();
            connection.setAutoCommit(false);

            SchemaMigrator schemaMigrator = new HibernateSchemaMigrator(schemaName);
            schemaMigrator.migrate(connection, dialect);
        } catch (HibernateException e) {
            throw new MigrationFailedException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Inject
    public void initialiseSchemaMigration(EntityManagerFactory entityManagerFactory) throws MigrationFailedException {
        this.entityManagerFactory = entityManagerFactory;
        checkSchema();
    }

}
