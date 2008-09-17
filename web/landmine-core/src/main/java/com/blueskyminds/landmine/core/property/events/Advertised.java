package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaign;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import javax.persistence.*;
import java.util.Date;

/**
 * A property was put on the market (advertised)
 *
 * Date Started: 14/04/2008
 */
@Entity
@DiscriminatorValue("Advertised")
public class Advertised extends PremiseEvent {

    private PropertyAdvertisementCampaign campaign;

    public Advertised(Premise premise, Date dateApplied, PropertyAdvertisementCampaign campaign) {
        super(premise, dateApplied);
        this.campaign = campaign;
    }

    public Advertised(PropertyAdvertisementCampaign campaign) {
        this.campaign = campaign;
    }

    /** Default constructor for ORM */
    protected Advertised() {
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="CampaignId")
    public PropertyAdvertisementCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(PropertyAdvertisementCampaign campaign) {
        this.campaign = campaign;
    }

    @Transient
    public boolean isOfType(PropertyAdvertisementTypes type) {
        return campaign.isOfType(type);
    }

    /**
     * The name of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public String getSourceType() {
        return campaign.getClass().getSimpleName();
    }

    /**
     * The id of the entity that is the source of this event
     *
     * @return
     */
    @Transient
    public Long getSourceId() {
        return campaign.getId();
    }

    @Transient
    public String getDescription() {
        AskingPrice initialAskingPrice = campaign.getInitialAskingPrice();
        PropertyAdvertisementTypes type = campaign.getType();
        if (initialAskingPrice != null) {
            if (type != null) {
                return "Advertised "+ type.toString()+" "+initialAskingPrice.toString();
            } else {
                return "Advertised "+initialAskingPrice.toString();
            }
        } else {
            if (type != null) {
                return "Advertised "+ type.toString();
            } else {
                return "Advertised";
            }
        }
    }
}