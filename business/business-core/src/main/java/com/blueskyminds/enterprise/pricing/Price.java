package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.homebyfive.framework.framework.measurement.Quantity;
import com.blueskyminds.enterprise.taxpolicy.TaxPolicy;

import javax.persistence.*;

/**
 *
 * A Price is an association between a Monetary value (Money), a quantity of goods (Quantity) and Tax
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class Price {

    /** The money value */
    private Money money;

    /** The tax on the money value */
    private TaxPolicy tax;

    /** The quantity (and units) of goods that the money value corresponds to */
    private Quantity quantity;

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new price */
    public Price(Money money, TaxPolicy tax, Quantity quantity) {
        this.money = money;
        this.tax = tax;
        this.quantity = quantity;
    }

    /** Default constructor for ORM */
    protected Price() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="TaxPolicyId")
    public TaxPolicy getTaxPolicy() {
        return tax;
    }

    public void setTaxPolicy(TaxPolicy tax) {
        this.tax = tax;
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return money+"/"+quantity;
    }
}
