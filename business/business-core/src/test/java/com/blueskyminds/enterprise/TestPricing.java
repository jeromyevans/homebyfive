package com.blueskyminds.enterprise;

import com.blueskyminds.enterprise.accounting.service.AccountingService;
import com.blueskyminds.enterprise.accounting.service.AccountingServiceImpl;
import com.blueskyminds.enterprise.license.*;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.party.PartyTypes;
import com.blueskyminds.enterprise.party.ScheduleOfParties;
import com.blueskyminds.enterprise.pricing.*;
import com.blueskyminds.enterprise.pricing.policy.FixedPrice;
import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.enterprise.pricing.policy.RecurringPrice;
import com.blueskyminds.enterprise.pricing.policy.TransactionFee;
import com.blueskyminds.enterprise.pricing.terms.PrepaidInFull;
import com.blueskyminds.enterprise.pricing.terms.PrepaidRecurring;
import com.blueskyminds.enterprise.pricing.terms.Terms;
import com.blueskyminds.enterprise.taxpolicy.GST;
import com.blueskyminds.enterprise.taxpolicy.ScheduleOfTaxes;
import com.blueskyminds.enterprise.taxpolicy.TaxPolicy;
import com.blueskyminds.enterprise.region.graph.RegionHandle;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;
import com.blueskyminds.homebyfive.framework.core.measurement.QuantityUnits;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;

import java.util.*;

