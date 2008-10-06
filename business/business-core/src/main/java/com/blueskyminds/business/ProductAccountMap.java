package com.blueskyminds.business;

import com.blueskyminds.business.accounting.Account;
import com.blueskyminds.business.pricing.Product;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Collection;


/**
 * Maps products within an enterprise to the Account to use for the income or expense financial transactions
 *
 * Date Started: 21/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ProductAccountMap extends AccountMap<Product, ProductAccountMapEntry> {

    private static final String REVENUE = "revenue";
    private static final String EXPENSE = "expense";

    // ------------------------------------------------------------------------------------------------------

    public ProductAccountMap(Enterprise enterprise) {
        super(enterprise);

        init();
    }

    /** Default constructor of ORM */
    protected ProductAccountMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the product account map with default properties */
    private void init() {
        defineAccountMap(REVENUE);
        defineAccountMap(EXPENSE);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Map the specified product to a revenue account.
     * Replaces existing entry for the product
     * @param product
     * @param account
     * @return true if added
     */
    public boolean mapRevenueAccount(Product product, Account account) {
        return mapAccount(REVENUE, product,  account);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Map the specified product to an expense account.
     * Replaces existing entry for the product
     * @param product
     * @param account
     * @return true if added
     */
    public boolean mapExpenseAccount(Product product, Account account) {
        return mapAccount(EXPENSE, product,  account);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the expense account for the specified product
     * @param product
     * @return null if there's no expense account for that product
     */
    @Transient
    public Account getExpenseAccount(Product product) {
        return getAccount(EXPENSE, product);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the revenue account for the specified product
     * @param product
     * @return null if there's no revenue account for that product
     */
    @Transient
    public Account getRevenueAccount(Product product) {
        return getAccount(REVENUE, product);
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
    @OneToMany(mappedBy = "productAccountMap", cascade=CascadeType.ALL)
    protected Collection<ProductAccountMapEntry> getEntries() {
        return super.getEntries();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Factory method to create an entry for the AccountMap.  Called by the superclass. */
    protected ProductAccountMapEntry newEntry(String mapName, Product item, Account account) {
        return new ProductAccountMapEntry(this, mapName, item, account);
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Product Account Map ---");
        super.print(out);
    }
}
