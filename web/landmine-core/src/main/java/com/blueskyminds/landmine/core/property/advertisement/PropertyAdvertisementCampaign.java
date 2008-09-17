package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

//import com.blueskyminds.landmine.core.property.Premise;

//import javax.persistence.Entity;

/**
 * A property advertisement campaign binds a set of related property advertisements to a premise
 *
 * Date Started: 15/04/2008
 */
@Entity
public class PropertyAdvertisementCampaign extends AbstractDomainObject {

    private Premise premise;
    private Set<PropertyAdvertisement> propertyAdvertisements;
    private PropertyAdvertisementTypes type;

    public PropertyAdvertisementCampaign(Premise premise, PropertyAdvertisement propertyAdvertisement) {
        this.premise = premise;
        this.propertyAdvertisements = new HashSet<PropertyAdvertisement>();
        addPropertyAdvertisement(propertyAdvertisement);
    }

    public PropertyAdvertisementCampaign() {
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @OneToMany(mappedBy="campaign", cascade= CascadeType.ALL)
    public Set<PropertyAdvertisement> getPropertyAdvertisements() {
        return propertyAdvertisements;
    }

    public void setPropertyAdvertisements(Set<PropertyAdvertisement> propertyAdvertisements) {
        this.propertyAdvertisements = propertyAdvertisements;
    }

    /**
     * Adds the advertisement to this campaign
     * Inherits the type from the advertisement if not yet set for the campaign
     *
     * @param propertyAdvertisement
     */
    public void addPropertyAdvertisement(PropertyAdvertisement propertyAdvertisement) {
        propertyAdvertisements.add(propertyAdvertisement);
        propertyAdvertisement.setCampaign(this);
        if (this.getType() == null) {
            this.setType(propertyAdvertisement.getType());
        }
    }

    @Enumerated
    public PropertyAdvertisementTypes getType() {
        return type;
    }

    public void setType(PropertyAdvertisementTypes type) {
        this.type = type;
    }


    @Transient
    public boolean isOfType(PropertyAdvertisementTypes type) {
        if (type != null) {
            return type.equals(getType());
        } else {
            return getType() == null;
        }
    }

    /** Get the property advertisement applicable on or before the specified date */
    @Transient
    public PropertyAdvertisement getLastPropertyAdvertisementWithPrice(Date date) {
        PropertyAdvertisement closest = null;
        for (PropertyAdvertisement propertyAdvertisement : propertyAdvertisements) {
            if (propertyAdvertisement.getPrice() != null) {
                Date dateListed = propertyAdvertisement.getDateListed();
                if ((dateListed.before(date)) || (DateUtils.isSameDay(dateListed, date))) {
                    if (closest == null) {
                        closest = propertyAdvertisement;
                    } else {
                        // this one is closer than the current
                        if (dateListed.after(closest.getDateListed())) {
                            closest = propertyAdvertisement;
                        }
                    }
                }
            }
        }
        return closest;
    }

    /** Get the property advertisement applicable on or after the specified date */
    @Transient
    public PropertyAdvertisement getNextPropertyAdvertisementWithPrice(Date date) {
        PropertyAdvertisement closest = null;
        for (PropertyAdvertisement propertyAdvertisement : propertyAdvertisements) {
            if (propertyAdvertisement.getPrice() != null) {
                Date dateListed = propertyAdvertisement.getDateListed();
                if ((dateListed.after(date)) || (DateUtils.isSameDay(dateListed, date))) {
                    if (closest == null) {
                        closest = propertyAdvertisement;
                    } else {
                        // this one is closer than the current
                        if (dateListed.after(closest.getDateListed())) {
                            closest = propertyAdvertisement;
                        }
                    }
                }
            }
        }
        return closest;
    }

    /**
     * The date of the first advertisement
     * @return
     */
    @Transient
    public Date getInitialDate() {
        PropertyAdvertisement first = getInitialAdvertisement();
        if (first != null) {
            return first.getDateListed();
        } else {
            return null;
        }        
    }

    /**
     * Get the first advertisement in the campaign
     * @return
     */
    @Transient
    public PropertyAdvertisement getInitialAdvertisement() {
        Date firstListed = null;
        PropertyAdvertisement first = null;

        for (PropertyAdvertisement propertyAdvertisement : propertyAdvertisements) {
            Date dateListed = propertyAdvertisement.getDateListed();
            if ((firstListed == null) || (dateListed.before(firstListed))) {
                first = propertyAdvertisement;
                firstListed = propertyAdvertisement.getDateListed();
            }
        }
        return first;
    }

    /**
     * Get the first advertisement in the campaign that includes an asking price
     * @return
     */
    @Transient
    public PropertyAdvertisement getInitialAdvertisementWithPrice() {
        Date firstListed = null;
        PropertyAdvertisement first = null;

        for (PropertyAdvertisement propertyAdvertisement : propertyAdvertisements) {
            if (propertyAdvertisement.getPrice() != null) {
                Date dateListed = propertyAdvertisement.getDateListed();
                if ((firstListed == null) || (dateListed.before(firstListed))) {
                    first = propertyAdvertisement;
                    firstListed = propertyAdvertisement.getDateListed();
                }
            }
        }
        return first;
    }

    /**
     * Get the asking price of the first advertisement in the campaign
     *
     * @return
     */
    @Transient
    public AskingPrice getInitialAskingPrice() {
        PropertyAdvertisement first = getInitialAdvertisementWithPrice();
        if (first != null) {
            return first.getPrice();
        } else {
            return null;
        }
    }
}
