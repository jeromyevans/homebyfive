package com.blueskyminds.enterprise.license;

import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.enterprise.party.Party;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Managers all of the licenses available within the Enterprise, including those that are unallocated or
 * unavailable.
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfLicenses extends Schedule<License> {

    /** The journal associated with this schedule of licenses.  The journal tracks transfers */
    private LicenseJournal licenseJournal;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of a ScheduleOfLicenses */
    public ScheduleOfLicenses(Enterprise enterprise, LicenseJournal licenseJournal, Journal journal) {
        super(enterprise, journal);
        this.licenseJournal = licenseJournal;
        init();
    }

    /** Default constructor for ORM */
    protected ScheduleOfLicenses() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the manager with default values
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the specified license, adding it the the account identified
     * @param license to create
     * @param initialAccount initial account for the license
     * @return the license if added successfully, otherwise null
     */
    public License createLicense(License license, LicenseAccount initialAccount) {
        boolean added = false;
        License licenseAdded;

        licenseAdded = super.create(license);

        if (licenseAdded != null) {
            if (initialAccount.addLicense(licenseAdded)) {
                added = true;
            }
            else {
                // an error occurred - attempt to roll-back
                super.delete(licenseAdded);
            }
        }

        if (added) {
            return licenseAdded;
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Removes the specified license from the schedule
     *
     * @param license to delete
     * @param fromAccount the account that currently contains the license (this is a precaution really)
     * @return true if added successfully
     */
    public boolean deleteLicense(License license, LicenseAccount fromAccount) {
        boolean deleted = false;

        if (super.delete(license)) {
            // remove from the account
            if (fromAccount.hasLicenseAllocated(license)) {
                if (fromAccount.removeLicense(license)) {
                    deleted = true;
                }
                else {
                    // an error occurred - should roll-back.  Make an audit trail entry
                    //ServiceLocator.auditTrail().audit(this, "A license was deleted ("+license.getIdentityName()+") but it couldn't be removed from the nominated account: "+fromAccount.getIdentityName());
                }
            }
            else {
                if (fromAccount.hasLicenseReserved(license)) {
                    if (fromAccount.removeReservedLicense(license)) {
                        deleted = true;
                    }
                    else {
                        // an error occurred - should roll-back.  Make an audit trail entry
                        //ServiceLocator.auditTrail().audit(this, "A license was deleted ("+license.getIdentityName()+") but it couldn't be unreserved from the nominated account: "+fromAccount.getIdentityName());
                    }
                }
                else {
                    // license is not attached to anything - it can be deleted
                    deleted = true;
                }
            }
        }

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempt to reserve the specified license for the requesting account
     *
     * A license can only be reserved if it's available.
     * A license is available if its currently in the special-case UnallocatedAccount
     *
     * Process:
     *   1. tests if the license is available according to the fromAccount, and if available
     *   2. creates an entry to release it from the fromAccount
     *   3. creates an entry to reserve the license for the toAccount
     *   4. creates a transaction for the two entries
     *   5. commits the transaction
     *
     * @throws IllegalTransactionException if the license is not available or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was reserved successfully, exception if it was not
     **/
    public License reserveLicense(License license, UnallocatedAccount fromAccount, LicenseAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is available
        if (fromAccount.isLicenseAvailable(license))
        {
            licenseJournal.createReserveTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to reserve a license that was not available");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempt to unreserve the license from the specified account, moving it to the UnallocatedAccoun
     *
     * A license can only be unreserved if it's currently reserved
     * A license can only be transferred to the toAccount
     *
     * @throws IllegalTransactionException if the license is not available or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was reserved successfully, exception if it was not
     **/
    public License unreserveLicense(License license, LicenseAccount fromAccount, UnallocatedAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is available
        if (fromAccount.hasLicenseReserved(license))
        {
            licenseJournal.createUnreserveTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to unreserve a license that was not reserved");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Tranfers the license from the specified UnallocatedAccount to the toAccount
     *
     * A license can only be allocated if it's currently unallocated
     *
     * @throws IllegalTransactionException if the license is not allocated or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was transferred successfully, exception if it was not
     **/
    public License allocateLicense(License license, UnallocatedAccount fromAccount, LicenseAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is in the fromAccount
        if (fromAccount.hasLicenseAllocated(license))
        {
            licenseJournal.createTransferTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to allocate a license that was not unallocated");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Tranfers the license from the specified fromAccount to the Unallocated toAccount
     * The fromAccount has no option - the transfer is performed if the license is currently allocated to
     * it.
     *
     * A license can only be unallocated if it's currently allocated
     * A license can only be transferred to an UnallocatedAccount
     *
     * @throws IllegalTransactionException if the license is not allocated or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was transferred successfully, exception if it was not
     **/
    public License unallocateLicense(License license, LicenseAccount fromAccount, UnallocatedAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is in the fromAccount
        if (fromAccount.hasLicenseAllocated(license))
        {
            licenseJournal.createTransferTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to unallocate a license that was not allocated");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Tranfers the license from the specified UnavilableAccount to the UnallocatedAccount
     *
     *
     * @throws IllegalTransactionException if the license is not in the UnavailableAccount or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was transferred successfully, exception if it was not
     **/
    public License makeLicenseAvailable(License license, UnavailableAccount fromAccount, UnallocatedAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is in the fromAccount
        if (fromAccount.hasLicenseAllocated(license))
        {
            licenseJournal.createTransferTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to make a license available that was not in an Unavailable account");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Tranfers the license from the specified UnallocatedAccount to the UnavailableAccount
     *
     * @throws IllegalTransactionException if the license is not in the UnallocatedAccount or if the attempt to
     *  create a transaction throws the exception
     *
     * @return the updated License if it was transferred successfully, exception if it was not
     **/
    public License makeLicenseUnavailable(License license, UnallocatedAccount fromAccount, UnavailableAccount toAccount, String note, Party party)
            throws IllegalTransactionException {

        // determine if the license is in the fromAccount
        if (fromAccount.hasLicenseAllocated(license))
        {
            licenseJournal.createTransferTransaction(new Date(), fromAccount,  toAccount, license, note, party);
        }
        else {
            throw new IllegalTransactionException("Attempted to make a license available that was not in an Unavailable account");
        }

        return license;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfLicensesEntry",
            joinColumns=@JoinColumn(name="ScheduleOfLicensesId"),
            inverseJoinColumns = @JoinColumn(name="LicenseId")
    )
    protected List<License> getLicenses() {
        return super.getDomainObjects();
    }

    protected void setLicenses(List<License> licenses) {
        super.setDomainObjects(licenses);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the license journal associated with this schedule of accounts */
    @ManyToOne()
    @JoinColumn(name="LicenseJournalId")
    public LicenseJournal getLicenseJournal() {
        return licenseJournal;
    }

    public void setLicenseJournal(LicenseJournal licenseJournal) {
        this.licenseJournal = licenseJournal;
    }

    // ------------------------------------------------------------------------------------------------------
}
