package com.blueskyminds.landmine.core.property.renovation;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;

/**
 * Information about the renovation of a property
 *
 * Date Started: 14/04/2008
 */
@Entity
@Table(name="PropertyRenovation")
public class PropertyRenovation extends AbstractDomainObject {

    private Premise premise;
    private Date dateComplete;

    public PropertyRenovation(Premise premise, Date dateComplete) {
        this.premise = premise;
        this.dateComplete = dateComplete;
    }

    public PropertyRenovation() {
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateCompleted")
    public Date getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(Date dateComplete) {
        this.dateComplete = dateComplete;
    }
}
