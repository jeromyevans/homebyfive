package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.contact.EmailAddress;
import com.blueskyminds.homebyfive.business.contact.POCRole;
import com.blueskyminds.homebyfive.business.contact.PhoneNumberTypes;
import com.blueskyminds.homebyfive.business.contact.ContactAddress;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.service.AddressProcessingException;
import com.blueskyminds.homebyfive.business.address.Address;
import com.blueskyminds.homebyfive.business.address.AddressTestCase;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.factory.CachingTagFactory;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpressionFactory;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.party.dao.PartyDAO;
import com.blueskyminds.homebyfive.business.party.dao.OrganisationDAO;
import com.blueskyminds.homebyfive.business.party.dao.IndividualDAO;
import com.blueskyminds.homebyfive.business.party.service.PartyServiceImpl;
import com.blueskyminds.homebyfive.business.party.service.PartyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Standard testsuite methods for Parties
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public class PartyTestCase extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(PartyTestCase.class);

    protected static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    protected static final String TEST_EMAIL = "jeromy.evans@blueskyminds.com.au";

    protected TagDAO tagDAO;
    protected TagService tagService;
    protected TagExpressionFactory tagExpressionFactory;

    protected PartyDAO partyDAO;
    protected PartyService partyService;
    private OrganisationDAO organisationDAO;
    private IndividualDAO individualDAO;

    public PartyTestCase() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    /**
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        tagDAO = new TagDAO(em);
        tagService = new TagServiceImpl(tagDAO);
        tagExpressionFactory = new TagExpressionFactory(new CachingTagFactory(tagService));

        partyDAO = new PartyDAO(em);
        individualDAO = new IndividualDAO(em);
        organisationDAO = new OrganisationDAO(em);
        partyService = new PartyServiceImpl(partyDAO, individualDAO, organisationDAO);
    }


    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Create an individual with tags "test, a, b", an email, two phone numbers 
     *  
     * @return
     */
    protected Individual createTestIndividual1() {
        Tag testTag = tagService.lookupOrCreateTag("test");
        Tag a = tagService.lookupOrCreateTag("a");
        Tag b = tagService.lookupOrCreateTag("b");

        em.flush();

        Individual individual = new Individual("Jeromy", "Evans").
                withTags(testTag, a, b);               

        EmailAddress emailAddress = individual.addEmailAddress(TEST_EMAIL, POCRole.Business);
        emailAddress.addTag(testTag);
        individual.addPhoneNumber("0438951541", POCRole.Personal, PhoneNumberTypes.Mobile);
        individual.addPhoneNumber("0299225427", POCRole.Personal, PhoneNumberTypes.Fixed);

        return individual;
    }

      /**
     * Create an individual with tags "test, b, c", an email, two phone numbers 
     *
     * @return
     */
    protected Individual createTestIndividual2() {
        Tag testTag = tagService.lookupOrCreateTag("test");
        Tag b = tagService.lookupOrCreateTag("b");
        Tag c = tagService.lookupOrCreateTag("c");

        em.flush();

        Individual individual = new Individual("Bill", "Evans").
                withTags(testTag, b, c);

        EmailAddress emailAddress = individual.addEmailAddress("bill@blueskyminds.com.au", POCRole.Business);
        emailAddress.addTag(testTag);
        individual.addPhoneNumber("0438951541", POCRole.Personal, PhoneNumberTypes.Mobile);
        individual.addPhoneNumber("0299225427", POCRole.Personal, PhoneNumberTypes.Fixed);

        return individual;
    }
}
