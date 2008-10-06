package com.blueskyminds.homebyfive.business;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.business.accounting.Account;

/**
 * A simplication of an entry in the AccountMap used for persistence.
 * The in-memory map is maintained in nested hash-maps, but in persistence a simple single table is used.
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AccountMapEntry<T> extends AbstractDomainObject {

    /** The map that this entry belongs to */
    private AccountMap accountMap;

    /** The unqiue name of the map */
    private String mapName;

    /** The item being mapped */
    private T item;

    /** The account it's mapped to */
    private Account account;

    // ------------------------------------------------------------------------------------------------------

    public AccountMapEntry(AccountMap accountMap, String mapName, T item, Account account) {
        this.accountMap = accountMap;
        this.mapName = mapName;
        this.item = item;
        this.account = account;
    }

    /** Default constructor for ORM */
    protected AccountMapEntry() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the map this entry belongs to */
    public AccountMap getAccountMap() {
        return accountMap;
    }

    protected void setAccountMap(AccountMap accountMap) {
        this.accountMap = accountMap;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the sub-map that this entry belongs to */
    public String getMapName() {
        return mapName;
    }

    protected void setMapName(String mapName) {
        this.mapName = mapName;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the item being mapped */
    public T getItem() {
        return item;
    }

    protected void setItem(T item) {
        this.item = item;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the account the item is mapped to */
    public Account getAccount() {
        return account;
    }

    protected void setAccount(Account account) {
        this.account = account;
    }

    // ------------------------------------------------------------------------------------------------------
}
