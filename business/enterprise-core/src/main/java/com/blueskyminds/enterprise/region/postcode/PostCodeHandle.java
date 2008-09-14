package com.blueskyminds.enterprise.region.postcode;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.postcode.PostCode;
import com.blueskyminds.enterprise.region.RegionTypes;

import javax.persistence.*;

/**
 * RegionHandle for a postcode
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("P")
public class PostCodeHandle extends RegionHandle {

    private PostCode postCode;

    protected PostCodeHandle(String name, PostCode postCode) {
        super(name, RegionTypes.PostCode);
        this.postCode = postCode;
    }

    protected PostCodeHandle() {
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="PostCodeId")
    public PostCode getPostCode() {
        if (postCode != null) {
            postCode.setRegionHandle(this);
        }
        return postCode;
    }

    public void setPostCode(PostCode postCode) {
        this.postCode = postCode;
    }

    @Transient
    protected Region getRegionTarget() {
        if (postCode != null) {
            postCode.setRegionHandle(this);
        }
        return postCode;
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
}
