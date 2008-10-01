package com.blueskyminds.homebyfive.framework.framework.measurement;

import javax.persistence.Embeddable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * A generalised representation of a measured Quantity
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class Quantity {

    /** The type of quantity units */
    private QuantityUnits units;

    /** The amount in major units of the QuantityUnits */
    private BigDecimal amount;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quanity of the specified amount and units */
    public Quantity(BigDecimal amount, QuantityUnits units) {
        init(amount, units);
    }

    /** Default constructor for ORM */
    protected Quantity() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Quantity(double amount, QuantityUnits units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Quantity(float amount, QuantityUnits units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Quantity(int amount, QuantityUnits units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Quantity(long amount, QuantityUnits units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the quantity with the specified values */
    private void init(BigDecimal amount, QuantityUnits units) {
        this.amount = amount;
        this.units = units;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the amount of the quantity in the current units
     * @return amount in the current units
     */
    @Basic
    @Column(name="Quantity")
    public BigDecimal getAmount() {
        return amount;
    }
    
    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the units for the quantity
     * @return the units for the quantity
     */
    @Enumerated
    @Column(name="QtyUnits")
    public QuantityUnits getUnits() {
        return units;
    }

    protected void setUnits(QuantityUnits units) {
        this.units = units;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Asserts that this instance has the same units as the specified argument.
     *
     * @param arg
     */
    private void assertSameUnitsAs(Quantity arg) {
        assert units.equals(arg.units) : "Quantity units mismatch";

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiply this quantity by the specified quantity and return the results.
     * The units must be identifcal
     */
    public Quantity multiply(Quantity other) {
        assertSameUnitsAs(other);
        return newQuantity(getAmount().multiply(other.amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this quantity by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this quantity by the amount
     */
    public Quantity multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this money by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this money by the amount
     */
    public Quantity multiply(BigDecimal amount) {
        return newQuantity(getAmount().multiply(amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Factory method to create an instance of a money object from the specified amount
     * @param amount
     * @return new Money instance with the specified amount
     */
    private Quantity newQuantity(BigDecimal amount) {
        return new Quantity(amount, this.units);                
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return amount+" "+units;
    }
}
