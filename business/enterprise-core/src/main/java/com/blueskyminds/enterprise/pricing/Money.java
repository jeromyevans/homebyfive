package com.blueskyminds.enterprise.pricing;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A monetary amount with currency.
 *
 * Represents something generally accepted as a medium of exchange, a measure of value, or a means of payment.
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class Money implements Comparable {

    /** The amount.
     * Note that the amount is stored in this major units with a scale derived from the currency
     */
    private BigDecimal amount;
    private Currency currency;

    /** The rounding mode to use in calculations */
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    // ------------------------------------------------------------------------------------------------------

    /** Create Money specified in the major units of the currency */
    public Money(double amount, Currency currency) {
        init(new BigDecimal(amount),  currency);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create Money specified in the major units of the currency */
    public Money(String amount, Currency currency) {
        init(new BigDecimal(amount), currency);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create Money specified in the major units of the currency */
    public Money(BigDecimal amount, Currency currency) {
        init(amount,  currency);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * A special-case money instance, initialised with zero value and no currency.
     * Currency will be inherited through operations performed on the instance.
     *
     * Also used as default constructor for ORM
     */
    public Money() {
        init(new BigDecimal("0.0"), null);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the amount and currency for the scaling of the currency */
    private void init(BigDecimal amount, Currency currency) {
        this.currency = currency;
        if (currency != null) {
            this.amount = amount.setScale(currency.getDefaultFractionDigits(), ROUNDING_MODE);
        }
        else {
            // an amount without a currency or scale (yet)
            this.amount = amount;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /***
     * Determines if this money instance has been initialised
     * @return true if the money has been initialised - has an amount and currency
     */
    @Transient
    public boolean isNull() {
        return (currency == null);
    }

    // ------------------------------------------------------------------------------------------------------

    /***
     * Determines if this money instance has zero value
     * @return true if the money has zero value
     */
    @Transient
    public boolean isZero() {
        // must use compare-to, not equals, to compare by value excluding scale
        return (amount.compareTo(BigDecimal.ZERO) == 0);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds ths specified money to this money and returns the result
     * @param other
     * @return result of adding the specified money to this money
     */
    public Money add(Money other) {
        assertSameCurrencyAs(other);

        return newMoney(amount.add(other.amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds ths specified money to this money, updating this value
     * @param other
     * @return this, after adding the amount
     */
    public Money sum(Money other) {
        assertSameCurrencyAs(other);

        amount = amount.add(other.amount);

        return this;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Subtracts ths specified money from this money, updating this value
     * @param other
     * @return this, after subtracting the amount
     */
    public Money minus(Money other) {
        assertSameCurrencyAs(other);

        amount = amount.subtract(other.amount);

        return this;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Subtracts the specified money from this money and returns the result
     * @param other
     * @return result of subtracting the specified money from this money
     */
    public Money subtract(Money other) {
        assertSameCurrencyAs(other);

        return newMoney(amount.subtract(other.amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this money by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this money by the amount
     */
    public Money multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this money by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this money by the amount
     */
    public Money multiply(BigDecimal amount) {
        return newMoney(amount().multiply(amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Factory method to create an instance of a money object from the specified amount
     * @param amount
     * @return new Money instance with the specified amount
     */
    private Money newMoney(BigDecimal amount) {
        Money money = new Money();
        money.init(amount,  currency);
        return money;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
      Get the amount of this Money instance in the currency's major units.
     */
    public BigDecimal amount() {
        return amount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Asserts that this instance was the same currency as the specified argument.
     * If the currency of this instance was never set, then the currency is inherited from the argument
     * (this only works once, and only for the special-case money constructor where currency is deliberately
     * not set)
     *
     * @param arg
     */
    private void assertSameCurrencyAs(Money arg) {
        if (currency == null) {
            // inherit the currency from the argument
            currency = arg.currency;
        }
        else {
            assert currency.equals(arg.currency) : "Money math mismatch";
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public int compareTo(Object other) {
        if (other instanceof Money) {
            return compareTo((Money) other);
        }
        else {
            return -1;
        }          
    }

    // ------------------------------------------------------------------------------------------------------

    /** Compare two money instances (by value and currency) */
    public int compareTo(Money other) {
        assertSameCurrencyAs(other);
        if (amount.compareTo(other.amount) < 0) {
            return -1;
        }
        else {
            if (amount.compareTo(other.amount) == 0) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Tests equality by value */
    public boolean equals(Object other) {
        return ((other instanceof Money) && (equals((Money)other)));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Tests equality by value */
    public boolean equals(Money other) {
        return (currency.equals(other.currency) && (amount.compareTo(other.amount) == 0));
    }

    // ------------------------------------------------------------------------------------------------------

    public int hashCode() {
        int result;
        result = (int) (amount.longValue() ^ (amount.longValue() >>> 32));
        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the value preceeded by the currency */
    public String toString() {
        NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();

        return currency+""+currencyFormat.format(amount);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the amount for ORM */
    @Basic
    protected BigDecimal getAmount() {
        return amount;
    }

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the currency as a code for ORM */
    @Basic
    @Column(name="Currency", length=4)
    protected String getCurrencyCode() {
        if (currency != null) {
            return currency.getCurrencyCode();
        }
        else {
            return null;
        }
    }

    protected void setCurrencyCode(String currencyCode) {
        if (currencyCode != null) {
            this.currency = Currency.getInstance(currencyCode);
        }
        else {
            this.currency = null;
        }
    }

    @Transient
    protected Currency getCurrency() {
        return currency;
    }

    protected void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
