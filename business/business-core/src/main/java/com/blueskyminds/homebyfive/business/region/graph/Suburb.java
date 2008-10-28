package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
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

    /*Use for editing a new suburb */
    public Suburb(State state) {
        super("", RegionTypes.Suburb);
        addParentRegion(state);
        populateAttributes();
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

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        State state = getState();
        if (state != null) {
            this.parentPath = state.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
        }
    }
   
}