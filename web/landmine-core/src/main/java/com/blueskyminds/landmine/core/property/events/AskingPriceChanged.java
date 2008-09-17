package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;

/**
 * The asking price of a property changed within an advertising campaign
 * 
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("AskingPriceChanged")
public class AskingPriceChanged extends PremiseEvent {

    private PropertyAdvertisement fromAdvertisement;
    private PropertyAdvertisement toAdvertisement;
    private AskingPriceChangeType type;

    public AskingPriceChanged(Premise premise, Date dateApplied, PropertyAdvertisement fromAdvertisement, PropertyAdvertisement toAdvertisement, AskingPriceChangeType type) {
        super(premise, dateApplied);
        this.fromAdvertisement = fromAdvertisement;
        this.toAdvertisement = toAdvertisement;
        this.type = type;
    }

    public AskingPriceChanged(PropertyAdvertisement fromAdvertisement, PropertyAdvertisement toAdvertisement, AskingPriceChangeType type) {
        this.fromAdvertisement = fromAdvertisement;
        this.toAdvertisement = toAdvertisement;
        this.type = type;
    }

    /** Default constructor for ORM */
    protected AskingPriceChanged() {
    }

    @ManyToOne
    @JoinColumn(name="FromAdvertisementId")
    public PropertyAdvertisement getFromAdvertisement() {
        return fromAdvertisement;
    }

    public void setFromAdvertisement(PropertyAdvertisement fromAdvertisement) {
        this.fromAdvertisement = fromAdvertisement;
    }

    @ManyToOne
    @JoinColumn(name="ToAdvertisementId")
    public PropertyAdvertisement getToAdvertisement() {
        return toAdvertisement;
    }

    public void setToAdvertisement(PropertyAdvertisement toAdvertisement) {
        this.toAdvertisement = toAdvertisement;
    }

    @Enumerated
    @Column(name="AskingPriceChangeType")
    public AskingPriceChangeType getType() {
        return type;
    }

    public void setType(AskingPriceChangeType type) {
        this.type = type;
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return toAdvertisement.getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return toAdvertisement.getId();
    }

    @Transient
    public String getDescription() {
        AskingPrice price = toAdvertisement.getPrice();
        if (price != null) {
            return "Asking Price "+type.toString()+" to "+ price.toString();
        } else {
            return "Asking Price "+type.toString();
        }
    }
}
