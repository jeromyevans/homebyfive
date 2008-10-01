package com.blueskyminds.enterprise;

import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.persistence.jdbc.PersistenceTools;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceService;
import com.blueskyminds.enterprise.party.Company;
import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.enterprise.party.Title;
import com.blueskyminds.enterprise.contact.*;
import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.pricing.Contract;
import com.blueskyminds.enterprise.pricing.Product;
import com.blueskyminds.enterprise.region.country.CountryFactory;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.state.StateFactory;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeFactory;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbFactory;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A simple class for generating test data
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AddressTestTools extends TestTools {

    private static final Log LOG = LogFactory.getLog(AddressTestTools.class);
    public static final String CREATE_REGION_PARENT_INDEX = "create index RegionParentIndex on RegionMap (parentId)";
    public static final String CREATE_REGION_CHILD_INDEX = "create index RegionChildIndex on RegionMap (parentId)";

    public static Company blueSkyMinds() {
        Company blueSkyMinds = new Company("Blue Sky Minds Pty Ltd");
        blueSkyMinds.addEmailAddress(new EmailAddress("info@blueskyminds.com.au", POCRole.Business));
        blueSkyMinds.addStreetAddress(new ContactAddress(new PlainTextAddress("1/22 Spruson Street, Neutral Bay NSW 2089"), POCRole.Business));
        blueSkyMinds.addPhoneNumber(new PhoneNumber("0438951541", POCRole.Business, PhoneNumberTypes.Mobile));
        blueSkyMinds.addPhoneNumber(new PhoneNumber("02 99225427", POCRole.Business, PhoneNumberTypes.Fixed));

        return blueSkyMinds;
    }

    public static Individual jeromyEvans() {
        Individual jeromy = new Individual("Jeromy", "Evans");
        jeromy.setInitials("JRE");
        jeromy.setMiddleInitial("R");
        jeromy.setTitle(Title.Mr);

        EmailAddress emailAddress = new EmailAddress("jeromy.evans@blueskyminds.com.au", POCRole.Business);
        emailAddress.addRole(POCRole.Personal);  // dual roles
        jeromy.addEmailAddress(emailAddress);

        return jeromy;
    }

    public static Individual jeromyEvans2() {
        Individual jeromy = new Individual("Jeromy", "Evans");
        jeromy.setInitials("JRE");
        jeromy.setMiddleInitial("R");
        jeromy.setTitle(Title.Mr);
        jeromy.setMiddleName("Robert");

        EmailAddress emailAddress = new EmailAddress("jeromyevans@exemail.com.au", POCRole.Personal);
        emailAddress.addRole(POCRole.Personal);  // dual roles
        jeromy.addEmailAddress(emailAddress);

        PhoneNumber mobile = new PhoneNumber("0438951541", POCRole.Business, PhoneNumberTypes.Mobile);
        jeromy.addPhoneNumber(mobile);

        return jeromy;
    }

    public static void printRegions(PersistenceService persistenceService) {
        printAll(RegionHandle.class, persistenceService);
    }

     public static void printContracts(PersistenceService persistenceService) {
        printAll(Contract.class, persistenceService);
    }

     public static void printProducts(PersistenceService persistenceService) {
        printAll(Product.class, persistenceService);
    }

    public static void printRegions(EntityManager em) {
        printAll(em, RegionHandle.class);
    }

     public static void printContracts(EntityManager em) {
        printAll(em, Contract.class);
    }

     public static void printProducts(EntityManager em) {
        printAll(em, Product.class);
    }

    public static void initialiseAddresses(EntityManager em) {
        CountryHandle australia = new CountryFactory().createCountry("Australia");

        StateHandle nsw = australia.addState(new StateFactory().createState("New South Wales", "NSW"));

        PostCodeHandle nbPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2089"));
        PostCodeHandle kPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2060"));
        PostCodeHandle mPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2088"));

        SuburbHandle mosmon = nsw.addSuburb(new SuburbFactory().createSuburb("Mosmon", mPostCode));
        SuburbHandle neutralBay = nsw.addSuburb(new SuburbFactory().createSuburb("Neutral Bay", nbPostCode));
        SuburbHandle kirribilli = nsw.addSuburb(new SuburbFactory().createSuburb("Kirribilli", kPostCode));

        Street sprusonStreet = neutralBay.addStreet(new Street("Spruson", StreetType.Street));
        Street philipStreet = neutralBay.addStreet(new Street("Phillip", StreetType.Street));
        Street militaryRoad = neutralBay.addStreet(new Street("Military", StreetType.Road));
        mosmon.addStreet(militaryRoad);
        Street carabellaStreet = kirribilli.addStreet(new Street("Carabella", StreetType.Street));

        Address addressA = new UnitAddress("1", "22", sprusonStreet, neutralBay, nbPostCode);
        Address addressB = new UnitAddress("3", "60", carabellaStreet, kirribilli, kPostCode);

        em.persist(australia);
        em.persist(addressA);
        em.persist(addressB);
        em.flush();

    }

    public static void initialisePlainAddresses(EntityManager em) {

        em.persist(testAddressA());
        em.persist(new PlainTextAddress("102/12 Glen Street, Milsons Point NSW 2060"));
        em.persist(new PlainTextAddress("3/60 Carabella Street, Kirribilli NSW 2060"));

        em.flush();
    }


    public static Address testAddressA() {
        return new PlainTextAddress("1/22 Spruson Street, Neutral Bay NSW 2089");
    }

    private static final String[] COUNTRY_SQL_FILES = {
            "COUNTRY",
            "POSTCODE",
            "STATE",
            "SUBURB",
            "REGIONHANDLE",
            "REGIONALIAS",
            "REGIONHIERARCHY"

    };

    private static final String[] ADDRESS_SQL_FILES = {
            "STREET",
            "SUBURBSTREETMAP",
            "ADDRESS"
    };

    public static final String AUSTRALIA = "Australia";
    
    /** Initialise all of the reference region data.
     * This method uses a direct JDBC connection to the in-memory hypersonic database */
    public static void initialiseCountryList() {
        try {
            createHSQLDB();
            Connection connection = getConnection();

            initialiseCountryList(connection);
            initialiseRegionIndexes(connection);
        } catch(SQLException e) {
            LOG.error("SQL error", e);
        }        

    }

    /** Initialise all of the reference region data.
     * This method uses a direct JDBC connection */
    public static void initialiseCountryList(Connection connection) {
         applySQLFiles(connection, COUNTRY_SQL_FILES);
    }

    /** Initialise a bunch of sample addresses for testing
     * hsqldb:mem:mem only */
    public static void initialiseSampleAusAddresses() {
        initialiseSampleAusAddresses(getConnection());
    }

    /** Initialise a bunch of sample addresses for testing
     * This method uses a direct JDBC connection */
    public static void initialiseSampleAusAddresses(Connection connection) {
        applySQLFiles(connection, ADDRESS_SQL_FILES);
    }

    @Deprecated
    public static void initialiseAustralianStates() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Deprecated
    public static void initialiseAustralianSuburbs() {
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String ADDRESS_PATTERNS_FILE_NAME = "addressSubstitutions.csv";

    /** Loads a CSV file of all the patterns defined for cleansing addresses */
    public static void initialiseAddressSubstitutionPatterns(EntityManager em) {
        List<Substitution> substitutions;
        int patterns = 0;
        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        substitutions = substitutionsFileReader.readCsv(ADDRESS_PATTERNS_FILE_NAME);

        for (Substitution substitution : substitutions) {
            em.persist(substitution);
            patterns++;
        }
        em.flush();

        LOG.info("Initialised "+patterns+" substitution patterns");        
    }

    // ------------------------------------------------------------------------------------------------------

    public static void initialiseRegionIndexes(Connection connection) throws SQLException {
        PersistenceTools.executeUpdate(connection, "create index RegionHierarchyChildIndex on RegionHierarchy (childId)");
        PersistenceTools.executeUpdate(connection, "create index RegionHierarchyParentIndex on RegionHierarchy (parentId)");

        PersistenceTools.executeUpdate(connection, "create index RegionHandleIndex on RegionHandle (type, name)");
        PersistenceTools.executeUpdate(connection, "create index RegionHandleTypeIndex on RegionHandle (Impl, name)");
        PersistenceTools.executeUpdate(connection, "create index RegionHandleCountryIndex on RegionHandle (CountryId)");
        PersistenceTools.executeUpdate(connection, "create index RegionHandleStateIndex on RegionHandle (StateId)");
        PersistenceTools.executeUpdate(connection, "create index RegionHandlePostCodeIndex on RegionHandle (PostCodeId)");
        PersistenceTools.executeUpdate(connection, "create index RegionHandleSuburbIndex on RegionHandle (SuburbId)");

        PersistenceTools.executeUpdate(connection, "create index RegionAliasIndex on RegionAlias (RegionHandleId, name)");

        PersistenceTools.executeUpdate(connection, "create index CountryISO3CodeIndex on Country (ISO3DigitCountryCode)");
        PersistenceTools.executeUpdate(connection, "create index CountryISO2CodeIndex on Country (ISO2DigitCountryCode)");

        PersistenceTools.executeUpdate(connection, "create index SubstitutionGroupNameIndex on Substitution (GroupName)");

        PersistenceTools.executeUpdate(connection, "create index SuburbStreetMapSuburbIndex on SuburbStreetMap (SuburbId)");
        PersistenceTools.executeUpdate(connection, "create index SuburbStreetMapStreetIndex on SuburbStreetMap (StreetId)");

        PersistenceTools.executeUpdate(connection, "create index StreetSectionIndex on Street (type, section, name)");
        PersistenceTools.executeUpdate(connection, "create index StreetIndex on Street (type, name)");
    }
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

}
