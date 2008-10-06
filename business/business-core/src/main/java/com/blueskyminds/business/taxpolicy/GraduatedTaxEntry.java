package com.blueskyminds.business.taxpolicy;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.measurement.Limits;

import javax.persistence.*;
import java.io.PrintStream;
import java.math.BigDecimal;

/**
 * Represents an entry in a graduated tax scale.
 *
 * Date Started: 2/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class GraduatedTaxEntry extends AbstractDomainObject {

    /** That graduated tax policy that this entry belongs to */
    private GraduatedTax graduatedTax;

    /** The from amount */
    private BigDecimal fromAmount;

    /** The limit/constraint applied to the from amount */
    private Limits fromLimit;

    /** The to amount */
    private BigDecimal toAmount;

    /** The limit/constraint applied to the to amount */
    private Limits toLimit;

    /** The fixed amount of tax to include */
    private BigDecimal fixedAmount;

    /** The percent amount of tax to apply within this graduation */
    private BigDecimal fixedPercent;

    // ------------------------------------------------------------------------------------------------------

    public GraduatedTaxEntry(GraduatedTax graduatedTax, BigDecimal fromAmount, Limits fromLimit, BigDecimal toAmount, Limits toLimit, BigDecimal fixedAmount, BigDecimal fixedPercent) {
        this.graduatedTax = graduatedTax;
        this.fromAmount = fromAmount;
        this.fromLimit = fromLimit;
        this.toAmount = toAmount;
        this.toLimit = toLimit;
        this.fixedAmount = fixedAmount;
        this.fixedPercent = fixedPercent;
    }

    /** Default constructor for ORM */
    protected GraduatedTaxEntry() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines whether the specified amount is within the range of this graduated tax entry */
    public boolean amountInRange(BigDecimal amount) {
        return ((testFromLimit(amount)) && (testToLimit(amount)));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determine if the from limit of this entry is ok for the given amount */
    private boolean testFromLimit(BigDecimal amount) {
        boolean okay = false;

        if (amount != null) {
            switch (fromLimit) {
                case Equal:
                    okay = amount.compareTo(fromAmount) == 0;
                    break;
                case GreaterThan:
                    okay = amount.compareTo(fromAmount) > 0;
                    break;
                case GreaterThanOrEqual:
                    okay = amount.compareTo(fromAmount) >= 0;
                    break;
                case LessThan:
                    okay = amount.compareTo(fromAmount) < 0;
                    break;
                case LessThanOrEqual:
                    okay = amount.compareTo(fromAmount) <= 0;
                    break;
                case NegativeInfinity:
                    okay = true;
                    break;
                case PositveInfinity:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determine if the to limit of this entry is ok for the given amount */
    private boolean testToLimit(BigDecimal amount) {
        boolean okay = false;

        if (amount != null) {
            switch (toLimit) {
                case Equal:
                    okay = amount.compareTo(toAmount) == 0;
                    break;
                case GreaterThan:
                    okay = amount.compareTo(toAmount) > 0;
                    break;
                case GreaterThanOrEqual:
                    okay = amount.compareTo(toAmount) >= 0;
                    break;
                case LessThan:
                    okay = amount.compareTo(toAmount) < 0;
                    break;
                case LessThanOrEqual:
                    okay = amount.compareTo(toAmount) <= 0;
                    break;
                case NegativeInfinity:
                    okay = false;
                    break;
                case PositveInfinity:
                    okay = true;
                    break;
            }
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /** On the assumption that the base amount fits within this entry, calculate the amount of tax
     *  on the base amount.
     *
     * This method should only been called once it has been asserted that the baseAmount is in the range
     * of this entry
     *
     * The calculate is:
     *   ((baseAmount - fromAmount) * fixedPercent) + fixedAmount
     *
     * @param baseAmount
     * @return calculated tax
     */
    public BigDecimal calculateTaxAmount(BigDecimal baseAmount) {

        BigDecimal excess = baseAmount.subtract(fromAmount);
        return fixedAmount.add(excess.multiply(fixedPercent));
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="GraduatedTaxId")
    public GraduatedTax getGraduatedTax() {
        return graduatedTax;
    }

    protected void setGraduatedTax(GraduatedTax graduatedTax) {
        this.graduatedTax = graduatedTax;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="FromAmount")
    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    protected void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="FromLimit")
    public Limits getFromLimit() {
        return fromLimit;
    }

    protected void setFromLimit(Limits fromLimit) {
        this.fromLimit = fromLimit;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="ToAmount")
    public BigDecimal getToAmount() {
        return toAmount;
    }

    protected void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="ToLimit")
    public Limits getToLimit() {
        return toLimit;
    }

    protected void setToLimit(Limits toLimit) {
        this.toLimit = toLimit;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="FixedAmount")
    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    protected void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="FixedPercent")
    public BigDecimal getFixedPercent() {
        return fixedPercent;
    }

    protected void setFixedPercent(BigDecimal fixedPercent) {
        this.fixedPercent = fixedPercent;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(fromLimit+" "+fromAmount+" - "+toLimit+" "+toAmount+": "+fixedAmount+", "+fixedPercent);
    }

    // ------------------------------------------------------------------------------------------------------
}
