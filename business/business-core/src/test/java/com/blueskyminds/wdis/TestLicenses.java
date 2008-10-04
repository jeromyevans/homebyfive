package com.blueskyminds.wdis;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.RegionFactory;
import com.blueskyminds.enterprise.license.*;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.party.PartyTypes;
import com.blueskyminds.enterprise.party.ScheduleOfParties;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceSession;
import com.blueskyminds.homebyfive.framework.core.test.DbTestCase;

import java.util.Collection;
import java.util.Currency;

/**
 * Test the functionality of the license scheme
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestLicenses extends DbTestCase {

    public TestLicenses(String s) {
       super(s);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Test the creation of licenses
     */
    public void testLicenseCreation() {

        Journal journal = new Journal();

        ScheduleOfParties sop = new ScheduleOfParties(null, journal);
        Party testParty = sop.createParty(new Party(PartyTypes.Company));


        Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));
        LicenseJournal licenseJournal = new LicenseJournal(enterprise, journal);
        ScheduleOfLicenses sol = new ScheduleOfLicenses(null, licenseJournal, journal);
        //ScheduleOfAccounts schedule = new ScheduleOfAccounts(null, journal);
        ScheduleOfLicenseAccounts sola = new ScheduleOfLicenseAccounts(enterprise, journal);

        // setup a test account
        LicenseAccount accountA = sola.createLicenseAccount(testParty);
        LicenseAccount accountB = sola.createLicenseAccount(testParty);

        //RegionType postCode = new RegionType(RegionTypes.PostCode);
        Region region = new RegionFactory().createPostCode("TestRegion");

        // create a new license in the unallocated account
        License testLicenseA = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseB = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseC = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());
        License testLicenseD = sol.createLicense(new RegionLicense(region, LicenseTypes.Exclusive), sola.getUnallocatedAccount());

        // reserve the license for account A
        try {
            sol.reserveLicense(testLicenseA, sola.getUnallocatedAccount(), accountA, "Reserved for testParty", testParty);
            assertTrue(accountA.hasLicenseReserved(testLicenseA));
            assertFalse(sola.getUnallocatedAccount().hasLicenseAllocated(testLicenseA));
        }
        catch (IllegalTransactionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        // unreserve the license for account A back to the unallocated account
        try {
            sol.unreserveLicense(testLicenseA, accountA, sola.getUnallocatedAccount(), "Unreserved", testParty);
            assertTrue(sola.getUnallocatedAccount().hasLicenseAllocated(testLicenseA));
            assertFalse(accountA.hasLicenseAllocated(testLicenseA));
            assertFalse(accountA.hasLicenseReserved(testLicenseA));
        }
        catch (IllegalTransactionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            // make the license unavailable
            sol.makeLicenseUnavailable(testLicenseA, sola.getUnallocatedAccount(), sola.getUnavailableAccount(), "Unavailable", testParty);
        }
        catch (IllegalTransactionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            // try to acquire it
            sol.allocateLicense(testLicenseA, sola.getUnallocatedAccount(), accountA, "Allocate", testParty);
            fail();
        }
        catch (IllegalTransactionException e) {
            // this should fail because it's not in the unallocatedAccount
        }

        try {
            // make the license available
            sol.makeLicenseAvailable(testLicenseA, sola.getUnavailableAccount(), sola.getUnallocatedAccount(), "Make avail", testParty);
            sol.allocateLicense(testLicenseA, sola.getUnallocatedAccount(), accountA, "allocate", testParty);
            sol.allocateLicense(testLicenseB, sola.getUnallocatedAccount(), accountA, "allocate", testParty);
            sol.allocateLicense(testLicenseC, sola.getUnallocatedAccount(), accountA, "allocate", testParty);
            sol.reserveLicense(testLicenseD, sola.getUnallocatedAccount(), accountA, "allocate", testParty);
            assertTrue(accountA.hasLicenseAllocated(testLicenseA));
            assertTrue(accountA.hasLicenseAllocated(testLicenseB));
            assertTrue(accountA.hasLicenseAllocated(testLicenseC));
            assertTrue(accountA.hasLicenseReserved(testLicenseD));
            assertFalse(accountA.hasLicenseReserved(testLicenseC));
            sol.unreserveLicense(testLicenseD, accountA, sola.getUnallocatedAccount(), "unreserve", testParty);

            sola.print(System.out);
            licenseJournal.print(System.out);
        }
        catch (IllegalTransactionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }


    public void testLicensePersistence() {
        PersistenceSession session;

        try {
            PersistenceService persistenceService = getPersistenceService();
            Enterprise enterprise = new Enterprise("TestEnterprise", Currency.getInstance("AUD"));

            LicenseJournal licenseJournal = new LicenseJournal(enterprise, enterprise.getJournal());
            ScheduleOfLicenses sol = new ScheduleOfLicenses(null, licenseJournal, enterprise.getJournal());
            ScheduleOfLicenseAccounts sola = new ScheduleOfLicenseAccounts(enterprise, enterprise.getJournal());
            Party testParty = enterprise.getScheduleOfParties().createParty(new Party(PartyTypes.Company));

            // setup a test account
            LicenseAccount accountA = sola.createLicenseAccount(testParty);
            LicenseAccount accountB = sola.createLicenseAccount(testParty);

            // create a new license in the unallocated account
            License testLicenseA = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
            License testLicenseB = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
            License testLicenseC = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());
            License testLicenseD = sol.createLicense(new License(LicenseTypes.Exclusive), sola.getUnallocatedAccount());

            persistenceService.save(enterprise);
            persistenceService.save(licenseJournal);
            persistenceService.save(sol);
            persistenceService.save(sola);

            licenseJournal.print(System.out);

            session = persistenceService.openSession();
            Collection<ScheduleOfLicenseAccounts> solaList = persistenceService.findAll(ScheduleOfLicenseAccounts.class);
            for (ScheduleOfLicenseAccounts sola2 : solaList) {
                sola2.print(System.out);
            }
            persistenceService.closeSession();
        }
        catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    // ------------------------------------------------------------------------------------------------------

}
