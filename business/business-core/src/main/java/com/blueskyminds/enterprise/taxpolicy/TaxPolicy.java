package com.blueskyminds.enterprise.taxpolicy;

import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

/**
 * A Tax policy is used to represent the tax on a commerial item
 *
 * Date Started: 22/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="TaxType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Genric")
public abstract class TaxPolicy extends AbstractDomainObject {

    /** The name of this tax */
    private String name;

    /** The currency applied */
    private Currency currency;

    /** The start date (inclusive) that this tax scale is applicable */
    private Date startDate;

    /** The end date (inclusive) that this tax scale is applicable */
    private Date endDate;

    // ------------------------------------------------------------------------------------------------------

    protected TaxPolicy(String name, Currency currency, Date startDate, Date endDate) {
        this.name = name;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** Default constructor for ORM */
    protected TaxPolicy() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of this tax policy */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /** Get the currency code for the enterprise */
    @Basic
    @Column(name="CurrencyCode")
    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    public void setCurrencyCode(String currencyCode) {
        this.currency = Currency.getInstance(currencyCode);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the date that this tax comes into effect.  A null value means N/A (always) */
    @Temporal(TemporalType.DATE)
    @Column(name="StartDate")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the date that this tax stops being in effect.  A null value means N/A (indefinite) */
    @Temporal(TemporalType.DATE)
    @Column(name="EndDate")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines if the tax policy is applicable on the specified date. */
    @Transient
    public boolean isApplicable(Date date) {
        boolean startOk = false;
        boolean endOk = false;


        if (date != null) {
            // check the start date, if defined
            if (startDate != null) {
                if ((DateUtils.isSameDay(date, startDate)) || (date.after(startDate))) {
                    startOk = true;
                }
            }
            else {
                startOk = true;
            }

            // check the end date, if defined
            if (endDate != null) {
                if ((DateUtils.isSameDay(date, endDate)) || (date.before(endDate))) {
                    endOk = true;
                }
            }
            else {
                endOk = true;
            }
        }

        return (startOk && endOk);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculate the tax on the specified base amount */
    @Transient
    public abstract Money calculateTaxAmount(Money baseAmount);

    /** Returns true if the tax is a fixed amoount */
    @Transient
    public abstract boolean isFixedTax();

    /** Returns true if the tax is a graduated amount */
    @Transient
    public abstract boolean isGraduatedTax();

    /** The percentage of fixed tax on the amount */
    @Transient
    public abstract BigDecimal getFixedPercent();

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+"("+getName()+"): "+getCurrencyCode()+" StateDate: "+getStartDate()+" endDate:"+getEndDate());
    }

    // ------------------------------------------------------------------------------------------------------
}
