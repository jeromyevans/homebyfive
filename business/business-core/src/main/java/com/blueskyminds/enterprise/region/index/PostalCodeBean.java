package com.blueskyminds.enterprise.region.index;

import javax.persistence.*;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.tools.KeyGenerator;

/**
 * Denormalized postcode bean for fast lookup by path
 *
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("P")
public class PostalCodeBean extends RegionBean {
    
    public PostalCodeBean() {
    }

    public PostalCodeBean(String name) {
        this.name = name;
        populateAttributes();
    }

    public PostalCodeBean(PostalCode postCodeHandle) {
        super(postCodeHandle);
        populateAttributes();
    }

    @Transient
    public String getPostCodeId() {
        return key;
    }

    protected void setPostCodeId(String postCodeId) {
        this.key = postCodeId;
    }

    @Transient
    public PostalCode getPostCodeHandle() {
        return (PostalCode) region;
    }

    public void setPostCodeHandle(PostalCode postCodeHandle) {
        this.region = postCodeHandle;
    }

    @Enumerated
    @Column(name="Status")
    public DomainObjectStatus getStatus() {
        return status;
    }

    public void setStatus(DomainObjectStatus status) {
        this.status = status;
    }
    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.parentPath = getParent().getPath();
        this.key = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.PostCode;
    }

    public void mergeWith(RegionBean otherPostCode) {
        if (otherPostCode instanceof PostalCodeBean) {
            getPostCodeHandle().mergeWith(((PostalCodeBean) otherPostCode).getPostCodeHandle());
        }
    }

}
