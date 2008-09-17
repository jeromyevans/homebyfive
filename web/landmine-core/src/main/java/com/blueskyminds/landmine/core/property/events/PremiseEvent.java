package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;

/**
 * Events for premises include:
 *  - it was advertised for sale
 *  - the asking price was reduced
 *  - it was sold
 *  - a renovation was completed
 *  - it was constructed
 *
 * Date Started: 14/04/2008
 */
@Entity
@Table(name="PremiseEvent")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Impl")
@DiscriminatorValue("abstract")
public abstract class PremiseEvent extends AbstractDomainObject {

    private Premise premise;
    private Date dateApplied;

    protected PremiseEvent(Premise premise, Date dateApplied) {
        this.premise = premise;
        this.dateApplied = dateApplied;
    }

    /** Default constructor for ORM */
    protected PremiseEvent() {
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateApplied")
    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    /**
     * True if this event occurred within 2 months before the specified date
     *
     * @param date
     * @return
     */
    public boolean within2MonthsBefore(Date date) {
        if (getDateApplied() != null) {
            return DateUtils.add(getDateApplied(), Calendar.MONTH, 2).before(date);
        }
        return false;
    }

    /**
     * True if this event occurred within 2 months before the specified date
     *
     * @param date
     * @return
     */
    public boolean within2MonthsAfter(Date date) {
        if (getDateApplied() != null) {
            return DateUtils.add(date, Calendar.MONTH, 2).before(getDateApplied());
        }
        return false;
    }

    /**
     * The name of the entity that is the source of this event
     * @return
     */
    @Transient
    public abstract String getSourceType();

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public abstract Long getSourceId();

    @Transient
    public abstract String getDescription();
}
