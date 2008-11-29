package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.SuburbBean;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.*;

import org.jboss.envers.Versioned;

/**
 * A SuburbHandle for a Suburb
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("B")
@Versioned
public class Suburb extends Region {

    /** A special case SuburbHandle instance used to identify an invalid Suburb rather than a null value */
    public static final Suburb INVALID = invalid();

    private String postalCodePath;

    public Suburb(String name) {
        super(name, RegionTypes.Suburb);
        populateAttributes();
    }

    public Suburb(State state, PostalCode postCode, String name) {
        super(name, RegionTypes.Suburb);
        addParentRegion(state);
        addParentRegion(postCode);
        populateAttributes();
    }

    public Suburb(State state, String name) {
        super(name, RegionTypes.Suburb);
        addParentRegion(state);
        populateAttributes();
    }

    public Suburb(String statePath, String postCodePath, String name) {
        super(name, RegionTypes.Suburb);
        this.parentPath = statePath;
        this.postalCodePath = postCodePath;
        populateAttributes();
    }

    /*Use for editing a new suburb */
    public Suburb(State state) {
        super("", RegionTypes.Suburb);
        addParentRegion(state);
        populateAttributes();
    }
    
    public Suburb() {
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
     * Set the state for this suburb. If a state is already set, the current state is removed
     * @param state
     */
    public void setState(State state) {
        State existing = getState();

        if (existing == null) {
            addParentRegion(state);
        } else {
            if (existing != state) {
                // remove old, add new
                removeParentRegion(existing);
                addParentRegion(state);
            }
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

    @Basic
    @Column(name="PostalCodePath")
    public String getPostalCodePath() {
        return postalCodePath;
    }

    public void setPostalCodePath(String postalCodePath) {
        this.postalCodePath = postalCodePath;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        State state = getState();
        if (state != null) {
            this.parentPath = state.getPath();
        }
        this.path = PathHelper.joinPath(parentPath, key);
        PostalCode postalCode = getPostCode();
        if (postalCode != null) {
            this.postalCodePath = postalCode.getPath();
        }
    }

    /**
     * Create or update the denormalized index entity
     */
    @PrePersist
    protected void prePersist() {
        super.prePersist();
        if (regionIndex == null) {
             regionIndex = new SuburbBean(this);
        } else {
            // update the attribute of the index
            regionIndex.populateDenormalizedAttributes();
        }
    }

   
}