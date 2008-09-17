package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.renovation.PropertyRenovation;

import javax.persistence.*;
import java.util.Date;

/**
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("Renovated")
public class Renovated extends PremiseEvent {

    private PropertyRenovation propertyRenovation;

    public Renovated(Premise premise, Date dateApplied, PropertyRenovation propertyRenovation) {
        super(premise, dateApplied);
        this.propertyRenovation = propertyRenovation;
    }

    public Renovated(PropertyRenovation propertyRenovation) {
        this.propertyRenovation = propertyRenovation;
    }

    /** Default constructor for ORM */
    protected Renovated() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="RenovationId")
    public PropertyRenovation getPropertyRenovation() {
        return propertyRenovation;
    }

    public void setPropertyRenovation(PropertyRenovation propertyRenovation) {
        this.propertyRenovation = propertyRenovation;
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return propertyRenovation.getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return propertyRenovation.getId();
    }

    @Transient
    public String getDescription() {
        return "Renovated";
    }
}
