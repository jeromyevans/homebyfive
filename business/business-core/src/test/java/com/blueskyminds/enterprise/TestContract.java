package com.blueskyminds.enterprise;

import com.blueskyminds.homebyfive.framework.core.test.DbTestCase;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceSession;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.framework.core.measurement.QuantityUnits;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;
import com.blueskyminds.enterprise.pricing.*;
import com.blueskyminds.enterprise.taxpolicy.GST;
import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.enterprise.pricing.policy.RecurringPrice;
import com.blueskyminds.enterprise.pricing.policy.FixedPrice;
import com.blueskyminds.enterprise.pricing.policy.TransactionFee;
import com.blueskyminds.enterprise.party.*;
import com.blueskyminds.enterprise.party.IndividualRole;
import com.blueskyminds.enterprise.license.License;
import com.blueskyminds.enterprise.license.LicenseTypes;
import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;

import java.util.Currency;

/**
 * Test creation and use of contracts
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestContract extends DbTestCase {

    public TestContract(String string) {
        super(string);
    }

    public void testContractPersistence() {
        PersistenceSession session;

        try {
            PersistenceService gateway = getPersistenceService();

            Company blueSkyMinds = AddressTestTools.blueSkyMinds();
            Individual jeromy = AddressTestTools.jeromyEvans();
            blueSkyMinds.addIndividualRelationship(new IndividualRelationship(blueSkyMinds, jeromy, "Director", new IndividualRole("Director")));

            session = gateway.openSession();

            Currency AUD = Currency.getInstance("AUD");
            Enterprise testEnterprise = new Enterprise("Test", AUD);
            ScheduleOfContracts soc = testEnterprise.getScheduleOfContracts();

            PricingPolicy policyA = new RecurringPrice(new Price(new Money(200.00, AUD), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
            PricingPolicy policyB = new RecurringPrice(new Price(new Money(250.00, AUD), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
            PricingPolicy policyC = new RecurringPrice(new Price(new Money(230.00, AUD), GST.newInstance(), new Quantity(1, QuantityUnits.Each)), PeriodTypes.Month);
            PricingPolicy policyD = new FixedPrice(new Price(new Money(10.00, AUD), GST.newInstance(), new Quantity(1, QuantityUnits.Each)));
            PricingPolicy policyE = new TransactionFee(new Price(new Money(2.00, AUD), GST.newInstance(), new Quantity(1, QuantityUnits.Each)));

            Contract contractA = new Contract("ContractA", testEnterprise.getJournal());
            soc.createContract(contractA);

            // create a new license in the unallocated account
            License testLicenseA = new License(LicenseTypes.Exclusive);
            contractA.mapPricingPolicy(testLicenseA, policyA);
            testEnterprise.getProductList().createProduct(testLicenseA);

            //gateway.save(sop);
            gateway.save(testEnterprise);
//            gateway.save(policyA);
//            gateway.save(region);
//            gateway.save(testLicenseA);
            //gateway.save(testEnterprise);

            AddressTestTools.printProducts(gateway);
            AddressTestTools.printContracts(gateway);
            TestTools.printAll(ContractProductPriceMap.class, gateway);
            //TestTools.printAll(PricingPolicy.class);
            //gateway.save(blueSkyMinds, session);
            //gateway.save(jeromy, session);

            gateway.closeSession();

        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
