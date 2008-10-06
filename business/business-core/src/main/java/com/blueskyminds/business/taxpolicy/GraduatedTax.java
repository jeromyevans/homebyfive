package com.blueskyminds.business.taxpolicy;

import com.blueskyminds.business.pricing.Money;

import javax.persistence.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Definition of a graduated tax
 *
 * Date Started: 2/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Graduated")
public class GraduatedTax extends TaxPolicy {

    /** The entries in this graduated tax scale */
    private List<GraduatedTaxEntry> entries;

    // ------------------------------------------------------------------------------------------------------

    public GraduatedTax(String name, Currency currency, Date startDate, Date endDate) {
        super(name, currency, startDate,  endDate);
        init();
    }

    /** Default constructor for ORM */
    protected GraduatedTax() {

    }

    /** Initialise the graduated tax scale with default attributes */
    private void init() {
        entries = new LinkedList<GraduatedTaxEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add an entry to this graduated tax scale */
    public void defineEntry(GraduatedTaxEntry graduatedTaxEntry) {
        entries.add(graduatedTaxEntry);
    }

    // todo: needs methods for updating the scales and checking that there's no conflicting entries

    // ------------------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------------------

    /** Calculate the amount of tax on the base amount using this graduated tax policy */
    public Money calculateTaxAmount(Money baseAmount) {
        Money tax = null;

        for (GraduatedTaxEntry entry : entries) {
            if (entry.amountInRange(baseAmount.amount())) {
                tax = new Money(entry.calculateTaxAmount(baseAmount.amount()), getCurrency());
            }
        }

        return tax;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns false */
    @Transient
    public boolean isFixedTax() {
        return false;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true */
    @Transient
    public boolean isGraduatedTax() {
        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns null */
    @Transient
    public BigDecimal getFixedPercent() {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------
    /** Get the entries in this GraduatedTax */
    @OneToMany(mappedBy = "graduatedTax", cascade=CascadeType.ALL)
    public List<GraduatedTaxEntry> getEntries() {
        return entries;
    }

    protected void setEntries(List<GraduatedTaxEntry> entries) {
        this.entries = entries;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+"("+getName()+"): "+getCurrencyCode()+" StateDate: "+getStartDate()+" endDate:"+getEndDate());
        for (GraduatedTaxEntry entry : getEntries()) { 
            out.print("   ");
            entry.print(out);
        }
    }
    // ------------------------------------------------------------------------------------------------------
}
