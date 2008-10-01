package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.framework.core.MergeUnsupportedException;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.PrintStream;

/**
 * Default implementation of a Region that provides all the common behaviour and attributes
 *  for a Region entity.
 *
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@MappedSuperclass
public class DefaultRegionImpl extends AbstractDomainObject implements Region, RegionHandleAware {

    protected RegionMixin regionXMixin;

    @Enumerated
    @Column(name = "Type")
    public RegionTypes getType() {
        return regionXMixin.getType();
    }

    public void setType(RegionTypes type) {
        regionXMixin.setType(type);
    }

    @Transient
    public String getName() {
        return regionXMixin.getName();
    }

    public void setName(String name) {
        regionXMixin.setName(name);
    }

     /**
     * Add a subregion to this region
     * The other region is not updated to point back to this parent
     *
     * @param childRegion
     * @return true if added ok
     */
    public boolean addChildRegion(Region childRegion) {
        return regionXMixin.addChildRegion(childRegion);
    }

    /**
     * Add the other region as a parent of this region.
     * The other region is not updated to point to this child
     *
     * @param parentRegion
     * @return true if added ok
     */
    public boolean addParentRegion(Region parentRegion) {
        return regionXMixin.addParentRegion(parentRegion);
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getName()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+getName()+")");
    }

    /**
     * A reference to the RegionHandle for this Region instance.
     * @return
     */
    @Transient
    public RegionHandle getRegionHandle() {
        return regionXMixin.getRegionHandle();
    }

    public void setRegionHandle(RegionHandle regionProxy) {
        regionXMixin.setRegionHandle(regionProxy);
    }

    /**
     * Merge the properties of another Region into this Region instance
     *
     * The default implementation only copies the simple properties over to this region.  Specialized regions should
     * extend this to copy collections
     *
     * @param otherRegion
     * @throws MergeUnsupportedException
     */
    public <T extends DomainObject> void mergeWith(T otherRegion) {
        if (((AbstractEntity) otherRegion).isInstance(DefaultRegionImpl.class)) {
            super.mergeSimpleProperties(otherRegion);
        }        
    }
}
