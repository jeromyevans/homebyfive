package com.blueskyminds.enterprise.region.postcode;

import com.blueskyminds.enterprise.region.DefaultRegionImpl;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.*;
import com.blueskyminds.enterprise.region.RegionMixin;

import javax.persistence.*;

/**
 * A postal code/zip code region
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
public class PostCode extends DefaultRegionImpl implements Region {

    protected PostCode(String postCode) {
        //this.regionXMixin = new RegionMixin(new PostCodeHandle(postCode, this), RegionTypes.PostCode);
        this.regionXMixin = new RegionMixin(RegionTypes.PostCode);
        this.regionXMixin.setName(postCode);
    }

    protected PostCode() {
        this.regionXMixin = new RegionMixin();
    }            
}
