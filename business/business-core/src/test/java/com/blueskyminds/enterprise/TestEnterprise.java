package com.blueskyminds.enterprise;

import com.blueskyminds.enterprise.accounting.ChartOfAccounts;
import com.blueskyminds.enterprise.accounting.DetailAccount;
import com.blueskyminds.enterprise.license.*;
import com.blueskyminds.enterprise.party.Company;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.pricing.*;
import com.blueskyminds.enterprise.pricing.policy.FixedPrice;
import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.enterprise.pricing.policy.RecurringPrice;
import com.blueskyminds.enterprise.pricing.policy.TransactionFee;
import com.blueskyminds.enterprise.taxpolicy.GST;
import com.blueskyminds.enterprise.taxpolicy.GSTFree;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;
import com.blueskyminds.homebyfive.framework.core.measurement.QuantityUnits;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceSession;
import com.blueskyminds.homebyfive.framework.core.test.DbTestCase;

import java.util.*;

/**
 * Methods to test the Enterprise class
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestEnterprise extends DbTestCase {

    public TestEnterprise(String string) {
        super(string);
    }

    public void testEnterprise() {
        WDISEnterprise enterprise = new WDISEnterprise();

        Party testParty = enterprise.getScheduleOfParties().createParty(new Company("TestCompany"));
        Product adminFee = enterprise.getProductList().createProduct(new Fee("Admin", FeeTypes.Fixed));
        Product transactionFee = enterprise.getProductList().createProduct(new Fee("Transaction", FeeTypes.Transaction));

        // setup a test license account
        ScheduleOfLicenseAccounts sola = enterprise.getScheduleOfLicenseAccounts();
        LicenseAccount accountAHolder = sola.createLicenseAccount(testParty);

        //RegionType postCode = new RegionType(RegionTypes.PostCode);
        Region region = new RegionFactory().createPostCode("TestRegion");

        // create a new license in the unallocated account
        License testLicenseA = enterprise.getScheduleOfLicenses().createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseB = enterprise.getScheduleOfLicenses().createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());

        enterprise.mapProductToRevenueAccount(testLicenseA, enterprise.getLicenseFees());
        enterprise.mapProductToRevenueAccount(testLicenseB, enterprise.getLicenseFees());
        enterprise.mapProductToRevenueAccount(adminFee, enterprise.getAdminFees());
        enterprise.mapProductToRevenueAccount(transactionFee, enterprise.getTransactionFees());
        enterprise.mapTaxPolicyToLiabilityAccount(enterprise.GST(), enterprise.getTaxLiabilityAccount());

        Contract contractA = enterprise.getScheduleOfContracts().createContract(new Contract("ContractA", enterprise.getJournal()));

        PricingPolicy policyA = new RecurringPrice(new Price(new Money(200.00, Currency.getInstance("AUD")), enterprise.GST(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyB = new RecurringPrice(new Price(new Money(250.00, Currency.getInstance("AUD")), enterprise.GST(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyC = new RecurringPrice(new Price(new Money(230.00, Currency.getInstance("AUD")), enterprise.GST(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyD = new FixedPrice(new Price(new Money(10.00, Currency.getInstance("AUD")), enterprise.GST(), new Quantity(1, QuantityUnits.Each)));
        PricingPolicy policyE = new TransactionFee(new Price(new Money(2.00, Currency.getInstance("AUD")), enterprise.GST(), new Quantity(1, QuantityUnits.Each)));

        contractA.mapPricingPolicy(testLicenseA, policyA);
        contractA.mapPricingPolicy(testLicenseB, policyB);
        contractA.mapPricingPolicy(adminFee, policyD);
        contractA.mapPricingPolicy(transactionFee, policyE);

        // --- two contracts, same price for License A, different price for LicenseB  ---
//todo:
//        // --- setup an account on ContractA and an account on ContractB
//        testParty.changeCurrentContract(contractA, enterprise.getJournal());
//
//        assertTrue(contractA.lookupPricingPolicy(testLicenseA).equals(policyA));
//
//        // --- create an order for accountAHolder
//        Order orderA = new Order(testParty, testParty.getCurrentContract(), new Date(), new Interval(12, PeriodTypes.Month), new PrepaidRecurring(PeriodTypes.Month), enterprise.getJournal());
//
//        try {
//            // --- reserve a license
//            enterprise.getScheduleOfLicenses().reserveLicense(testLicenseA, sola.getUnallocatedAccount(), accountAHolder, "reserve", testParty);
//        }
//        catch (IllegalTransactionException e) {
//            fail();
//        }
//
//        // add the reserved license to the order
//        assertTrue(orderA.addProduct(testLicenseA, new Quantity(1, QuantityUnits.Each)));
//        assertTrue(orderA.addProduct(adminFee, new Quantity(1, QuantityUnits.Each)));
//        assertTrue(orderA.addProduct(transactionFee, new Quantity(1, QuantityUnits.Each)));
//
//        orderA.print(System.out);
//
//        Calendar cal = GregorianCalendar.getInstance();
//        cal.add(Calendar.MONTH, 1);
//        Date nextMonth = cal.getTime();
//
//        InvoiceFactory invoiceFactory = new InvoiceFactory();
//
//        Invoice invoiceA = invoiceFactory.createInitialInvoice(orderA, enterprise.getJournal());
//        invoiceA.print(System.out);
//        Invoice invoiceB = invoiceFactory.createOngoingInvoice(orderA, 1, nextMonth, enterprise.getJournal());
//        invoiceB.print(System.out);
//        List<Invoice> invoices = invoiceFactory.createInvoices(orderA, enterprise.getJournal());
//
//        try {
//            invoiceA.journalise(enterprise, "");
//
//            invoiceB.journalise(enterprise, "");
//
//            for (Invoice invoice : invoices) {
//                invoice.print(System.out);
//                invoice.journalise(enterprise, "");
//            }
//
//        } catch (FinancialTransactionException e) {
//            e.printStackTrace();
//            fail();
//        }
//
//        enterprise.getChartOfAccounts().print(System.out);
//
//        enterprise.getChartOfAccounts().getAssets().printLedger(System.out);
//        enterprise.getChartOfAccounts().getLiabilities().printLedger(System.out);
//        enterprise.getChartOfAccounts().getCapital().printLedger(System.out);
//        enterprise.getChartOfAccounts().getDrawings().printLedger(System.out);
//        enterprise.getChartOfAccounts().getRevenue().printLedger(System.out);
//        enterprise.getChartOfAccounts().getExpenses().printLedger(System.out);
//
//        enterprise.getFinancialJournal().print(System.out);
//
//        // make a payment against an invoice
//        Receipt receipt = new Receipt(new Date(), testParty);
//        // apply to the entire value of the first invoice in the collection
//        receipt.applyAmount(invoices.get(0));
//        receipt.print(System.out);
//
//        try {
//            receipt.journalise(enterprise, "Payment of First Invoice");
//        }
//        catch (FinancialTransactionException e) {
//            e.printStackTrace();
//            fail();
//        }
//
//        enterprise.getChartOfAccounts().print(System.out);
//        enterprise.getFinancialJournal().print(System.out);
//
//        enterprise.getChartOfAccounts().getAssets().printLedger(System.out);
//
    }

    public void testProductAccountMapPersistence() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));

            Party testParty = enterprise.getScheduleOfParties().createParty(new Company("TestCompany"));
            Product adminFee = enterprise.getProductList().createProduct(new Fee("Admin", FeeTypes.Fixed));
            Product transactionFee = enterprise.getProductList().createProduct(new Fee("Transaction", FeeTypes.Transaction));

            ChartOfAccounts chartOfAccounts = enterprise.getChartOfAccounts();
            DetailAccount licenseFees = chartOfAccounts.createRevenueAccount("License Fees");
            DetailAccount adminFees = chartOfAccounts.createRevenueAccount("Admin Fees");
            DetailAccount transactionFees = chartOfAccounts.createRevenueAccount("Transaction Fees");
            DetailAccount gstCollected = chartOfAccounts.createLiabilityAccount("GST Collected");

            // setup a test license account
            LicenseJournal licenseJournal = new LicenseJournal(enterprise, enterprise.getJournal());
            ScheduleOfLicenses sol = new ScheduleOfLicenses(enterprise, licenseJournal, enterprise.getJournal());
            ScheduleOfLicenseAccounts sola = new ScheduleOfLicenseAccounts(enterprise, enterprise.getJournal());

            LicenseAccount accountAHolder = sola.createLicenseAccount(testParty);
            
            // create a new license in the unallocated account
            License testLicenseA = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
            License testLicenseB = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());

            enterprise.getProductList().createProduct(testLicenseA);
            enterprise.getProductList().createProduct(testLicenseB);

            enterprise.getScheduleOfTaxes().defineTaxPolicy(GST.newInstance());
            enterprise.getScheduleOfTaxes().defineTaxPolicy(GSTFree.getInstance());

            enterprise.mapProductToRevenueAccount(testLicenseA, licenseFees);
            enterprise.mapProductToRevenueAccount(testLicenseB, licenseFees);
            enterprise.mapProductToRevenueAccount(adminFee, adminFees);
            enterprise.mapProductToRevenueAccount(transactionFee, transactionFees);
            enterprise.mapTaxPolicyToLiabilityAccount(enterprise.getScheduleOfTaxes().getByName(GST.TAX_NAME), gstCollected);

            enterprise.getProductAccountMap().print(System.out);

            gateway.save(enterprise);

            session = gateway.openSession();
            Collection<ProductAccountMap> maps = gateway.findAll(ProductAccountMap.class);
            for (ProductAccountMap productAccountMap : maps) {
                productAccountMap.print(System.out);
            }
            gateway.closeSession();

            session = gateway.openSession();
            Collection<TaxAccountMap> taxMaps = gateway.findAll(TaxAccountMap.class);
            for (TaxAccountMap taxAccountMap : taxMaps) {
                taxAccountMap.print(System.out);
            }
            gateway.closeSession();

        }
        catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
