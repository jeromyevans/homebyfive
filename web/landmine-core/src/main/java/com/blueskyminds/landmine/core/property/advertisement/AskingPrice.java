package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.datetime.PeriodTypes;

import javax.persistence.*;

/**
 * Represents an asking price - an asking price may be a exact, a range, or relate to a limit
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Embeddable
public class AskingPrice implements Comparable {

    /** An enumeration identifying this price type */
    private AskingPriceTypes type;
    private Money lowerPrice;
    private Money upperPrice;
    private PeriodTypes period;

    // ------------------------------------------------------------------------------------------------------

    /** Create a price where there's no actual value, but a type.
     *  eg. Negotiable, Price on Application
     * @param type
     */
    public AskingPrice(AskingPriceTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create an exact price for the amount of money */
    public AskingPrice(Money price) {
        this.type = AskingPriceTypes.Exact;
        this.lowerPrice = price;
        this.period = PeriodTypes.OnceOff;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a price for the a price range */
    public AskingPrice(Money lowerPrice, Money upperPrice) {
        this.type = AskingPriceTypes.Range;
        this.lowerPrice = lowerPrice;
        this.upperPrice = upperPrice;
        this.period = PeriodTypes.OnceOff;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a price for the amount of money that's specified for a period */
    public AskingPrice(Money price, PeriodTypes period) {
        this.type = AskingPriceTypes.Exact;
        this.lowerPrice = price;
        this.period = period;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a price for the a price range that's specified for a period */
    public AskingPrice(Money lowerPrice, Money upperPrice, PeriodTypes period) {
        this.type = AskingPriceTypes.Range;
        this.lowerPrice = lowerPrice;
        this.upperPrice = upperPrice;
        this.period = period;
    }

// ------------------------------------------------------------------------------------------------------

    /** Create an exact price for the amount of money with a type enumeration.
     *   eg. 'Above' */
    public AskingPrice(Money price, AskingPriceTypes type) {
        this.type = type;
        this.lowerPrice = price;
        this.period = PeriodTypes.OnceOff;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a price for the amount of money that's specified for a period with a type enumeration
     *  eg. 'Above' */
    public AskingPrice(Money price, PeriodTypes period, AskingPriceTypes type) {
        this.type = type;
        this.lowerPrice = price;
        this.period = period;
    }

    // ------------------------------------------------------------------------------------------------------
    
    /** Default constructor for ORM */
    protected AskingPrice() {

    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** True if this price has a range */
    @Transient
    public boolean hasPriceRange() {
        return AskingPriceTypes.Range.equals(type);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the lower value in this price */
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="LowerAmt")),
            @AttributeOverride(name="currencyCode", column = @Column(name="LowerCurr"))
            })
    public Money getLowerPrice() {
        return lowerPrice;
    }

    protected void setLowerPrice(Money lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    /** Get the price value.  If the price is a range the getLower and getUpper properties should be used*/
    @Transient
    public Money getPrice() {
        return lowerPrice;
    }

    // ------------------------------------------------------------------------------------------------------
    /** Get the upper value in this price, if used */
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="UpperAmt")),
            @AttributeOverride(name="currencyCode", column = @Column(name="UpperCurr"))
            })
    public Money getUpperPrice() {
        return upperPrice;
    }

    protected void setUpperPrice(Money upperPrice) {
        this.upperPrice = upperPrice;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** The period attached to this price, if used.
     *   eg. per week
     * @return period
     */
    @Enumerated
    @Column(name="Period")
    public PeriodTypes getPeriod() {
        return period;
    }

    public void setPeriod(PeriodTypes period) {
        this.period = period;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Return a new price that equals this price adjusted by the specified percentage.  Useful for testing
     *
     *
     *
     *  NOTE: if the price was a range, the lower price is increased by the percentage and the
     *    upper price increased to maintain the same range (ie. the range is not adjusted)
     *
     * @param percentage  1.0 = 100% (no change)
     * @return AskingPrice - new instance with the adjusted values (this is not affected)
     * */
    public AskingPrice adjustedPrice(Double percentage) {
        AskingPrice newPrice;
        if (hasPriceRange()) {
            Money difference = upperPrice.subtract(lowerPrice);
            Money lower = lowerPrice.multiply(percentage);
            Money upper = lowerPrice.add(difference);
            newPrice = new AskingPrice(lower, upper, period);
        } else {
            Money lower = lowerPrice.multiply(percentage);
            newPrice = new AskingPrice(lower, period);
        }

        return newPrice;
    }

    // -----------------------------------------------------------------------------------------------------

    /** Return a duplicate of this price */
    public AskingPrice duplicate() {
        AskingPrice newPrice = new AskingPrice(type);
        
        newPrice.setLowerPrice(lowerPrice);
        newPrice.setUpperPrice(upperPrice);
        newPrice.setPeriod(period);

        return newPrice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (lowerPrice != null) {
            if (upperPrice != null) {
                builder.append(lowerPrice + " - "+upperPrice);
            } else {
                builder.append(lowerPrice);
            }
            if (period != null) {
                builder.append(" "+period);
            }
        }
        return builder.toString();
    }

    /**
     * Compares this asking price with another asking price.
     * If the two prices have the same period they are compared based on the lower values (or upper if defined)
     * If the prices
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o != null) {
            if (o instanceof AskingPrice) {
                AskingPrice other = (AskingPrice) o;

                if (getPeriod().equals(other.getPeriod())) {
                    // they have the same period - direct comparison accounting for range
                    if (upperPrice != null) {
                        if (other.getUpperPrice() != null) {
                            return upperPrice.compareTo(other.getUpperPrice());
                        } else {
                            return upperPrice.compareTo(other.getLowerPrice());
                        }
                    } else {
                        if (other.getUpperPrice() != null) {
                            return lowerPrice.compareTo(other.getUpperPrice());
                        } else {
                            return lowerPrice.compareTo(other.getLowerPrice());
                        }
                    }
                } else {
                    // perform a comparison normalizing to the same period

                    Money normalizedUpper = (upperPrice != null ? upperPrice.multiply(getPeriod().occurrencesInOneYear()) : null);
                    Money otherNormalizedUpper = (other.getUpperPrice() != null ? other.getUpperPrice().multiply(other.getPeriod().occurrencesInOneYear()) : null);

                    Money normalizedLower = (lowerPrice != null ? lowerPrice.multiply(getPeriod().occurrencesInOneYear()) : null);
                    Money otherNormalizedLower = (other.getLowerPrice() != null ? other.getLowerPrice().multiply(other.getPeriod().occurrencesInOneYear()) : null);

                    if (normalizedUpper != null) {
                        if (otherNormalizedUpper != null) {
                            return normalizedUpper.compareTo(otherNormalizedUpper);
                        } else {
                            return normalizedUpper.compareTo(otherNormalizedLower);
                        }
                    } else {
                        if (normalizedLower != null) {
                            if (otherNormalizedUpper != null) {
                                return normalizedLower.compareTo(otherNormalizedUpper);
                            } else {
                                return normalizedLower.compareTo(otherNormalizedLower);
                            }
                        }
                    }
                }

            }
        }
        return 0;
    }

    // -----------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
