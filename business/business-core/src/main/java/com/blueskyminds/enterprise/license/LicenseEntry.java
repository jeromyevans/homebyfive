package com.blueskyminds.enterprise.license;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.util.Date;

/**
 * LicenseEntry is part of a transaction associate a License with an AccountHolder
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class LicenseEntry extends AbstractDomainObject {

    /** The transaction that this entry is for */
    private LicenseTransaction licenseTransaction;

    /**
     * The account that this entry is for
     **/
    private LicenseAccount account;

    /**
     * The license that is being entered
     */
    private License license;

    /**
     * The date of this entry, set during the commit
     */
    private Date date;

    /**
     * Whether this is entry is adding the license to the account or removing it
     */
    private LicenseEntryTypes type;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new entry for a LicenseTransaction
     *
     * @param account The account that this entry applies to
     * @param license The license being affected
     * @param type The type of entry
     */
    public LicenseEntry(LicenseTransaction licenseTransaction, LicenseAccount account, License license, LicenseEntryTypes type) {
        this.licenseTransaction = licenseTransaction;
        this.account = account;
        this.license = license;
        this.type = type;
    }

    /** Default constructor for ORM */
    protected LicenseEntry() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this entry is contra (eg. opposite or the reverse) of the specified entry
     * The test ensures they have conta types and same license
     * @param entry
     * @return true if this entry is the contra of the specified entry
     */
//    @Transient
//    public boolean isContra(LicenseEntry entry) {
//        boolean contra = false;
//
//        if (entry != null) {
//            if (license.equals(entry.getLicense())) {
//                if (type.isContra(entry.getType()))  {
//                    contra = true;
//                }
//            }
//        }
//
//        return contra;
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Provides the type of this LicenseEntry
     * @return the type of this license entry
     */
    @Embedded
    @Column(name="Type")
    public LicenseEntryTypes getType() {
        return type;
    }

    protected void setType(LicenseEntryTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the date/time stamp of this entry
     * @return the date/time stamp of this entry
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Date")
    public Date getDate() {
        return date;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the license affected by this entry
     * @return the license affected by this entry
     */
    @ManyToOne
    @JoinColumn(name="LicenseId")
    public License getLicense() {
        return license;
    }

    protected void setLicense(License license) {
        this.license = license;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the account affected by this entry
     * @return the account affected by this entry
     */
    @ManyToOne
    @JoinColumn(name="LicenseAccountId")
    public LicenseAccount getAccount() {
        return account;
    }

    protected void setAccount(LicenseAccount account) {
        this.account = account;
    }

    @ManyToOne
    @JoinColumn(name="LicenseTransactionId")
    public LicenseTransaction getLicenseTransaction() {
        return licenseTransaction;
    }

    public void setLicenseTransaction(LicenseTransaction licenseTransaction) {
        this.licenseTransaction = licenseTransaction;
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to apply this enty, updating the account and
     *
     * @return true if completed ok
     */
    public boolean doCommit() {
        boolean committed = false;
        date = new Date();            // set the timestamp of this entry

        switch (type) {
            case Include:
                committed = account.addLicense(license);
                break;
            case Release:
                committed = account.removeLicense(license);
                break;
            case Reserve:
                committed = account.addReservedLicense(license);
                break;
            case Unreserve:
                committed = account.removeReservedLicense(license);
                break;
        }
        return committed;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to reverse the change applied by this enty, updating the account.
     * Use this method only when rolling back a license transaction
     *
     * @return true if completed ok
     */
    public boolean doReverse() {
        boolean committed = false;

        switch (type) {
            case Include:
                committed = account.removeLicense(license);
                break;
            case Release:
                committed = account.addLicense(license);
                break;
            case Reserve:
                committed = account.removeReservedLicense(license);
                break;
            case Unreserve:
                committed = account.addReservedLicense(license);
                break;
        }
        return committed;
    }

    // ------------------------------------------------------------------------------------------------------
}
