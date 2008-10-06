package com.blueskyminds.business.license;

import com.blueskyminds.business.license.LicenseTypes;
import com.blueskyminds.business.license.License;
import com.blueskyminds.business.region.graph.Region;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * A type of License given for a Region
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class RegionLicense extends License {

    /**
     * The region that this license represents allocated by this allocation
     **/
    private Region region;

    // -------------------------------------------------------------------------------------------------------
    /**
    * Create a new license of the specified type.  The license is with respect to a region
    *
    * @param region
    * @param type
    */
    public RegionLicense(Region region, LicenseTypes type) {
        super(type);
        this.region = region;
    }

    /** Default constructor for ORM */
    protected RegionLicense() {
    }

    // -------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="RegionId")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    // -------------------------------------------------------------------------------------------------------

    /**
     * Determines whether this license is available.
     *  A license is only available only if its currently allocated to a special-case UnallocatedAccount
     * @return true if this license is available
     */
//    public boolean isAvailable() {
//        return ((getAccount() instanceof UnallocatedAccount));
//    }

    // -------------------------------------------------------------------------------------------------------
}
