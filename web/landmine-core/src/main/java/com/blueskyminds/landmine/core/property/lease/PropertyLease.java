package com.blueskyminds.landmine.core.property.lease;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.party.Organisation;

import javax.persistence.*;
import java.util.Date;


/**
 * Information about the lease for a property
 *
 * Date Started: 14/04/2008
 */
@Entity
@Table(name="PropertyLease")
public class PropertyLease extends AbstractDomainObject {

    private Premise premise;
    private Date dateLeased;
    private LeasePrice price;
    private Organisation vendorsAgent;

    public PropertyLease(Premise premise) {
        this.premise = premise;
    }

    /** Default constructor for ORM */
    public PropertyLease() {
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
    @Column(name="DateLeased")
    public Date getDateLeased() {
        return dateLeased;
    }

    public void setDateLeased(Date dateLeased) {
        this.dateLeased = dateLeased;
    }

    @Embedded
    public LeasePrice getPrice() {
        return price;
    }

    public void setPrice(LeasePrice price) {
        this.price = price;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AgentId")
    public Organisation getVendorsAgent() {
        return vendorsAgent;
    }

    public void setVendorsAgent(Organisation vendorsAgent) {
        this.vendorsAgent = vendorsAgent;
    }
}
