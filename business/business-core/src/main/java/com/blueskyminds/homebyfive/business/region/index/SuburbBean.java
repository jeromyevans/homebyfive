package com.blueskyminds.homebyfive.business.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("B")
public class SuburbBean extends RegionIndex {

    public SuburbBean() {
    }

    public SuburbBean(String name) {
        this.name = name;
        populateDenormalizedAttributes();
    }

    public SuburbBean(Suburb suburb) {
        super(suburb);
        populateDenormalizedAttributes();
    }

    /**
     * The handle for the Suburb implementation
     *
     * @return
     */
    @Transient
    public Suburb getSuburbHandle() {
        return (Suburb) region;
    }

    public void setSuburbHandle(Suburb suburbHandle) {
        this.region = suburbHandle;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateDenormalizedAttributes() {
        this.key = KeyGenerator.generateId(name);

        // the state is the parent
        parent = getSuburbHandle().getState().getRegionIndex();

        if (parent != null) {
            this.parentPath = parent.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
            this.status = DomainObjectStatus.Valid;
            this.type = RegionTypes.Suburb;

            this.countryId = parent.getCountryId();
            this.countryPath = parent.getCountryPath();
            this.countryName = parent.getCountryName();

            this.stateId = parent.getKey();
            this.statePath = parent.getPath();
            this.stateName = parent.getName();

//            this.postalCodeId = key;
//            this.postalCodePath = path;
//            this.postalCodeName = name;
        }

        this.suburbId = key;
        this.suburbPath = path;
        this.suburbName = name;
    }

    public void mergeWith(RegionIndex suburbBean) {
        if (suburbBean instanceof SuburbBean) {
            getSuburbHandle().mergeWith(((SuburbBean) suburbBean).getSuburbHandle());
        }
    }

    @Transient
    public StateBean getStateBean() {
        return (StateBean) getParent();
    }
}
