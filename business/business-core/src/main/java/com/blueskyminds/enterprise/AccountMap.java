package com.blueskyminds.enterprise;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.accounting.Account;
import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.framework.DomainObject;

import javax.persistence.Transient;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;
import java.io.PrintStream;

/**
 * An account map is used to map a type of entity to Accounts
 *
 * The entity, of class T, may be any type of object.
 *
 * Date Started: 23/05/2006
 *
 * History:
 *
 * T: the entity type being mapped to an Account
 * E: the entry class info the map
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class AccountMap<T extends DomainObject, E extends AccountMapEntry<T>> extends AbstractDomainObject {

    /** The enterprise that this map was created for */
    private Enterprise enterprise;

    /** A map of account maps, identified by name */
    private Map<String, Map<T, Account>> accountMap;

    /** A persistent collection of the AccountMapEntries - these are mapped into the Map for faster access */
    private Collection<E> entries;

    // ------------------------------------------------------------------------------------------------------

    public AccountMap(Enterprise enterprise) {
        this.enterprise = enterprise;
        init();
    }

    /** Default constructor for ORM */
    protected AccountMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the account map with default properties */
    private void init() {
        accountMap = new HashMap<String, Map<T, Account>>();
        entries = new HashSet<E>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new account map with the specified name
     * @param mapName
     */
    protected void defineAccountMap(String mapName) {
        accountMap.put(mapName,  new HashMap<T, Account>());
    }

    // ------------------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------------------

    /**
     * Map the specified entity to a revenue account.
     * Replaces existing entry for that entity
     * @param entity
     * @param account
     * @return true if added, false if mapName not recognised
     */
    public boolean mapAccount(String mapName, T entity, Account account) {
        // lookup the name of the map
        Map<T, Account> map = accountMap.get(mapName);
        if (map != null) {
            map.put(entity,  account);
            return true;
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the account for the specified entity
     * @param entity
     * @return null if there's no account for that entity, or the mapName isn't recognised
     */
    public Account getAccount(String mapName, T entity) {
        // lookup the name of the map
        Map<T, Account> map = accountMap.get(mapName);
        if (map != null) {
            return map.get(entity);
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the entire underlying map in this AccountMap */
    @Transient
    public Map<String, Map<T, Account>> getAccountMap() {
        return accountMap;
    }

    /** Set the entire underlying map in this AccountMap */
    protected void setAccountMap(Map<String, Map<T, Account>> accountMap) {
        this.accountMap = accountMap;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the specified inner map identified by mapName */
    protected Map<T, Account> getInnerMap(String mapName) {
        return accountMap.get(mapName);
    }

    // ------------------------------------------------------------------------------------------------------

    public Enterprise getEnterprise() {
        return enterprise;
    }

    protected void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the persistent collection of Entries for this AccountMap.  Subclasses need to override
     * the annotation for this method
     *
     * @return persistent collection of entries
     */
    protected Collection<E> getEntries() {
        entries.clear();

        // loop though the top-level map names
        for (String mapName : getAccountMap().keySet()) {
            Map<T, Account> map = getInnerMap(mapName);
            // now loop through the values in this inner-map
            for (T item : map.keySet()) {
                // create an entry
                entries.add(newEntry(mapName, item, map.get(item)));
            }
        }

        return entries;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Factory method to create an Entry for the AccountMap */
    protected abstract E newEntry(String mapName, T item, Account account);

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Update the hashmap using the collection of entries.  The collection is optimised into the
     * hashmap of hashmaps for fast in-memory use.
     * @param entries
     */
    protected void setEntries(Collection<E> entries) {
        Map<String, Map<T, Account>> newMap = new HashMap<String, Map<T, Account>>();
        Map<T, Account> innerMap;

        this.entries = entries;

        for (E entry : entries) {
            // check if the mapname exists
            if (newMap.containsKey(entry.getMapName())) {
                innerMap = newMap.get(entry.getMapName());
            }
            else {
                // create a new inner map
                innerMap = new HashMap<T, Account>();
                newMap.put(entry.getMapName(), innerMap);
            }

            // update the inner map
            innerMap.put(entry.getItem(), entry.getAccount());
        }

        // update the in-memory map
        setAccountMap(newMap);
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        // loop though the top-level map names
        for (String mapName : getAccountMap().keySet()) {
            out.println(mapName+":");

            Map<T, Account> map = getInnerMap(mapName);
            // now loop through the values in this inner-map
            for (T item : map.keySet()) {
                // create an entry
                out.println("   "+item.getIdentityName()+" "+map.get(item).getIdentityName()+" ("+map.get(item).getName()+")");
            }
        }
    }
}


