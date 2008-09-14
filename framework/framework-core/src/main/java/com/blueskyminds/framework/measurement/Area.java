package com.blueskyminds.framework.measurement;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Embeddable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * Encapsulates an amount of area
 *
 * Measurements are intended to be immutable.  Public setters are provided for use by the object that the
 * Area is a component (is embedded)
 *
 * Date Started: 10/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Embeddable
public class Area {

    /** The type of units */
    protected UnitsOfArea units;

    /** The amount in major units of T */
    protected BigDecimal amount;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quanity of the specified amount and units */
    public Area(BigDecimal amount, UnitsOfArea units) {
        init(amount, units);
    }

    /** Default constructor for ORM */
    protected Area() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Area(double amount, UnitsOfArea units) {
        if ((Double.isInfinite(amount)) || (Double.isNaN(amount))) {
            throw new NumberFormatException("Measurement amount cannot be Infinity or NaN");
        } else {
            init(BigDecimal.valueOf(amount), units);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Area(float amount, UnitsOfArea units) {
        if ((Float.isInfinite(amount)) || (Float.isNaN(amount))) {
            throw new NumberFormatException("Measurement amount cannot be Infinity or NaN");
        } else {
            init(BigDecimal.valueOf(amount), units);
        }

    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Area(int amount, UnitsOfArea units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new quantity of the specified amount and units */
    public Area(long amount, UnitsOfArea units) {
        init(BigDecimal.valueOf(amount), units);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the quantity with the specified values */
    private void init(BigDecimal amount, UnitsOfArea units) {
        this.amount = amount;
        this.units = units;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Asserts that this instance has the same units as the specified argument.
     *
     * @param arg
     */
    private void assertSameUnitsAs(Area arg) {
        assert units.equals(arg.units) : "Measurement units mismatch";

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiply this quantity by the specified quantity and return the results.
     * The units must be identical
     */
    public Area multiply(Area other) {
        assertSameUnitsAs(other);
        return newInstance(getAmount().multiply(other.amount), this.units);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this quantity by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this quantity by the amount
     */
    public Area multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Multiplies this Area by the specified amount and returns the result
     * @param amount
     * @return result of multiplying this Area by the amount
     */
    public Area multiply(BigDecimal amount) {
        return newInstance(getAmount().multiply(amount), this.units);
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Factory method to create an instance of an Area object from the specified amount and units
     * @param amount
     * @return new Area instance with the specified amount and units
     */
    protected Area newInstance(BigDecimal amount, UnitsOfArea units) {
        return new Area(amount, units);
    }

    /**
     * Factory method to create a new empty (and invalid) instance that needs to be prepared via the setters
     * 
     * @return new empty Area instance
     */
    public static Area newInstance() {
        return new Area();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the amount of the quantity in the current units
     * @return amount in the current units
     */
    @Basic
    @Column(name="Area")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Change the amount
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) throws NumberFormatException {
        if (StringUtils.isNotBlank(amount)) {
            this.amount = new BigDecimal(amount);
        } else {
            this.amount = null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the units for the quantity
     * @return the units for the quantity
     */
    @Enumerated
    @Column(name="AreaUnits")
    public UnitsOfArea getUnits() {
        return units;
    }

    /**
     * Change the units.
     * @param units
     */
    public void setUnits(UnitsOfArea units) {
        this.units = units;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return amount + " " + units;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (amount != null ? !amount.equals(area.amount) : area.amount != null) return false;
        if (units != area.units) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (units != null ? units.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    // ------------------------------------------------------------------------------------------------------
}
