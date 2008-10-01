package com.blueskyminds.homebyfive.framework.framework.test;

import com.blueskyminds.homebyfive.framework.framework.DomainObject;
import com.blueskyminds.homebyfive.framework.framework.HasIdentity;
import com.blueskyminds.homebyfive.framework.framework.Printable;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceSession;
import com.blueskyminds.homebyfive.framework.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.homebyfive.framework.framework.persistence.jpa.dao.GenericDAO;
import com.blueskyminds.homebyfive.framework.framework.tools.FileTools;
import com.blueskyminds.homebyfive.framework.framework.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.framework.tools.filters.NonBlankFilter;
import com.blueskyminds.homebyfive.framework.framework.tools.filters.StringFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Methods that help implement unit tests
 *
 * Date Started: 18/08/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestTools {

    private static final Log LOG = LogFactory.getLog(TestTools.class);
        
    public static <T> Collection<T> findAll(Class<T> clazz, EntityManager em) {
        return new GenericDAO(em, clazz).findAll();
    }

     public static <T> T findById(Class<T> clazz, Object id, EntityManager em) {
        return (T) new GenericDAO(em, clazz).findById(id);
    }

    public static void createHSQLDB() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
    }

    /**
     * Get a JDBC connection to hsqldb:mem:mem
     **/
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    public static <T extends HasIdentity> void printAll(Class<T> clazz, PersistenceService persistenceService) {

        PersistenceSession session = null;

        System.out.println("--- List of "+clazz.getSimpleName()+"'s ---");

        try {
            session = persistenceService.openSession();

            Collection<T> items = persistenceService.findAll(clazz);

            for (T item : items) {
                if (DomainObject.class.isAssignableFrom(item.getClass())) {
                    ((DomainObject) item).print(System.out);
                } else {
                    System.out.print(item);
                }
            }

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        } finally{
            if (session != null) {
                try {

                    persistenceService.closeSession();
                }
                catch (PersistenceServiceException e) {
                    //
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public static <T extends HasIdentity> void printAll(EntityManager em, Class<T> clazz) {

        System.out.println("--- List of "+clazz.getSimpleName()+"'s ---");

        try {
            Collection<T> items = em.createQuery("from "+clazz.getName()).getResultList();

            for (T item : items) {
                if (item instanceof Printable) {
                    ((Printable)item).print(System.out);
                } else {
                    item.toString();
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static <T extends HasIdentity> void printAll(Class<T> clazz, EntityManager em) {
        printAll(em, clazz);
    }

    protected static void applySQLFiles(Connection connection, String[] sqlFiles) {
        StringFilter filter = new NonBlankFilter();
        String[] sqlLines;
        try {
            for (String file : sqlFiles) {
                URI location = ResourceTools.locateResource(file+".sql");
                if (location != null) {
                    sqlLines = FileTools.readTextFile(location, filter);
                    PersistenceTools.executeUpdate(connection, sqlLines);
                } else {
                    LOG.error("Could not find resource: "+file+".sql");
                }
            }
        } catch(SQLException e) {
            LOG.error("SQL error", e);
        } catch (IOException e) {
            LOG.error("Failed to read data", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
