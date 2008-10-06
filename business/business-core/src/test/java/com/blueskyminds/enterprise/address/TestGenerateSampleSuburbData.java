package com.blueskyminds.enterprise.address;

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
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvTextReader;
import com.blueskyminds.homebyfive.framework.core.persistence.jdbc.PersistenceTools;
import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.dao.AddressDAO;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Locale;
import java.util.Currency;
import java.util.Map;
import java.util.HashMap;

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
    private static final String TARGET_PATH = "src/test/resources/";

    private static final String AU = "AU";

    public TestGenerateSampleSuburbData() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    private static void unloadTable(Connection connection, String tableName) throws IOException, SQLException {
        FileTools.writeTextFile(TARGET_PATH+tableName+".sql", PersistenceTools.unload(connection, tableName));
    }

    public static void initialiseCountryList(AddressService addressService) {
        LOG.info("InitialiseCountryList: ");

        int countries = 0;

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            try {
                Country thisCountry = addressService.createCountry(locale.getDisplayCountry(), locale.getCountry(), locale.getISO3Country(), Currency.getInstance(locale).getCurrencyCode());
                LOG.debug("Created country: "+thisCountry.getName());
                countries++;
            } catch (IllegalArgumentException e) {
                // thrown then not all of the fields are available for the locale
            }
        }

        LOG.info("Created "+countries+" countries");
    }

    public static void initialiseAustralianStates(AddressService addressService, EntityManager em) {
        LOG.info("InitialiseAustralianStates: ");

        Country australia = new AddressDAO(em).lookupCountry(AU);

        addressService.createState("Western Australia", "WA", australia);
        addressService.createState("South Australia", "SA", australia);
        addressService.createState("Victoria", "VIC", australia);
        addressService.createState("Tasmania", "TAS", australia);
        addressService.createState("New South Wales", "NSW", australia);
        addressService.createState("Australian Capital Territory", "ACT", australia);
        addressService.createState("Queensland", "QLD", australia);
        addressService.createState("Northern Territory", "NT", australia);

        LOG.info("Created 9 states/territories in AUS");
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String SUBURB_CATEGORY = "delivery area";
    private static final String PO_BOX_CATEGORY = "post office boxes";

    /** Loads a CSV file of all the suburbs defined in Australia and commits these to the database */
    public static void initialiseAustralianSuburbs(AddressService addressService, EntityManager em) {
        LOG.info("InitialiseAustralianSuburbs: ");
        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        try {
            CsvTextReader csvReader = new CsvTextReader(ResourceTools.openStream("/src/resources/pc-full_20080303.csv"), csvOptions);
            Map<String, State> stateHash = new HashMap<String, State>();
            Map<String, PostalCode> postCodeHash = new HashMap<String, PostalCode>();
            Map<String, Map<String, Suburb>> suburbHash = new HashMap<String, Map<String, Suburb>>();
            State state;

            int suburbs = 0;
            int postCodes = 0;

            //RegionGraphDAO regionDAO = new RegionGraphDAO(em);
            AddressDAO addressDAO = new AddressDAO(em);
            Country australia = addressDAO.lookupCountry(AU);

            while (csvReader.read()) {
                if (csvReader.isNonBlank()) {
                    String[] values = csvReader.getAsStrings();
                    String postCodeValue = StringUtils.upperCase(values[0]);
                    // assert that the postcode is a numeric value
                    if (StringUtils.isNumeric(postCodeValue)) {
                        String suburbValue = WordUtils.capitalize(StringUtils.lowerCase(values[1]));
                        String stateValue = StringUtils.upperCase(values[2]);
                        String category = StringUtils.lowerCase(StringUtils.trim(values[9]));

                        if (SUBURB_CATEGORY.equals(category)) {

                            // ignore district offices
                            if (!StringUtils.contains(suburbValue, " DC")) {
                                // lookup the state...
                                if (stateHash.containsKey(stateValue)) {
                                    state = stateHash.get(stateValue);
                                } else {
                                    //state = australia.getStateByAbbr(stateValue);
                                    StopWatch stopWatch = new StopWatch();
                                    stopWatch.start();
                                    state = addressDAO.lookupState(stateValue, australia);
                                    stateHash.put(stateValue, state);
                                    // prepare for new suburb hash
                                    suburbHash.put(stateValue, new HashMap<String, Suburb>());
                                    stopWatch.stop();
                                    LOG.info("state query: "+stopWatch);
                                }


                                PostalCode postCodeHandle = postCodeHash.get(postCodeValue);
                                if (postCodeHandle == null) {
                                    // get the post code  - if it doesn't exist already, create a new instance and add it to the
                                    /// state
                                    //postCode = new PostCode(postCodeValue);
                                    postCodeHandle = addressService.createPostCode(postCodeValue, state);
                                    postCodes++;
                                    postCodeHash.put(postCodeValue, postCodeHandle);
                                }

                                // check if the suburb already exists
                                // NOTE: duplicate check is disabled because production data was built without this
                                // duplicatules will need to be removed via a merge operation
                                //SuburbHandle suburbHandle = suburbHash.get(stateValue).get(suburbValue);
                                //if (suburbHandle == null) {
                                    // create a new one
                                    Suburb suburbHandle = addressService.createSuburb(suburbValue, state);
                                    suburbHash.get(stateValue).put(suburbValue, suburbHandle);
                                    suburbs++;
                                //}
                                postCodeHandle.addChildRegion(suburbHandle);

                                em.persist(postCodeHandle);
                                em.persist(suburbHandle);

                                if (LOG.isInfoEnabled()) {
                                    if (suburbs % 100 == 0) {
                                        LOG.info(suburbs+" suburbs "+postCodes+" postcodes");
                                        DebugTools.printAvailableHeap();
                                        em.flush();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            em.persist(australia);
            em.flush();
            LOG.info("Created "+suburbs+" suburbs and "+postCodes+" postcodes");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void testGenerateSampleData() throws Exception {
        AddressService addressService = new AddressServiceImpl(em);

        AddressTestTools.initialiseRegionIndexes(getConnection());        

        initialiseCountryList(addressService);
        initialiseAustralianStates(addressService, em);
        initialiseAustralianSuburbs(addressService, em);

        for (String table : AddressTestTools.REGION_TABLES) {
            unloadTable(getConnection(), table);
        }
    }
}
