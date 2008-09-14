package com.blueskyminds.enterprise;

import com.blueskyminds.enterprise.accounting.Account;
import com.blueskyminds.enterprise.taxpolicy.TaxPolicy;

import javax.persistence.*;

/**
 * A simplication of an entry in the TaxAccountMap used for persistence.
 * The in-memory map is maintained in nested hash-maps, but in persistence a simple single table is used.
 *
 * This is an implementation of AccountMapEntry for TaxPolicy's, and it's main role is to make the
 * interfaces specific and provide the product-specific ORM annotations
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class TaxAccountMapEntry extends AccountMapEntry<TaxPolicy> {

    // ------------------------------------------------------------------------------------------------------

    public TaxAccountMapEntry(TaxAccountMap taxAccountMap, String mapName, TaxPolicy taxPolicy, Account account) {
        super(taxAccountMap, mapName, taxPolicy, account);
    }

    /** Default constructor for ORM */
    protected TaxAccountMapEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the map this entry belongs to */
    @ManyToOne
    @JoinColumn(name="TaxAccountMapId")
    public TaxAccountMap getTaxAccountMap() {
        return (TaxAccountMap) super.getAccountMap();
    }

    public void setTaxAccountMap(TaxAccountMap taxAccountMap) {
        super.setAccountMap(taxAccountMap);
    }

    @Transient
    public AccountMap getAccountMap() {
        return super.getAccountMap();
    }

    protected void setAccountMap(AccountMap accountMap) {
        super.setAccountMap(accountMap);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the sub-map that this entry belongs to */
    @Basic
    @Column(name="MapName")
    public String getMapName() {
        return super.getMapName();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the product being mapped */
    @ManyToOne
    @JoinColumn(name="TaxPolicyId")
    public TaxPolicy getTaxPolicy() {
        return super.getItem();
    }

    protected void setTaxPolicy(TaxPolicy taxPolicy) {
        super.setItem(taxPolicy);
    }

    @Override
    @Transient
    public TaxPolicy getItem() {
        return super.getItem();
    }

    public void setItem(TaxPolicy item) {
        super.setItem(item);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the account the tax is mapped to */
    @ManyToOne
    @JoinColumn(name="AccountId")
    public Account getAccount() {
        return super.getAccount();
    }

    // ------------------------------------------------------------------------------------------------------

}
