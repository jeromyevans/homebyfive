package com.blueskyminds.enterprise.license;

import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;

import javax.persistence.*;

/**
 * Wraps a License with type information about how it's been allocated to an account.
 * eg. as a reservation, or a full allocation
 *
 * IMPORTANT: this is a value-object.  It overrides equals and hashcode for allow comparisons
 * by value of license and type.
 *
 * Date Started: 2/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class LicenseAllocation extends AbstractDomainObject {

    /** The license account this allocation is within */
    private LicenseAccount licenseAccount;

    /**
     * A reference to the license this allocation is for
     **/
    private License license;

    /**
     * The type of allocation
     */
    private LicenseAllocationTypes type;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new license allocation of the specified type.
     *
     * @param license - the license being allocated
     * @param type    - the type of allocation
     */
    public LicenseAllocation(LicenseAccount licenseAccount,  License license, LicenseAllocationTypes type) {
        this.licenseAccount = licenseAccount;
        this.license = license;
        this.type = type;
    }

    /** Default constructor for ORM */
    protected LicenseAllocation() {

    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="LicenseAccountId")
    public LicenseAccount getLicenseAccount() {
        return licenseAccount;
    }

    protected void setLicenseAccount(LicenseAccount licenseAccount) {
        this.licenseAccount = licenseAccount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the license referenced in this allocation
     * @return the license
     */
    @OneToOne
    @JoinColumn(name="LicenseId")
    public License getLicense() {
        return license;
    }

    protected void setLicense(License license) {
        this.license = license;
    }

    // ------------------------------------------------------------------------------------------------------

    /** get the type of this allocation */
    @Enumerated
    @Column(name="Type")
    public LicenseAllocationTypes getType() {
        return type;
    }

    public void setType(LicenseAllocationTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This class is a value object - equals is overridden to compare the license and type
     * @param o
     * @return true if referenced license and type are equal
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LicenseAllocation that = (LicenseAllocation) o;

        if (!license.equals(that.license)) return false;
        if (type != that.type) return false;

        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * This class is a value object - hashcode is overridden to use the license and type
     * @return hashcode
     */
    public int hashCode() {
        int result;
        result = license.hashCode();
        result = 29 * result + type.hashCode();
        return result;
    }
}
