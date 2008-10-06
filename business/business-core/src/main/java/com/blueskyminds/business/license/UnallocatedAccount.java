package com.blueskyminds.business.license;

import com.blueskyminds.business.party.Party;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A special-case LicenseAccount for Licenses that are not currently allocated to any relationships
 *
 * A license is always available if its ALLOCATED to the the UnallocatedAccount
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Unallocated")
public class UnallocatedAccount extends LicenseAccount {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new special-case UnallocatedAccount */
    public UnallocatedAccount(ScheduleOfLicenseAccounts scheduleOfLicenseAccounts, Party party) {
        super(scheduleOfLicenseAccounts, party);
    }


    /** Default constructor for ORM */
    protected UnallocatedAccount() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * A license in this special-case LicenseAccount is always available
     * @param license
     * @return true if the license is in this account
     */
    public boolean isLicenseAvailable(License license) {
        return hasLicenseAllocated(license);
    }
}
