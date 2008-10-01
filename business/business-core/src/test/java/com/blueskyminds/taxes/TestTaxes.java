package com.blueskyminds.taxes;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.taxpolicy.*;
import com.blueskyminds.homebyfive.framework.framework.measurement.Limits;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceSession;
import com.blueskyminds.homebyfive.framework.framework.test.DbTestCase;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;

/**
 * Functions testing the TaxPolicy implementation
 *
 * Date Started: 2/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestTaxes extends DbTestCase {

    public TestTaxes(String string) {
        super(string);
    }

    public void testTaxCalculation() {


        Currency AUD = Currency.getInstance("AUD");
        TaxPolicy gst = new FixedTax("GST", new BigDecimal("0.10"), AUD, null, null);
        TaxPolicy gstFree = new FixedTax("FRE", new BigDecimal("0.00"), AUD, null, null);

        GraduatedTax testTax = new GraduatedTax("Test", AUD, null, null);
        testTax.defineEntry(new GraduatedTaxEntry(testTax, BigDecimal.ZERO, Limits.GreaterThanOrEqual, new BigDecimal("10000.00"), Limits.LessThan, new BigDecimal("500.00"), new BigDecimal("0.10")));
        testTax.defineEntry(new GraduatedTaxEntry(testTax, new BigDecimal("10000.00"), Limits.GreaterThanOrEqual, new BigDecimal("25000.00"), Limits.LessThan, new BigDecimal("1500.00"), new BigDecimal("0.25")));
        testTax.defineEntry(new GraduatedTaxEntry(testTax, new BigDecimal("25000.00"), Limits.GreaterThanOrEqual, null, Limits.PositveInfinity, new BigDecimal("5250.00"), new BigDecimal("0.50")));

        BigDecimal taxA = gst.calculateTaxAmount(new Money(10000, AUD)).amount();
        assertTrue(taxA.compareTo(new BigDecimal(1000)) == 0);
        BigDecimal taxB = gstFree.calculateTaxAmount(new Money(10000, AUD)).amount();
        assertTrue(taxB.compareTo(BigDecimal.ZERO) == 0);

        BigDecimal taxC = testTax.calculateTaxAmount(new Money(0, AUD)).amount();
        assertTrue(taxC.compareTo(new BigDecimal(500)) == 0);
        BigDecimal taxD = testTax.calculateTaxAmount(new Money(5000, AUD)).amount();
        assertTrue(taxD.compareTo(new BigDecimal(500+500)) == 0);
        BigDecimal taxE = testTax.calculateTaxAmount(new Money(15000, AUD)).amount();
        assertTrue(taxE.compareTo(new BigDecimal(1500+1250)) == 0);
        BigDecimal taxF = testTax.calculateTaxAmount(new Money(25000, AUD)).amount();
        assertTrue(taxF.compareTo(new BigDecimal(5250)) == 0);
        BigDecimal taxG = testTax.calculateTaxAmount(new Money(50000, AUD)).amount();
        assertTrue(taxG.compareTo(new BigDecimal(5250+12500)) == 0);

    }

    public void testTaxPersistence() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));

            ScheduleOfTaxes sot = enterprise.getScheduleOfTaxes();

            TaxPolicy gst = new FixedTax("GST", new BigDecimal("0.10"), enterprise.getDefaultCurrency(), null, null);
            TaxPolicy gstFree = new FixedTax("FRE", new BigDecimal("0.00"), enterprise.getDefaultCurrency(), null, null);
            GraduatedTax testTax = new GraduatedTax("Test", enterprise.getDefaultCurrency(), null, null);
            testTax.defineEntry(new GraduatedTaxEntry(testTax, BigDecimal.ZERO, Limits.GreaterThanOrEqual, new BigDecimal("10000.00"), Limits.LessThan, new BigDecimal("500.00"), new BigDecimal("0.10")));
            testTax.defineEntry(new GraduatedTaxEntry(testTax, new BigDecimal("10000.00"), Limits.GreaterThanOrEqual, new BigDecimal("25000.00"), Limits.LessThan, new BigDecimal("1500.00"), new BigDecimal("0.25")));
            testTax.defineEntry(new GraduatedTaxEntry(testTax, new BigDecimal("25000.00"), Limits.GreaterThanOrEqual, null, Limits.PositveInfinity, new BigDecimal("5250.00"), new BigDecimal("0.50")));

            sot.defineTaxPolicy(gst);
            sot.defineTaxPolicy(gstFree);
            sot.defineTaxPolicy(testTax);

            gateway.save(enterprise);

            session = gateway.openSession();
            Collection<TaxPolicy> taxList = gateway.findAll(TaxPolicy.class);
            for (TaxPolicy taxPolicy : taxList) {
                taxPolicy.print(System.out);
            }

            Collection<ScheduleOfTaxes> sotList = gateway.findAll(ScheduleOfTaxes.class);
            for (ScheduleOfTaxes scheduleOfTaxes : sotList) {
                scheduleOfTaxes.print(System.out);
            }

            gateway.closeSession();

        }
        catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
