package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.Street;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.*;

/**
 * A SuburbHandle for a Suburb
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("B")
public class Suburb extends Region {

    /** A special case SuburbHandle instance used to indentify an invalid Suburb rather than a null value */
    public static final Suburb INVALID = invalid();

    public Suburb(String name) {
        super(name, RegionTypes.Suburb);
    }

    protected Suburb() {
    }

    /**
     * Associate the specified street with this suburb
     */
    public Street addStreet(Street street) {
        addChildRegion(street);
        return street;
    }

    /** Determine if this suburb contains the specified street */
    public boolean contains(Street street) {
        return hasChild(street);
    }
     /**
     * Gets the parent StateHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public State getState() {
        Region parent = getParent(RegionTypes.State);
        if (parent != null) {
            return (State) parent.unproxy().getModel();
        } else {
            return null;
        }
    }

     /**
     * Gets the parent PostCodeHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public PostalCode getPostCode() {
         Region parent = getParent(RegionTypes.PostCode);
         if (parent != null) {
            return (PostalCode) parent.unproxy().getModel();
         } else {
             return null;
         }
    }

    private static Suburb invalid() {
        Suburb invalid = new Suburb();
        invalid.setName("INVALID");
        invalid.setStatus(DomainObjectStatus.Deleted);
        return invalid;
    }

    @Transient
    public boolean isInvalid() {
        return this.equals(INVALID);
    }
}