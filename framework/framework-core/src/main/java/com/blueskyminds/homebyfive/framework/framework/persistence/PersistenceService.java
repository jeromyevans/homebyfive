package com.blueskyminds.homebyfive.framework.framework.persistence;

import com.blueskyminds.homebyfive.framework.framework.IdentityRef;
import com.blueskyminds.homebyfive.framework.framework.HasIdentity;
import com.blueskyminds.homebyfive.framework.framework.persistence.query.PersistenceQuery;
import com.blueskyminds.homebyfive.framework.framework.persistence.paging.Page;

import java.util.List;
import java.util.Collection;
import java.util.Properties;

/**
 * A standard gateway to access the persistence layer
 *
 * todo: phase out dependence on DomainObject - it should be able to work with any persistent entity
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface PersistenceService {

    // ------------------------------------------------------------------------------------------------------

     /** Open a session for a thread */
    PersistenceSession openSession() throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

     /** Close a session for a thread */
    void closeSession() throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the list of the specified domain objects.  Opens a new session or continues current session
     * depending on the persistence implementation.
     * @param clazz
     * @return List of entities of the specified class
     */
    <T extends HasIdentity> List<T> findAll(Class<T> clazz) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

     /** Save the specified domain object to the persistent store.
     * This version opens a session and performs the save as a transaction.  See the alternative method
     * to reuse an existing session or transaction.
     *
     * Returns the entity after saving.
     * If the DomainObject is new, it will now have an IdentityRef Field that uniquely identifies it in the
     * store.  */
    HasIdentity save(HasIdentity domainObject) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria
     * @return
     * @throws PersistenceServiceException
     */
    <T extends HasIdentity> Collection<T> find(Class<T> clazz, PersistenceQuery criteria) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria and returns the first valid result
     * @return
     * @throws PersistenceServiceException
     */
    <T extends HasIdentity> T findOne(Class<T> clazz, PersistenceQuery criteria) throws PersistenceServiceException;

     /**
     * Search for the domain object with the matching Id
     * @return an instance of the domain object
     * @throws PersistenceServiceException
      */
    <T extends HasIdentity> T findById(Class<T> clazz, Long id) throws PersistenceServiceException;

    /**
     * Search for the domain object with the matching identityRef
     * @return an instance of the domain object
     * @throws PersistenceServiceException
     */
    HasIdentity findById(IdentityRef identityRef) throws PersistenceServiceException;

    /**
     * Merge the state of the given DomainObject into the current persistence context
     *
     * @param domainObject
     * @return the result may not be the same instance of the domainObject, but it will have the same IdentityRef
     */
    <T extends HasIdentity> T merge(T domainObject) throws PersistenceServiceException;

    /**
     * Count the number of instances of the specified persistent entity
     *
     * @param clazz
     * @return count, as an int
     * @throws PersistenceServiceException
     */
    <T extends HasIdentity> long count(Class<T> clazz) throws PersistenceServiceException;

    /**
     * Returns a random instance of the specified domain object, fetched from the database
     *
     * @param clazz
     * @return the valid domain object instance, or null if there were none found
     * @throws PersistenceServiceException
     */
    <T extends HasIdentity> T random(Class<T> clazz) throws PersistenceServiceException;

    /**
     *  Returns a page containing persistent entities of the specified class.
     *
     * @param clazz
     * @param pageNo
     * @param pageSize
     * @return Page
     * @throws PersistenceServiceException
     */
    public <T> Page findPage(Class<T> clazz, int pageNo, int pageSize) throws PersistenceServiceException;

    /**
     * Search for any objects in persistence using the query, returning the requested page
     *
     * @param criteria
     * @param pageNo
     * @param pageSize
     * @return
     * @throws PersistenceServiceException
     */
    Page findPage(PersistenceQuery criteria, int pageNo, int pageSize) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /** Executes SQL directly
     * @param sqlLines lines of sql (one statement per line)
     * @return number of affected rows
     * @throws PersistenceServiceException if an SQL exception occurred
     **/
    int executeBulkUpdate(String[] sqlLines) throws PersistenceServiceException;

    /**
     * Execute an update operation in the specified query
     *
     * @param query containing the update operation
     *
     * @return number of affected entities
     * @throws PersistenceServiceException if the update can't be performed
     */
    int executeUpdate(PersistenceQuery query) throws PersistenceServiceException;
    // ------------------------------------------------------------------------------------------------------

    /**
     * Pass properties in to configure the PersistenceService
     *
     * @param properties
     */
    void setProperties(Properties properties);

}
