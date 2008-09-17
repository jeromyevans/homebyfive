package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.sale.PropertySale;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.enterprise.pricing.Money;

import javax.persistence.*;
import java.util.Date;

/**
 * Identifies that a property was sold
 *
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("Sold")
public class Sold extends PremiseEvent {

    private PropertySale propertySale;

    public Sold(Premise premise, Date dateApplied, PropertySale propertySale) {
        super(premise, dateApplied);
        this.propertySale = propertySale;
    }

    public Sold(PropertySale propertySale) {
        this.propertySale = propertySale;
    }

    /** Default constructor for ORM */
    protected Sold() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PropertySaleId")
    public PropertySale getPropertySale() {
        return propertySale;
    }

    public void setPropertySale(PropertySale propertySale) {
        this.propertySale = propertySale;
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return propertySale.getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return propertySale.getId();
    }

    @Transient
    public String getDescription() {
        Money salePrice = propertySale.getPrice();
        if (salePrice != null) {
            return "Sold "+salePrice.toString();
        } else {
            return "Sold";
        }
    }
}
