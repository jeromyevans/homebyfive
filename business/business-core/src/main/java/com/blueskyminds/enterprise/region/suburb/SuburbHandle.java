package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.address.Street;
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

    private Suburb suburb;

    protected SuburbHandle(String name) {
        super(name, RegionTypes.Suburb);
    }

    protected SuburbHandle() {
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="SuburbId")
    public Suburb getSuburb() {
        this.suburb.setRegionHandle(this);
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    /**
     * Associate the specified street with this suburb
     */
    public Street addStreet(Street street) {
        return suburb.addStreet(street);
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