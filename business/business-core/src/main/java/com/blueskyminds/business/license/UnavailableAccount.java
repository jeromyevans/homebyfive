package com.blueskyminds.business.license;

import com.blueskyminds.business.party.Party;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A special-case LicenseAccount for Licenses that exist but are currently unavailable
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Unavailable")
public class UnavailableAccount extends LicenseAccount {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new special-case UnavailableAccount */
    public UnavailableAccount(ScheduleOfLicenseAccounts scheduleOfLicenseAccounts, Party party) {
        super(scheduleOfLicenseAccounts, party);
    }

    /** Default constructor for ORM */
    protected UnavailableAccount() {

    }

    // ------------------------------------------------------------------------------------------------------
    /**
     * A license is never available from an UnavailableAccount
     *
     * @param license
     * @return false
     */
    public boolean isLicenseAvailable(License license) {
        return false;
    }
}
