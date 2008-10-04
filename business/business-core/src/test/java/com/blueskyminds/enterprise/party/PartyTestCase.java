package com.blueskyminds.enterprise.party;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.enterprise.contact.EmailAddress;
import com.blueskyminds.enterprise.contact.POCRole;
import com.blueskyminds.enterprise.contact.PhoneNumberTypes;
import com.blueskyminds.enterprise.contact.ContactAddress;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.tag.service.TagService;
import com.blueskyminds.enterprise.tag.service.TagServiceImpl;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.enterprise.region.Countries;
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

    private static final String PARTY_PERSISIENCE_UNIT = "TestEnterprisePersistenceUnit";

    protected static final String TEST_EMAIL = "jeromy.evans@blueskyminds.com.au";

    protected AddressService addressService;
    protected TagService tagService;


    public PartyTestCase() {
        super(PARTY_PERSISIENCE_UNIT);
    }

    /**
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        addressService = new AddressServiceImpl(em);
        tagService = new TagServiceImpl(em);
    }


    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Individual createTestIndividual1() {
        Tag testTag = tagService.lookupOrCreateTag("test");
        Individual individual = new Individual("Jeromy", "Evans");
        EmailAddress emailAddress = individual.addEmailAddress(TEST_EMAIL, POCRole.Business);
        emailAddress.addTag(testTag);
        individual.addPhoneNumber("0438951541", POCRole.Personal, PhoneNumberTypes.Mobile);
        individual.addPhoneNumber("0299225427", POCRole.Personal, PhoneNumberTypes.Fixed);
        try {
            Address address = addressService.lookupOrCreateAddress("1/22 Spruson Street Neutral Bay NSW 2089", Countries.AUS);
            individual.addStreetAddress(new ContactAddress(address, POCRole.Personal));
        } catch(AddressProcessingException e) {
            LOG.error(e);
        }

        individual.addTag(testTag);
        return individual;
    }
}
