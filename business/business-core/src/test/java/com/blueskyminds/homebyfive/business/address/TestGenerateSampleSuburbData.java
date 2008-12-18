package com.blueskyminds.homebyfive.business.address;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.StopWatch;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.FileTools;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvOptions;
import com.blueskyminds.homebyfive.framework.core.persistence.jdbc.PersistenceTools;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.service.*;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.tools.AusPostCodes;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.*;

/**
 * An application that generates the Australian suburb data for insertion into a database.  Runs in several steps:
 *   1. reads all the data from csv
 *   2. loads into the database using hibernate
 *   3. unloads all the data into a text file
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class TestGenerateSampleSuburbData extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestGenerateSampleSuburbData.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";
    private static final String TARGET_PATH = "business-test/src/main/resources/";

    private CountryService countryService;
    private StateService stateService;
    private SuburbService suburbService;
    private PostalCodeService postalCodeService;

    public TestGenerateSampleSuburbData() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    private static void unloadTable(Connection connection, String tableName) throws IOException, SQLException {
        FileTools.writeTextFile(TARGET_PATH+tableName+".sql", PersistenceTools.unload(connection, tableName));
    }

    public void initialiseCountryList() {
        LOG.info("InitialiseCountryList: ");

        int countries = 0;

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            try {
                if (StringUtils.isNotBlank(locale.getDisplayCountry())) {
                    Country country = new Country(locale.getDisplayCountry(), locale.getCountry(), locale.getISO3Country());
                    Country thisCountry = countryService.create(country);
                    LOG.debug("Created country: "+thisCountry.getName());
                    countries++;
                }
            } catch (IllegalArgumentException e) {
                // thrown then not all of the fields are available for the locale
            } catch (InvalidRegionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (DuplicateRegionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        LOG.info("Created "+countries+" countries");
    }

    public void initialiseAustralianStates() {
        LOG.info("InitialiseAustralianStates: ");

        Country australia = countryService.lookupCountry(Countries.AU);

        try {
            stateService.create(new State(australia, "Western Australia", "WA"));
            stateService.create(new State(australia, "South Australia", "SA"));
            stateService.create(new State(australia, "Victoria", "VIC"));
            stateService.create(new State(australia, "Tasmania", "TAS"));
            stateService.create(new State(australia, "New South Wales", "NSW"));
            stateService.create(new State(australia, "Australian Capital Territory", "ACT"));
            stateService.create(new State(australia, "Queensland", "QLD"));
            stateService.create(new State(australia, "Northern Territory", "NT"));
            LOG.info("Created 9 states/territories in AUS");
        } catch (DuplicateRegionException e) {
            e.printStackTrace();
        } catch (InvalidRegionException e) {
            e.printStackTrace();
        }

    }

    // ------------------------------------------------------------------------------------------------------

    private static final String SUBURB_CATEGORY = "delivery area";
    private static final String PO_BOX_CATEGORY = "post office boxes";

    /** Loads a CSV file of all the suburbs defined in Australia and commits these to the database */
    public void initialiseAustralianSuburbs() throws Exception {
        LOG.info("InitialiseAustralianSuburbs: ");
        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);

        Collection<AusPostCodes.State> states = AusPostCodes.readCsv(ResourceTools.openStream("pc-full_20080303.csv"));

        Map<String, State> stateHash = new HashMap<String, State>();
        Map<String, PostalCode> postCodeHash = new HashMap<String, PostalCode>();
        Map<String, Map<String, Suburb>> suburbHash = new HashMap<String, Map<String, Suburb>>();

        int suburbCount = 0;
        int postCodeCount = 0;

        Country australia = countryService.lookupCountry(Countries.AU);

        for (AusPostCodes.State thisState: states) {

            State state = stateService.lookup(australia, thisState.getName());
            Map<String, Suburb> suburbs = suburbHash.get(thisState.getName());
            if (suburbs == null) {
                suburbs = new HashMap<String, Suburb>();
                suburbHash.put(thisState.getName(), suburbs);
            }

            for (AusPostCodes.Suburb thisSuburb : thisState.getSuburbs()) {


                String postCodeName = thisSuburb.getPostCode().getName();
                PostalCode postalCode = postCodeHash.get(postCodeName);
                if (postalCode == null) {
                    postalCode = postalCodeService.create(new PostalCode(state, postCodeName));
                    postCodeHash.put(postCodeName, postalCode);
                    postCodeCount++;
                }

                Suburb suburb = suburbs.get(thisSuburb.getName());
                if (suburb == null) {
                    LOG.info("***"+thisSuburb.getName());
                    suburb = suburbService.create(new Suburb(state, thisSuburb.getName()));
                    suburbs.put(thisSuburb.getName(), suburb);
                    suburbCount++;
                }

                postalCode.addChildRegion(suburb);

                em.persist(postalCode);
                em.persist(suburb);
               

                if (LOG.isInfoEnabled()) {
                    if (suburbCount % 100 == 0) {
                        LOG.info(suburbs+" suburbs "+postCodeCount+" postcodes");
                        DebugTools.printAvailableHeap();
                        em.flush();
                    }
                }
            }
        }

        em.persist(australia);
        em.flush();
        LOG.info("Created "+suburbCount+" suburbs and "+postCodeCount+" postcodes");
    }

    public void testGenerateSampleData() throws Exception {
        AddressService addressService = new AddressServiceImpl(em);
        TagDAO tagDAO = new TagDAO(em);
        TagService tagService = new TagServiceImpl(tagDAO);
        CountryEAO countryEAO = new CountryEAO(em);
        countryService = new CountryServiceImpl(em, tagService, countryEAO);
        StateEAO stateEAO = new StateEAO(em);
        stateService = new StateServiceImpl(em, tagService,  countryService, stateEAO);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        suburbService = new SuburbServiceImpl(em, tagService, stateService, suburbEAO);
        PostCodeEAO postalCodeEAO = new PostCodeEAO(em);
        postalCodeService = new PostalCodeServiceImpl(em, tagService, stateService, postalCodeEAO);
        AddressTestTools.initialiseRegionIndexes(getConnection());

        initialiseCountryList();
        initialiseAustralianStates();
        initialiseAustralianSuburbs();

        for (String table : AddressTestTools.REGION_TABLES) {
            unloadTable(getConnection(), table);
        }
    }
}
