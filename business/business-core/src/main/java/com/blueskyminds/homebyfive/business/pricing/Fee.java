package com.blueskyminds.homebyfive.business.pricing;

import com.blueskyminds.homebyfive.business.accounting.Account;

import javax.persistence.*;

/**
 *
 * A fee is a standard cost that can be included in a CommercialDocument
 * The fee itself doesn't have a price - the contract applies a price to this fee
 *
 * A fee is a product
 *
 * Date Started: 12/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class Fee extends Product {

    /** The revenue account that this product is associated with when sold by the Enterprise */
    private Account revenueAccount;

    /** The type of fee */
    private FeeTypes type;

    /** The name of the fee */
    private String name;

    // -------------------------------------------------------------------------------------------------------

    /**
     *
    * Create a new fee of the specified type.
    *
    * @param type
    */
    public Fee(String name, FeeTypes type) {
        this.name = name;
        this.type = type;
    }

    /** Default constructor for ORM */
    protected Fee() {

    }

    // -------------------------------------------------------------------------------------------------------

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -------------------------------------------------------------------------------------------------------


    @Enumerated
    public FeeTypes getType() {
        return type;
    }

    public void setType(FeeTypes type) {
        this.type = type;
    }

    // -------------------------------------------------------------------------------------------------------

    @Transient
    public Account getRevenueAccount() {
        return revenueAccount;
    }

    public void setRevenueAccount(Account revenueAccount) {
        this.revenueAccount = revenueAccount;
    }

    // -------------------------------------------------------------------------------------------------------

}
