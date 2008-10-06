package com.blueskyminds.homebyfive.business;

import com.blueskyminds.homebyfive.business.accounting.Account;
import com.blueskyminds.homebyfive.business.pricing.Product;

import javax.persistence.*;

/**
 * A simplication of an entry in the ProductAccountMap used for persistence.
 * The in-memory map is maintained in nested hash-maps, but in persistence a simple single table is used.
 *
 * This is an implementation of AccountMapEntry for Product's, and it's main role is to make the
 * interfaces specific and provide the product-specific ORM annotations
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ProductAccountMapEntry extends AccountMapEntry<Product> {

    // ------------------------------------------------------------------------------------------------------

    public ProductAccountMapEntry(ProductAccountMap productAccountMap, String mapName, Product product, Account account) {
        super(productAccountMap, mapName, product, account);
    }

    /** Default constructor for ORM */
    protected ProductAccountMapEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the map this entry belongs to */
    @ManyToOne
    @JoinColumn(name="ProductAccountMapId")
    public ProductAccountMap getProductAccountMap() {
        return (ProductAccountMap) super.getAccountMap();
    }

    public void setProductAccountMap(ProductAccountMap productAccountMap) {
        super.setAccountMap(productAccountMap);
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
    @JoinColumn(name="ProductId")
    public Product getProduct() {
        return super.getItem();
    }

    protected void setProduct(Product product) {
        super.setItem(product);
    }

    @Override
    @Transient
    public Product getItem() {
        return super.getItem();
    }

    public void setItem(Product item) {
        super.setItem(item);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the account the product is mapped to */
    @ManyToOne
    @JoinColumn(name="AccountId")
    public Account getAccount() {
        return super.getAccount();
    }

    // ------------------------------------------------------------------------------------------------------




}
