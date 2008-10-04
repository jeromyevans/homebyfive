package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.*;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("S")
public class StateBean extends RegionBean {

    private String abbr;

    public StateBean() {
    }

    public StateBean(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
        populateAttributes();
    }

    public StateBean(State state) {
        super(state);
        this.abbr = state.getAbbreviation();
        populateAttributes();
    }

    /**
     * Lowercase unique state code in the country
     * @return
     */
    @Transient
    public String getStateId() {
        return key;
    }

    protected void setStateId(String stateId) {
        this.key = stateId;
    }

    @Basic
    @Column(name="Abbr")
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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
    public void populateAttributes() {
        this.parentPath = getParent().getPath();
        this.key = KeyGenerator.generateId(abbr);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.State;
    }

    public void mergeWith(RegionBean otherState) {
        if (otherState instanceof StateBean) {
            getStateHandle().mergeWith(((StateBean) otherState).getStateHandle());
        }
    }

}