/**
 * Test Cases for the Contract and Pricing framework
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestPricing extends JPATestCase {

    private static final String PRICING_PERSISIENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestPricing() {
        super(PRICING_PERSISIENCE_UNIT);
    }

    public void testRecurringPrice() {
        Journal journal = new Journal();

        RecurringPrice price = new RecurringPrice(new Price(new Money(12.37, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
    }   

    public void testMoney() {
        Money amountA = new Money(3.23, Currency.getInstance("AUD"));
        Money amountB = new Money(3.17, Currency.getInstance("AUD"));
        System.out.println("A= "+amountA);
        System.out.println("B= "+amountB);

        System.out.println("A+B="+amountA.add(amountB));
        Money amountC = amountA.multiply(0.5);
        System.out.println("C=A*0.5= "+amountC);
        System.out.println("C*2 = "+amountC.multiply(2));
    }

    public void testContract() {

        Journal journal = new Journal();

        ScheduleOfParties sop = new ScheduleOfParties(null, journal);
        Party testParty = sop.createParty(new Party(PartyTypes.Company));

        ProductList sof = new ProductList(null, journal);
        Product adminFee = sof.createProduct(new Fee("Admin", FeeTypes.Fixed));
        Product transactionFee = sof.createProduct(new Fee("Transaction", FeeTypes.Transaction));

        Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));
        LicenseJournal licenseJournal = new LicenseJournal(enterprise, journal);
        ScheduleOfLicenses sol = new ScheduleOfLicenses(null, licenseJournal,  journal);
        ScheduleOfLicenseAccounts sola = new ScheduleOfLicenseAccounts(enterprise, journal);
        //ScheduleOfAccounts schedule = new ScheduleOfAccounts(null, journal);

        // setup a test account
        LicenseAccount accountAHolder = sola.createLicenseAccount(testParty);
        LicenseAccount accountBHolder = sola.createLicenseAccount(testParty);

        //RegionType postCode = new RegionType(RegionTypes.PostCode);
        RegionHandle region = new RegionFactory().createPostCode("TestRegion");

        // create a new license in the unallocated account
        License testLicenseA = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseB = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());

        Contract contractA = new Contract("ContractA", journal);
        PricingPolicy policyA = new RecurringPrice(new Price(new Money(200.00, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyB = new RecurringPrice(new Price(new Money(250.00, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyC = new RecurringPrice(new Price(new Money(230.00, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyD = new FixedPrice(new Price(new Money(10.00, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)));
        PricingPolicy policyE = new TransactionFee(new Price(new Money(2.00, Currency.getInstance("AUD")), GST.newInstance(), new Quantity(1, QuantityUnits.Each)));

        contractA.mapPricingPolicy(testLicenseA, policyA);
        contractA.mapPricingPolicy(testLicenseB, policyB);
        contractA.mapPricingPolicy(adminFee, policyD);
        contractA.mapPricingPolicy(transactionFee, policyE);

        Contract contractB = new Contract("ContractB", journal);
        contractB.mapPricingPolicy(testLicenseA, policyA);
        contractB.mapPricingPolicy(testLicenseB, policyC);

        // --- two contracts, same price for License A, different price for LicenseB  ---

        // --- setup an account on ContractA and an account on ContractB
//todo:        testParty.changeCurrentContract(contractA, journal);
        //te.setCurrentContract(contractB, journal);

        assertTrue(contractA.lookupPricingPolicy(testLicenseA).equals(policyA));

        // --- create an order for accountAHolder
//todo:        Order orderA = new Order(testParty, testParty.getCurrentContract(), new Date(), new Interval(12, PeriodTypes.Month), new PrepaidRecurring(PeriodTypes.Month), journal);

        try {
            // --- reserve a license
            sol.reserveLicense(testLicenseA, sola.getUnallocatedAccount(), accountAHolder, "Reserve", testParty);
        }
        catch (IllegalTransactionException e) {
            fail();
        }

        // add the reserved license to the order
//todo:        assertTrue(orderA.addProduct(testLicenseA, new Quantity(1, QuantityUnits.Each)));
//todo:        assertTrue(orderA.addProduct(adminFee, new Quantity(1, QuantityUnits.Each)));
//todo:        assertTrue(orderA.addProduct(transactionFee, new Quantity(1, QuantityUnits.Each)));

//todo:        orderA.print(System.out);

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        Date nextMonth = cal.getTime();

        InvoiceFactory invoiceFactory = new InvoiceFactory();
//todo:
//        Invoice invoiceA = invoiceFactory.createInitialInvoice(orderA, journal);
//        invoiceA.print(System.out);
//        Invoice invoiceB = invoiceFactory.createOngoingInvoice(orderA, 1, nextMonth, journal);
//        invoiceB.print(System.out);
//        List<Invoice> invoices = invoiceFactory.createInvoices(orderA, journal);
//
//        for (Invoice invoice : invoices) {
//            invoice.print(System.out);
//        }
    }

    public void testPricePersistence() {
        em.persist(GST.newInstance());
        AccountingService accountingService = new AccountingServiceImpl(em);

        TaxPolicy gst = accountingService.lookupTaxPolicy(GST.TAX_NAME);
        Currency aud = Currency.getInstance("AUD");

        PricingPolicy policyA = new RecurringPrice(new Price(new Money(200.00, aud), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyB = new RecurringPrice(new Price(new Money(250.00, aud), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyC = new RecurringPrice(new Price(new Money(230.00, aud), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyD = new FixedPrice(new Price(new Money(10.00, aud), gst, new Quantity(1, QuantityUnits.Each)));
        PricingPolicy policyE = new TransactionFee(new Price(new Money(2.00, aud), gst, new Quantity(1, QuantityUnits.Each)));

        em.persist(policyA);
        em.persist(policyB);
        em.persist(policyC);
        em.persist(policyD);
        em.persist(policyE);

        em.flush();

        TestTools.printAll(em, PricingPolicy.class);
    }

    public void testTermsPersistence() {

        Terms termsA = new PrepaidRecurring(PeriodTypes.Month);
        Terms termsB = new PrepaidInFull();

        em.persist(termsA);
        em.persist(termsB);

        em.flush();

        TestTools.printAll(em, Terms.class);
    }


    public void testOrderPersistence() {



        Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));

        ScheduleOfParties sop = enterprise.getScheduleOfParties();
        Party testParty = sop.createParty(new Party(PartyTypes.Company));

        ProductList productList = enterprise.getProductList();
        Product adminFee = productList.createProduct(new Fee("Admin", FeeTypes.Fixed));
        Product transactionFee = productList.createProduct(new Fee("Transaction", FeeTypes.Transaction));

        LicenseJournal licenseJournal = new LicenseJournal(enterprise, enterprise.getJournal());
        ScheduleOfLicenses sol = new ScheduleOfLicenses(null, licenseJournal,  enterprise.getJournal());
        ScheduleOfLicenseAccounts sola = new ScheduleOfLicenseAccounts(enterprise, enterprise.getJournal());

        ScheduleOfTaxes sot = enterprise.getScheduleOfTaxes();
        TaxPolicy gst = GST.newInstance();
        sot.defineTaxPolicy(gst);

        // setup a test account
        LicenseAccount accountAHolder = sola.createLicenseAccount(testParty);
        LicenseAccount accountBHolder = sola.createLicenseAccount(testParty);

        // create a new license in the unallocated account
        License testLicenseA = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseB = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        // register the two licenses as products
        productList.createProduct(testLicenseA);
        productList.createProduct(testLicenseB);

        Contract contractA = enterprise.getScheduleOfContracts().createContract(new Contract("ContractA", enterprise.getJournal()));
        PricingPolicy policyA = new RecurringPrice(new Price(new Money(200.00, Currency.getInstance("AUD")), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyB = new RecurringPrice(new Price(new Money(250.00, Currency.getInstance("AUD")), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyC = new RecurringPrice(new Price(new Money(230.00, Currency.getInstance("AUD")), gst, new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
        PricingPolicy policyD = new FixedPrice(new Price(new Money(10.00, Currency.getInstance("AUD")), gst, new Quantity(1, QuantityUnits.Each)));
        PricingPolicy policyE = new TransactionFee(new Price(new Money(2.00, Currency.getInstance("AUD")), gst, new Quantity(1, QuantityUnits.Each)));

        contractA.mapPricingPolicy(testLicenseA, policyA);
        contractA.mapPricingPolicy(testLicenseB, policyB);
        contractA.mapPricingPolicy(adminFee, policyD);
        contractA.mapPricingPolicy(transactionFee, policyE);

        Contract contractB = enterprise.getScheduleOfContracts().createContract(new Contract("ContractB", enterprise.getJournal()));
        contractB.mapPricingPolicy(testLicenseA, policyA);
        contractB.mapPricingPolicy(testLicenseB, policyC);

        // --- two contracts, same price for License A, different price for LicenseB  ---

        // --- setup an account on ContractA and an account on ContractB
//todo:
//        testParty.changeCurrentContract(contractA, enterprise.getJournal());

        // --- create an order for accountAHolder
//        Order orderA = enterprise.getScheduleOfOrders().createOrder(new Order(testParty, testParty.getCurrentContract(), new Date(), new Interval(12, PeriodTypes.Month), new PrepaidRecurring(PeriodTypes.Month), enterprise.getJournal()));
//
//        // add a license and fees to the order
//        assertTrue(orderA.addProduct(testLicenseA, new Quantity(1, QuantityUnits.Each)));
//        assertTrue(orderA.addProduct(adminFee, new Quantity(1, QuantityUnits.Each)));
//        assertTrue(orderA.addProduct(transactionFee, new Quantity(1, QuantityUnits.Each)));
//
//        orderA.print(System.out);
//
//        em.persist(enterprise);
//
//        TestTools.printAll(em, ProductList.class);
//        TestTools.printAll(em, ScheduleOfTaxes.class);
//        TestTools.printAll(em, ScheduleOfContracts.class);
//
//        TestTools.printAll(em, Order.class);

//        em.flush();
//        enterprise.getScheduleOfOrders().print(System.out);
//
//        // create invoices for the order
//        List<Invoice> invoices = enterprise.getScheduleOfInvoices().createAllInvoices(orderA);
//
//        em.persist(enterprise);
//        em.flush();
//        TestTools.printAll(em, Invoice.class);
//        enterprise.getScheduleOfInvoices().print(System.out);
//
//        // create a receipt for the first invoice
//        Receipt receipt = new Receipt(new Date(), testParty);
//        receipt.applyAmount(invoices.get(0));
//
//        enterprise.getScheduleOfReceipts().createReceipt(receipt);
//
//        em.persist(enterprise);
//        em.flush();
//        TestTools.printAll(em, Receipt.class);
//        enterprise.getScheduleOfReceipts().print(System.out);
//
//        enterprise.getFinancialJournal().print(System.out);

    }


}
