package com.blueskyminds.homebyfive.business.region.index;

import javax.persistence.*;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import org.jboss.envers.Versioned;

/**
 * Denormalized postcode bean for fast lookup by path
 *
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("P")
public class PostalCodeBean extends RegionIndex {
    
    public PostalCodeBean() {
    }

    public PostalCodeBean(String name) {
        this.name = name;
        populateDenormalizedAttributes();
    }

    public PostalCodeBean(PostalCode postCodeHandle) {
        super(postCodeHandle);
        populateDenormalizedAttributes();
    }

    @Transient
    public String getPostCodeId() {
        return key;
    }

    protected void setPostCodeId(String postCodeId) {
        this.key = postCodeId;
    }

    @Transient
    public PostalCode getPostalCodeHandle() {
        return (PostalCode) region;
    }

    public void setPostalCodeHandle(PostalCode postCodeHandle) {
        this.region = postCodeHandle;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateDenormalizedAttributes() {
        this.key = KeyGenerator.generateId(name);

        // the state is the parent
        parent = getPostalCodeHandle().getState().getRegionIndex();

        if (parent != null) {
            this.parentPath = parent.getPath();

            this.path = PathHelper.joinPath(parentPath, key);
            this.status = DomainObjectStatus.Valid;
            this.type = RegionTypes.PostCode;

            this.countryId = parent.getCountryId();
            this.countryPath = parent.getCountryPath();
            this.countryName = parent.getCountryName();

            this.stateId = parent.getKey();
            this.statePath = parent.getPath();
            this.stateName = parent.getName();
        }
        this.postalCodeId = key;
        this.postalCodePath = path;
        this.postalCodeName = name;
    }

    public void mergeWith(RegionIndex otherPostCode) {
        if (otherPostCode instanceof PostalCodeBean) {
            getPostalCodeHandle().mergeWith(((PostalCodeBean) otherPostCode).getPostalCodeHandle());
        }
    }

    @Transient
    public StateBean getStateBean() {
        return (StateBean) getParent();
    }
}
