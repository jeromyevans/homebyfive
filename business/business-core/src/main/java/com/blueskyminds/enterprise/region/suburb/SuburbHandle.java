package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.street.StreetHandle;
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
public class SuburbHandle extends RegionHandle implements SuburbI {

    /** A special case SuburbHandle instance used to indentify an invalid Suburb rather than a null value */
    public static final SuburbHandle INVALID = invalid();

    public SuburbHandle(String name) {
        super(name, RegionTypes.Suburb);
    }

    protected SuburbHandle() {
    }

    /**
     * Associate the specified street with this suburb
     */
    public StreetHandle addStreet(StreetHandle street) {
        addChildRegion(street);
        return street;
    }

    /** Determine if this suburb contains the specified street */
    public boolean contains(StreetHandle street) {        
        return hasChild(street);
    }
     /**
     * Gets the parent StateHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public StateHandle getState() {
        RegionHandle parent = getParent(RegionTypes.State);
        if (parent != null) {
            return (StateHandle) parent.unproxy().getModel();
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
    public PostCodeHandle getPostCode() {
         RegionHandle parent = getParent(RegionTypes.PostCode);
         if (parent != null) {
            return (PostCodeHandle) parent.unproxy().getModel();
         } else {
             return null;
         }
    }

    private static SuburbHandle invalid() {
        SuburbHandle invalid = new SuburbHandle();
        invalid.setName("INVALID");
        invalid.setStatus(DomainObjectStatus.Deleted);
        return invalid;
    }

    @Transient
    public boolean isInvalid() {
        return this.equals(INVALID);
    }
}