package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.lease.PropertyLease;
import com.blueskyminds.landmine.core.property.lease.LeasePrice;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;

/**
 * Identifies that a property was Leased
 *
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("Leased")
public class Leased extends PremiseEvent {

    private PropertyLease propertyLease;

    public Leased(Premise premise, Date dateApplied, PropertyLease propertyLease) {
        super(premise, dateApplied);
        this.propertyLease = propertyLease;
    }

    public Leased(PropertyLease propertyLease) {
        this.propertyLease = propertyLease;
    }

    public Leased() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PropertyLeaseId")
    public PropertyLease getPropertyLease() {
        return propertyLease;
    }

    public void setPropertyLease(PropertyLease propertyLease) {
        this.propertyLease = propertyLease;
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return propertyLease.getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return propertyLease.getId();
    }

    @Transient
    public String getDescription() {
        LeasePrice leasePrice = propertyLease.getPrice();
        if (leasePrice != null) {
            return "Leased "+leasePrice.toString();
        } else {
            return "Leased";
        }

    }
}
