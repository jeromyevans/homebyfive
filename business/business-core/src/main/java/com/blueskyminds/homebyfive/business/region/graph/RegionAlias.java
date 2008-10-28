package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

import org.jboss.envers.Versioned;

/**
 * An alias for a region (an alternative name)
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@Versioned
public class RegionAlias extends AbstractEntity implements Named {

    private Region region;
    private String name;

    public RegionAlias(Region region, String name) {
        this.region = region;
        this.name = name;
    }

    /** Default constructor for ORM */
    public RegionAlias() {
    }

    @ManyToOne
    @JoinColumn(name="RegionId")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
