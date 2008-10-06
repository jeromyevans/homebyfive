package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.party.service.PartyService;
import com.blueskyminds.homebyfive.business.party.service.PartyServiceImpl;
import com.blueskyminds.homebyfive.business.contact.POCType;
import com.blueskyminds.homebyfive.business.contact.EmailAddress;
import com.blueskyminds.homebyfive.business.contact.PointOfContact;
import com.blueskyminds.homebyfive.business.contact.PartyPOC;

import java.util.Set;

/**
 * Tests the PartyService implementation
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public class TestPartyService extends PartyTestCase {

    protected PartyService partyService;
    /**
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        partyService = new PartyServiceImpl(em);
    }

    public void testIndividualCreation() throws Exception {
        Individual individual = partyService.createOrMergeIndividual(createTestIndividual1());
        assertNotNull(individual);
        Set<PointOfContact> emailAddresses = individual.getPointsOfContactOfType(POCType.EmailAddress);
        assertNotNull(emailAddresses);
        assertTrue(emailAddresses.size() == 1);

        Set<Party> parties = partyService.findIndividualsByTag("test");
        assertNotNull(parties);
        assertTrue(parties.size() > 0);

        Set<PartyPOC> pocs = partyService.findPointOfContactsByTag("test");
        assertNotNull(pocs);
        assertTrue(pocs.size() > 0);
        PartyPOC partyPOC = pocs.iterator().next();
        assertNotNull(partyPOC);
        assertEquals(individual, partyPOC.getParty());
        assertEquals(POCType.EmailAddress, partyPOC.getType());
        assertEquals(TEST_EMAIL, partyPOC.getPointOfContact().getValue());
        EmailAddress emailAddress = (EmailAddress) partyPOC.getPointOfContact();
        assertNotNull(emailAddress);

        Set<PartyPOC> pocs2 = partyService.findPointOfContactsByTag("test", POCType.EmailAddress);
        assertNotNull(pocs2);
        assertTrue(pocs2.size() > 0);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
