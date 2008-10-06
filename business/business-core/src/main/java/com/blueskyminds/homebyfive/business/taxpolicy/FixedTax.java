package com.blueskyminds.homebyfive.business.taxpolicy;

import com.blueskyminds.homebyfive.business.pricing.Money;

import javax.persistence.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

/**
 * An implementation of a TaxPolicy for fixed taxes.  Provides common methods for implementation of fixed
 * taxes.
 *
 * Date Started: 22/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Fixed")
public class FixedTax extends TaxPolicy {

    /** The fixed tax percentage */
    private BigDecimal taxPercent;

    //--------------------------------------------------------------------------------------------------

    public FixedTax(String name, BigDecimal taxPercent, Currency currency, Date startDate, Date endDate) {
        super(name, currency, startDate, endDate);
        this.taxPercent = taxPercent;
    }

    /** Default constructor for ORM */
    protected FixedTax() {

    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    /** Calculate the tax on the specified base amount */
    public Money calculateTaxAmount(Money baseAmount) {
        return baseAmount.multiply(taxPercent);
    }

    //--------------------------------------------------------------------------------------------------

    /** Returns true if the tax is a fixed amoount */
    @Transient
    public boolean isFixedTax() {
        return true;
    }

    //--------------------------------------------------------------------------------------------------

    /** Returns true if the tax is a graduated amount */
    @Transient
    public boolean isGraduatedTax() {
        return false;
    }

    //--------------------------------------------------------------------------------------------------

    /** The percentage of fixed tax on the amount */
    @Basic
    @Column(name="FixedPercent")
    public BigDecimal getFixedPercent() {
        return taxPercent;
    }

    protected void setFixedPercent(BigDecimal fixedPercent) {
        this.taxPercent = fixedPercent;
    }

    //--------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+"("+getName()+"): "+getCurrencyCode()+" StateDate: "+getStartDate()+" endDate:"+getEndDate()+ " Percent: "+getFixedPercent());
    }
}
