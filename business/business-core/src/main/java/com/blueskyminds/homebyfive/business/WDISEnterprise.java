package com.blueskyminds.homebyfive.business;

import com.blueskyminds.homebyfive.business.Enterprise;
import com.blueskyminds.homebyfive.business.taxpolicy.GST;
import com.blueskyminds.homebyfive.business.taxpolicy.TaxPolicy;
import com.blueskyminds.homebyfive.business.taxpolicy.GSTFree;
import com.blueskyminds.homebyfive.business.license.ScheduleOfLicenses;
import com.blueskyminds.homebyfive.business.license.LicenseJournal;
import com.blueskyminds.homebyfive.business.license.ScheduleOfLicenseAccounts;
import com.blueskyminds.homebyfive.business.accounting.DetailAccount;

import java.util.Currency;

/**
 * An implementation of an Enterprise for the WDIS domain.
 *
 * The Enterprise is extended to include:
 *   ScheduleOfLicenses
 *   ScheduleOfAccounts
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class WDISEnterprise extends Enterprise {

    /** The schedule of licenses defined in this Enterprise */
    private ScheduleOfLicenses scheduleOfLicenses;

    /** The schedule of license-holding accounts in this enterprise */
    private ScheduleOfLicenseAccounts scheduleOfLicenseAccounts;

    /** The journal for tracking license transfers */
    private LicenseJournal licenseJournal;

    /** The Account for fees received for licenses */
    private DetailAccount licenseFees;

    /** The Account for fees received for admin */
    private DetailAccount adminFees;

    /** The Account for fees received for transactions */
    private DetailAccount transactionFees;

    // ------------------------------------------------------------------------------------------------------

    public WDISEnterprise() {
        super("WDIS", Currency.getInstance("AUD"));
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    private void init() {
        licenseJournal = new LicenseJournal(this, getJournal());
        scheduleOfLicenseAccounts = new ScheduleOfLicenseAccounts(this, getJournal());
        scheduleOfLicenses = new ScheduleOfLicenses(this, licenseJournal, getJournal());

        // initialise the standard relationships for this enterprise
        licenseFees = getChartOfAccounts().createRevenueAccount("License Fees");
        adminFees = getChartOfAccounts().createRevenueAccount("Admin Fees");
        transactionFees = getChartOfAccounts().createRevenueAccount("Transaction Fees");

        setAccountsReceivable(getChartOfAccounts().createAssetAccount("Accounts Receivable"));
        setChequeAccount(getChartOfAccounts().createAssetAccount("Business Cheque Account"));
        setTaxLiabilityAccount(getChartOfAccounts().createLiabilityAccount("GST Collected"));

        // setup tax policies
        getScheduleOfTaxes().defineTaxPolicy(GST.newInstance());
        getScheduleOfTaxes().defineTaxPolicy(GSTFree.getInstance());

        // map taxes to liability relationships
        getTaxAccountMap().mapLiabilityAccount(GST(), getTaxLiabilityAccount());
    }

    // ------------------------------------------------------------------------------------------------------

    public ScheduleOfLicenses getScheduleOfLicenses() {
        return scheduleOfLicenses;
    }

    public ScheduleOfLicenseAccounts getScheduleOfLicenseAccounts() {
        return scheduleOfLicenseAccounts;
    }

    public DetailAccount getLicenseFees() {
        return licenseFees;
    }

    public DetailAccount getAdminFees() {
        return adminFees;
    }

    public DetailAccount getTransactionFees() {
        return transactionFees;
    }

    public TaxPolicy GST() {
        return getScheduleOfTaxes().getByName(GST.TAX_NAME);
    }
}
