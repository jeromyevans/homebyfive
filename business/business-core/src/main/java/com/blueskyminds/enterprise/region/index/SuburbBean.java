package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.*;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("B")
public class SuburbBean extends RegionBean {

    public SuburbBean() {
    }

    public SuburbBean(String name) {
        this.name = name;
        populateAttributes();
    }

    public SuburbBean(Suburb suburb) {
        super(suburb);
        populateAttributes();
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
    public void populateAttributes() {
        this.parentPath = getParent().getPath();
        this.key = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.Suburb;
    }

    public void mergeWith(RegionBean suburbBean) {
        if (suburbBean instanceof SuburbBean) {
            getSuburbHandle().mergeWith(((SuburbBean) suburbBean).getSuburbHandle());
        }
    }

}
