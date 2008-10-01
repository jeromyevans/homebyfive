package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.region.RegionMixin;
import com.blueskyminds.enterprise.region.DefaultRegionImpl;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.*;
import com.blueskyminds.enterprise.address.SuburbStreetMap;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Collection;
import java.util.HashSet;

/**
 *  A suburb or town within a state of a country
 *
 * A suburb always has at least one name and a type, and belongs to exactly one state
 *
 * Note: A suburb is a region who's parent is a State
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 16:56:55
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
public class Suburb extends DefaultRegionImpl implements Region, SuburbI {

    /** Streets defined in this suburb */
    private Collection<SuburbStreetMap> suburbStreetMaps;

    protected Suburb(String name) {
        //this.regionXMixin = new RegionMixin(new SuburbHandle(name, this), RegionTypes.Suburb);
        this.regionXMixin = new RegionMixin(RegionTypes.Suburb);
        init();
    }

    protected Suburb() {
        this.regionXMixin = new RegionMixin();
        init();
    }

    /** Initialise the suburb with default attributes */
    private void init() {
        suburbStreetMaps = new HashSet<SuburbStreetMap>();
    }

    /** Determine if this suburb contains the specified street */
    public boolean contains(Street street) {
        boolean found = false;
        for (SuburbStreetMap map : suburbStreetMaps) {
            if (map.getStreet().equals(street)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /** Associate the specified street with this suburb */
    public Street addStreet(Street street) {
        Street added = null;

        if (!contains(street)) {
            if (suburbStreetMaps.add(new SuburbStreetMap(this, street))) {
                added = street;
            }
        }
        return added;
    }

    /** Get the mappings of Streets to this Suburb
     * @return SuburbStreetMaps */
    @OneToMany(mappedBy = "suburb", cascade = CascadeType.ALL)
    protected Collection<SuburbStreetMap> getSuburbStreetMaps() {
        return suburbStreetMaps;
    }

    protected void setSuburbStreetMaps(Collection<SuburbStreetMap> suburbStreetMaps) {
        this.suburbStreetMaps = suburbStreetMaps;
    }

    /**
     * Merge the properties of another Suburb into this Suburb instance
     *
     * The streets defined in the other suburb are copied over to this suburb
     *
     * @param otherRegion
     * @throws com.blueskyminds.homebyfive.framework.core.MergeUnsupportedException
     */
    public <T extends DomainObject> void mergeWith(T otherRegion) {
        super.mergeWith(otherRegion);

        if (((AbstractEntity) otherRegion).isInstance(Suburb.class)) {

            Suburb otherSuburb = (Suburb) otherRegion;

            for (SuburbStreetMap map : otherSuburb.getSuburbStreetMaps()) {
                addStreet(map.getStreet());
            }
        }
    }
}
