package com.blueskyminds.enterprise.address;

import com.blueskyminds.homebyfive.framework.framework.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.framework.tools.csv.CsvOptions;
import com.blueskyminds.homebyfive.framework.framework.tools.csv.CsvTextReader;
import com.blueskyminds.homebyfive.framework.framework.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.framework.tools.FileTools;
import com.blueskyminds.homebyfive.framework.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.AddressTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

/**
 * A test-case application that generates a large number of Australian addresses for insertion into a database.
 *
 * Runs in several steps:
 *   1. reads all the data from csv
 *   2. loads into the database using hibernate
 *   3. unloads all the data into a text file
 *
 * Date Started: 7/07/2007
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class TestGenerateSampleAddresses extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestGenerateSampleAddresses.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";
    private static final int MAX_ADDRESSES = 30;
    private static final String AUS = "AUS";

    private static void unloadTable(Connection connection, String tableName, String targetPath) throws IOException, SQLException {        
        FileTools.writeTextFile(targetPath+tableName+".sql", PersistenceTools.unload(connection, tableName));
    }

    /**
     * Initialise some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        em.flush();
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String TARGET_PATH = "src/test/resources/";

    // ------------------------------------------------------------------------------------------------------

    private static final String[] tablesOfInterest = {
            "STREET",
            "SUBURBSTREETMAP",
            "ADDRESS"
    };

    public TestGenerateSampleAddresses() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    public void testGenerateAddresses() throws Exception {
        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        CsvTextReader csvReader = new CsvTextReader(ResourceTools.openStream("exampleAddressesSml.csv"), csvOptions);
        String addressText;
        Address address;
        AddressService addressService = new AddressServiceImpl(em);

        int count = 0;

        while ((csvReader.read()) && (count < MAX_ADDRESSES)) {
            if (csvReader.isNonBlank()) {
                String[] values = csvReader.getAsStrings();
                if (values.length > 0) {
                    addressText = values[0];
                    if ((addressText != null) && (addressText.length() > 0)) {
                        address = addressService.lookupOrCreateAddress(addressText, AUS);
                        //address = addressService.parseAddress(addressText, AUS);
                        if (address != null) {
//                            addressService.lookupOrCreateAddress(address);
                            address.print(System.out);
                            count++;
                            LOG.info("count:"+count);
                        }
                    }
                }
            }
        }

        LOG.info("Created "+count+" addresses.  Unloading tables");
        for (String table : tablesOfInterest) {
            unloadTable(getConnection(), table, TARGET_PATH);
        }        
    }
}
