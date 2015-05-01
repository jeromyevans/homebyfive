package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.party.service.PartyService;
import com.blueskyminds.homebyfive.business.party.service.PartyServiceImpl;
import com.blueskyminds.homebyfive.business.party.dao.PartyDAO;
import com.blueskyminds.homebyfive.business.contact.*;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpression;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.Enterprise;

import java.util.Set;
import java.util.Currency;
import java.util.Collection;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Tests the PartyService implementation
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public class PartyServiceTest extends PartyTestCase {

    private static final Log LOG = LogFactory.getLog(PartyServiceTest.class);


    public void testPartyPersistence() {
        Organisation blueSkyMinds = AddressTestTools.blueSkyMinds();
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

        Organisation blueSkyMinds = AddressTestTools.blueSkyMinds();
        em.persist(blueSkyMinds);

        assertEquals(4, blueSkyMinds.getPointsOfContact().size());

        Organisation example = new Organisation("Blue Sky Minds Pty Ltd");
        example.addEmailAddress(new EmailAddress("jeromy.evans@blueskyminds.com.au", POCRole.Business));
        example.addPhoneNumber(new PhoneNumber("02 99225423", POCRole.Business, PhoneNumberTypes.Fax));

        // this should be an instance of the first company, merged with the second
        Organisation blueSkyMinds2 = partyService.createOrMergeOrganisation(example, null);

        assertEquals(blueSkyMinds.getId(), blueSkyMinds2.getId());

        assertEquals(6, blueSkyMinds2.getPointsOfContact().size());
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

    public void testListPartiesByTagExpression() throws Exception {

        Individual individual1 = partyService.createOrMergeIndividual(createTestIndividual1());
        Individual individual2 = partyService.createOrMergeIndividual(createTestIndividual2());

        TagExpression a  = tagExpressionFactory.evaluate("a");
        TagExpression bOrC  = tagExpressionFactory.evaluate("b or c");
        TagExpression b  = tagExpressionFactory.evaluate("b");
        TagExpression c  = tagExpressionFactory.evaluate("c");
        TagExpression aAndC  = tagExpressionFactory.evaluate("a and c");
        TagExpression notA = tagExpressionFactory.evaluate("not a");
        TagExpression notB = tagExpressionFactory.evaluate("not b");

        assertResultSize(1, a);
        assertResultSize(2, bOrC);
        assertResultSize(2, b);
        assertResultSize(1, c);
        assertResultSize(0, aAndC);
        assertResultSize(1, notA);
        assertResultSize(0, notB);

    }

    protected void assertResultSize(int size, TagExpression tagExpression) {
        Set<Party> result1 = partyService.listParties(tagExpression);
        LOG.info("size="+result1.size()+" for "+tagExpression.asString());
        assertEquals(size, result1.size());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
