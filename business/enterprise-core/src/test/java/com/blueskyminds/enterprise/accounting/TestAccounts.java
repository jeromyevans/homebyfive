package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.enterprise.party.Company;
import com.blueskyminds.framework.test.DbTestCase;

import java.util.Currency;
import java.util.Date;
import java.util.List;

/**
 * Date Started: 15/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestAccounts extends DbTestCase {

    public TestAccounts(String string) {
        super(string);
    }

    public void testAccounts() {
        Enterprise enterprise = new Enterprise("WDISTest", Currency.getInstance("AUD"));

        Company company = new Company("testCompany");
        FinancialJournal financialJournal = enterprise.getFinancialJournal();
        ChartOfAccounts chartOfAccounts = enterprise.getChartOfAccounts();

        Account accountsReceivable = chartOfAccounts.createAssetAccount("Accounts Receivable");

        Account licenseRevenue = chartOfAccounts.createRevenueAccount("License Fees");
        Account transactionRevenue = chartOfAccounts.createRevenueAccount("Transaction Fees");

        assertTrue(accountsReceivable.getBalance().isZero());
        assertTrue(licenseRevenue.getBalance().isZero());
        assertTrue(transactionRevenue.getBalance().isZero());

        // make an entry for an invoice
        Date invoiceDate = new Date();

        try {
            FinancialTransaction invoiceTransaction = financialJournal.createSimpleTransaction(invoiceDate, accountsReceivable, licenseRevenue, new Money(200.00, Currency.getInstance("AUD")), "", company);

            assertTrue(accountsReceivable.getBalance().equals(new Money(200.00, Currency.getInstance("AUD"))));
            assertTrue(chartOfAccounts.getRevenue().getBalance().equals(new Money(200.00, Currency.getInstance("AUD"))));
            assertTrue(licenseRevenue.getBalance().equals(new Money(200.00, Currency.getInstance("AUD"))));
            assertTrue(transactionRevenue.getBalance().isZero());
        }
        catch (FinancialTransactionException e) {
            fail();
        }

    }

    public void testChartOfAccountsPersistence() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            Enterprise enterprise = new Enterprise("WDISTest", Currency.getInstance("AUD"));

            Company company = new Company("testCompany");
            FinancialJournal financialJournal = enterprise.getFinancialJournal();
            ChartOfAccounts chartOfAccounts = enterprise.getChartOfAccounts();

            Account accountsReceivable = chartOfAccounts.createAssetAccount("Accounts Receivable");
            Account licenseRevenue = chartOfAccounts.createRevenueAccount("License Fees");
            Account transactionRevenue = chartOfAccounts.createRevenueAccount("Transaction Fees");

            gateway.save(enterprise);
            //gateway.save(chartOfAccounts);

            List<ChartOfAccounts> cofaList = gateway.findAll(ChartOfAccounts.class);
            for (ChartOfAccounts cofa : cofaList) {
                cofa.print(System.out);
            }
        }
        catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }



}
