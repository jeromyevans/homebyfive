package com.blueskyminds.homebyfive.business.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("S")
public class StateBean extends RegionIndex {

    public StateBean() {
    }

    public StateBean(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
        populateDenormalizedAttributes();
    }

    public StateBean(State state) {
        super(state);
        this.abbr = state.getAbbr();
        populateDenormalizedAttributes();
    }

    /**
     * Lowercase unique state code in the country
     * @return
     */
    @Transient
    public String getStateId() {
        return key;
    }

    public void setStateId(String stateId) {
        this.key = stateId;
    }
   
    @Transient
    public State getStateHandle() {
        return (State) region;
    }

    public void setStateHandle(State stateHandle) {
        this.region = stateHandle;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateDenormalizedAttributes() {
        this.key = KeyGenerator.generateId(abbr);
        parent = getStateHandle().getCountry().getRegionIndex();                
        if (parent != null) {
            this.parentPath = parent.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
            this.status = DomainObjectStatus.Valid;
            this.type = RegionTypes.State;

            this.countryId = parent.getKey();
            this.countryPath = parent.getPath();
            this.countryName = parent.getName();
        }

        this.stateId = key;
        this.statePath = path;
        this.stateName = name;
    }

    public void mergeWith(RegionIndex otherState) {
        if (otherState instanceof StateBean) {
            getStateHandle().mergeWith(((StateBean) otherState).getStateHandle());
        }
    }

    @Transient
    public CountryBean getCountryBean() {
        return (CountryBean) parent;
    }
}