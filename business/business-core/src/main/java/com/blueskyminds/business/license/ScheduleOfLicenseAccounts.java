package com.blueskyminds.business.license;

import com.blueskyminds.business.Enterprise;
import com.blueskyminds.business.Schedule;
import com.blueskyminds.business.party.Party;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 * Schedule of the LicenseAccounts defined in an Enterprise
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfLicenseAccounts extends Schedule<LicenseAccount> {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of a ScheduleOfLicenses */
    public ScheduleOfLicenseAccounts(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
        init();
    }

    /** Default constructor for ORM */
    protected ScheduleOfLicenseAccounts() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise this schedule with deafult attributes */
    private void init() {
        // create the two special-case account
        createLicenseAccount(new UnallocatedAccount(this, getEnterprise().getSystemParty()));
        createLicenseAccount(new UnavailableAccount(this, getEnterprise().getSystemParty()));
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a LicenseAccount for the specified party
     *
     * @param party to create the license account for
     * @return the new LicenseAccount, or null if it failed
     */
    public LicenseAccount createLicenseAccount(Party party) {
        return createLicenseAccount(new LicenseAccount(this, party));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the LicenseAccount specified by including it in this schedule
     *
     * @param licenseAccount to create by adding to ths schedule
     * @return the new LicenseAccount, or null if it failed
     */
    public LicenseAccount createLicenseAccount(LicenseAccount licenseAccount) {
        return super.create(licenseAccount);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified licenseAccount.
     *
     * @param licenseAccount
     * @return true if deleted successfully
     */
    public boolean deleteLicenseAccount(LicenseAccount licenseAccount) {

        boolean deleted;

        deleted = super.delete(licenseAccount);

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfLicenseAccountsEntry",
            joinColumns=@JoinColumn(name="ScheduleOfLicenseAccountsId"),
            inverseJoinColumns = @JoinColumn(name="LicenseAccountId")
    )
    protected List<LicenseAccount> getLicenseAccounts() {
        return super.getDomainObjects();
    }

    protected void setLicenseAccounts(List<LicenseAccount> licenseAccounts) {
        super.setDomainObjects(licenseAccounts);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

// ------------------------------------------------------------------------------------------------------

    /** Get the special-case unallocated account for this schedule of accounts.
     *
     * This is a slow implementation - it iteratively searches the schedule for the appropriate account */
    @Transient
    public UnallocatedAccount getUnallocatedAccount() {
        for (LicenseAccount account : super.getDomainObjects()) {
            if (account instanceof UnallocatedAccount) {
                return (UnallocatedAccount) account;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the special-case unavailable account for this schedule
     *
     * This is a slow implementation - it iteratively searches the schedule for the appropriate account */
    @Transient
    public UnavailableAccount getUnavailableAccount() {
        for (LicenseAccount account : super.getDomainObjects()) {
            if (account instanceof UnavailableAccount) {
                return (UnavailableAccount) account;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Schedule of License Accounts ---");
        for (LicenseAccount licenseAccount : getDomainObjects()) {
            licenseAccount.print(out);
        }
    }
}
