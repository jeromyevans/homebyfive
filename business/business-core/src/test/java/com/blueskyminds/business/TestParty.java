package com.blueskyminds.business;

import com.blueskyminds.business.Enterprise;
import com.blueskyminds.business.party.*;
import com.blueskyminds.business.party.IndividualRole;
import com.blueskyminds.business.party.dao.PartyDAO;
import com.blueskyminds.business.party.service.PartyService;
import com.blueskyminds.business.party.service.PartyServiceImpl;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.business.contact.*;

import java.util.Currency;
import java.util.Collection;

/**
 *
 * Test creation and deletion of a party
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestParty extends JPATestCase {

    private static final String PARTY_PERSISIENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestParty() {
        super(PARTY_PERSISIENCE_UNIT);
    }

    // ------------------------------------------------------------------------------------------------------

    private Party initParty() {
        Company blueSkyMinds = AddressTestTools.blueSkyMinds();

        Individual jeromy = AddressTestTools.jeromyEvans();

        blueSkyMinds.addIndividualRelationship(new IndividualRelationship(blueSkyMinds, jeromy, "Director", new IndividualRole("Director")));

        return blueSkyMinds;
    }

    // ------------------------------------------------------------------------------------------------------

    public void testPartyPersistence() {
        Company blueSkyMinds = AddressTestTools.blueSkyMinds();
        Individual jeromy = AddressTestTools.jeromyEvans();
        blueSkyMinds.addIndividualRelationship(new IndividualRelationship(blueSkyMinds, jeromy, "Director", new IndividualRole("Director")));

        Enterprise testEnterprise = new Enterprise("Test", Currency.getInstance("AUD"));
        ScheduleOfParties sop = testEnterprise.getScheduleOfParties();

        sop.createParty(blueSkyMinds);
        sop.createParty(jeromy);
        em.persist(testEnterprise);
        em.flush();
        Collection<Party> parties = new PartyDAO(em).findAll();

        for (Party party : parties) {
            party.print(System.out);
            System.out.println();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Tests that two individual records can be merged */
    public void testMergeParty() {

        Individual jeromy1 = AddressTestTools.jeromyEvans();
        Individual jeromy2 = AddressTestTools.jeromyEvans2();

        assertEquals(1, jeromy1.getPointsOfContact().size());
        assertEquals(2, jeromy2.getPointsOfContact().size());

        assertEquals(null, jeromy1.getMiddleName());
        assertEquals("Robert", jeromy2.getMiddleName());

        jeromy1.mergeWith(jeromy2);

        assertEquals(3, jeromy1.getPointsOfContact().size());
        assertEquals("Robert", jeromy1.getMiddleName());
    }

    public void testOrganisationLookup() throws Exception {

        Company blueSkyMinds = AddressTestTools.blueSkyMinds();
        em.persist(blueSkyMinds);

        assertEquals(4, blueSkyMinds.getPointsOfContact().size());

        Company example = new Company("Blue Sky Minds Pty Ltd");
        example.addEmailAddress(new EmailAddress("jeromy.evans@blueskyminds.com.au", POCRole.Business));
        example.addPhoneNumber(new PhoneNumber("02 99225423", POCRole.Business, PhoneNumberTypes.Fax));

        PartyService partyService = new PartyServiceImpl(em);

        // this should be an instance of the first company, merged with the second
        Organisation blueSkyMinds2 = partyService.createOrMergeOrganisation(example, null);

        assertEquals(blueSkyMinds.getId(), blueSkyMinds2.getId());

        assertEquals(6, blueSkyMinds2.getPointsOfContact().size());
    }
}
