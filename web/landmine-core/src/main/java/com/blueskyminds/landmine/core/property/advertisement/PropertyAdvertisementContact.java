package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Maps an agent (Individual) to a PropertyAdvertisement
 *
 * Date Started: 10/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class PropertyAdvertisementContact extends AbstractDomainObject {

    private PropertyAdvertisement propertyAdvertisement;
    private Individual individual;

    public PropertyAdvertisementContact(PropertyAdvertisement propertyAdvertisement, Individual individual) {
        this.propertyAdvertisement = propertyAdvertisement;
        this.individual = individual;
        init();
    }

    /**
     * Default constructor for ORM
     */
    protected PropertyAdvertisementContact() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertyAdvertisementAgent with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "PropertyAdvertisementId")
    public PropertyAdvertisement getPropertyAdvertisement() {
        return propertyAdvertisement;
    }

    public void setPropertyAdvertisement(PropertyAdvertisement propertyAdvertisement) {
        this.propertyAdvertisement = propertyAdvertisement;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "IndividualId")
    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
}
