package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Transient;
import java.util.Date;

/**
 * The property was constructed
 * 
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("Constructed")
public class Constructed extends PremiseEvent {

    public Constructed(Premise premise, Date dateApplied) {
        super(premise, dateApplied);
    }

    protected Constructed() {
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return getId();
    }

    @Transient
    public String getDescription() {
        return "Constructed";
    }
}
