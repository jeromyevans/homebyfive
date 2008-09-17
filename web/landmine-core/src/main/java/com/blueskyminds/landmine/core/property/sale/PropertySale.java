package com.blueskyminds.landmine.core.property.sale;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;

/**
 * Information about the sale of a property
 *
 * Date Started: 14/04/2008
 */
@Entity
@Table(name="PropertySale")
public class PropertySale extends AbstractDomainObject {

    private Premise premise;
    private Date dateExchanged;
    private Date dateSettled;
    private Money price;
    private Organisation vendorsAgent;

    public PropertySale(Premise premise) {
        this.premise = premise;
    }

    /** Default constructor for ORM */
    public PropertySale() {
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
    @Column(name="DateExchanged")
    public Date getDateExchanged() {
        return dateExchanged;
    }

    public void setDateExchanged(Date dateExchanged) {
        this.dateExchanged = dateExchanged;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateSettled")
    public Date getDateSettled() {
        return dateSettled;
    }

    public void setDateSettled(Date dateSettled) {
        this.dateSettled = dateSettled;
    }

    @Embedded
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
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
