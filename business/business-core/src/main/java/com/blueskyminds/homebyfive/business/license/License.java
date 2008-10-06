package com.blueskyminds.homebyfive.business.license;

import com.blueskyminds.homebyfive.business.pricing.Product;

import javax.persistence.*;

/**
 *
 * Abstract implementation of a License
 *
 * A License is a Product
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class License extends Product {

    /**
     * The type of this license
     **/
    private LicenseTypes type;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new license of the specified type
     * The license is not attached any relationships
     * @param type
     */
    public License(LicenseTypes type) {
        this.type = type;
    }

    /** Default constructor for ORM */
    protected License() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the type enumeration for this license
     * @return the type enumeration of this license
     */
    @Enumerated
    @Column(name="Type")
    public LicenseTypes getType() {
        return type;
    }

    public void setType(LicenseTypes type) {
        this.type = type;
    }
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}

