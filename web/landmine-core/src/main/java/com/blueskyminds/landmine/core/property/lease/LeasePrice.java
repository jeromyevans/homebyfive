package com.blueskyminds.landmine.core.property.lease;

import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.datetime.PeriodTypes;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.Column;
import javax.persistence.Embedded;

/**
 * The price on a lease
 *
 * Date Started: 14/04/2008
 */
@Embeddable
public class LeasePrice {

    private Money price;
    private PeriodTypes period;

    @Embedded
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    /**
      * The period attached to this price, if used.
     *   eg. per week
     * @return period
     */
    @Enumerated
    @Column(name="Period")
    public PeriodTypes getPeriod() {
        return period;
    }

    protected void setPeriod(PeriodTypes period) {
        this.period = period;
    }
}
