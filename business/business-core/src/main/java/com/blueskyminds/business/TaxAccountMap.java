package com.blueskyminds.business;

import com.blueskyminds.business.taxpolicy.TaxPolicy;
import com.blueskyminds.business.Enterprise;
import com.blueskyminds.business.AccountMap;
import com.blueskyminds.business.accounting.Account;

import javax.persistence.*;
import java.util.Collection;
import java.io.PrintStream;

/**
 * A mapping between tax policies and relationships in the chart of relationships for this enterprise
 *
 * Date Started: 23/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class TaxAccountMap extends AccountMap<TaxPolicy, TaxAccountMapEntry> {

    private static final String LIABILITY = "liability";

    public TaxAccountMap(Enterprise enterprise) {
        super(enterprise);
        init();
    }

    /** Default constructor for ORM */
    protected TaxAccountMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the tax account map with default properties */
    private void init() {
        defineAccountMap(LIABILITY);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Map the specified taxpolicy to a liability account.
     * Replaces existing entry for the tax policy
     * @param taxPolicy
     * @param account
     * @return true if added
     */
    public boolean mapLiabilityAccount(TaxPolicy taxPolicy, Account account) {
        return mapAccount(LIABILITY, taxPolicy,  account);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the liability account for the specified tax policy
     * @param taxPolicy
     * @return null if there's no liability account for that taxPolicy
     */
    @Transient
    public Account getLiabilityAccount(TaxPolicy taxPolicy) {
        return getAccount(LIABILITY, taxPolicy);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the Enterprise that this ProductAccountMap belongs to */
    @OneToOne
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the entries in this map unrolled into a simple collection.
     * For use by ORM to persist the data in the map. Converts the in-memory hashmap of hashmaps into
     * a simple collection of simple objects */
    @OneToMany(mappedBy = "taxAccountMap", cascade=CascadeType.ALL)
    protected Collection<TaxAccountMapEntry> getEntries() {
        return super.getEntries();    //To change body of overridden methods use File | Settings | File Templates.
    }

    // ------------------------------------------------------------------------------------------------------

    /** Factory method to create an entry for the AccountMap.  Called by the superclass. */
    protected TaxAccountMapEntry newEntry(String mapName, TaxPolicy item, Account account) {
        return new TaxAccountMapEntry(this, mapName, item, account);
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Tax Account Map ---");
        super.print(out);
    }
}
