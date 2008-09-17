package com.blueskyminds.wdis;

import com.blueskyminds.enterprise.address.PlainTextAddress;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.*;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Currency;
import java.util.Date;

/**
 * A simple class for generating test data
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class WdisTestTools extends TestTools {

    private static final Log LOG = LogFactory.getLog(WdisTestTools.class);

    /*public static Company blueSkyMinds() {
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
        printAll(Region.class, persistenceService);
    }

     public static void printContracts(PersistenceService persistenceService) {
        printAll(Contract.class, persistenceService);
    }

     public static void printProducts(PersistenceService persistenceService) {
        printAll(Product.class, persistenceService);
    }

    public static void initialiseAddresses(PersistenceService persistenceService) {
        Country australia = new Country("Australia");

        State nsw = australia.addState(new State("New South Wales", "NSW", RegionTypes.State));

        PostCode nbPostCode = nsw.addPostCode(new PostCode("2089"));
        PostCode kPostCode = nsw.addPostCode(new PostCode("2060"));
        PostCode mPostCode = nsw.addPostCode(new PostCode("2088"));

        Suburb mosmon = nsw.addSuburb(new Suburb("Mosmon", RegionTypes.Suburb, mPostCode));
        Suburb neutralBay = nsw.addSuburb(new Suburb("Neutral Bay", RegionTypes.Suburb, nbPostCode));
        Suburb kirribilli = nsw.addSuburb(new Suburb("Kirribilli", RegionTypes.Suburb, kPostCode));

        Street sprusonStreet = neutralBay.addStreet(new Street("Spruson", StreetType.Street));
        Street philipStreet = neutralBay.addStreet(new Street("Phillip", StreetType.Street));
        Street militaryRoad = neutralBay.addStreet(new Street("Military", StreetType.Road));
        mosmon.addStreet(militaryRoad);
        Street carabellaStreet = kirribilli.addStreet(new Street("Carabella", StreetType.Street));

        AddressList addressList = new AddressList();
        Address addressA = addressList.createAddress(new UnitAddress("1", "22", sprusonStreet, neutralBay, nbPostCode));
        Address addressB = addressList.createAddress(new UnitAddress("3", "60", carabellaStreet, kirribilli, kPostCode));

        try {
            persistenceService.openSession();
            persistenceService.save(australia);
            persistenceService.save(addressList);
            persistenceService.closeSession();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    public static void initialisePlainAddresses(PersistenceService persistenceService) {

        try {
            persistenceService.openSession();
            persistenceService.save(testAddressA());
            persistenceService.save(new PlainTextAddress("102/12 Glen Street, Milsons Point NSW 2060"));
            persistenceService.save(new PlainTextAddress("3/60 Carabella Street, Kirribilli NSW 2060"));

            persistenceService.closeSession();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        
    }


    public static Address testAddressA() {
        return new PlainTextAddress("1/22 Spruson Street, Neutral Bay NSW 2089");
    }

    private static final String[] sqlDataFiles = {
            "REGION",
            "REGIONMAP",
            "COUNTRY",
            "POSTCODE",
            "STATE",
            "SUBURB"
    };

    *//** Initialise all of the reference region data *//*
    public static void initialiseCountryList() {

        StringFilter filter = new NonBlankFilter();
        String[] sqlLines;
        try {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
            } catch (Exception e) {
                System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
                e.printStackTrace();
                return;
            }

            Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");

            //PersistenceSession session = persistenceService.openSession();
            //PersistenceTransaction transaction = session.currentTransaction();
            for (String file : sqlDataFiles) {

                //transaction.begin();

                URL location = ResourceTools.locateResource(file+".sql");
                if (location != null) {
                    sqlLines = FileTools.readTextFile(location, filter);
                    //persistenceService.executeBulkUpdate(sqlLines);
                    PersistenceTools.executeUpdate(connection, sqlLines);
                }

                //transaction.commit();
            }
        } catch(SQLException e) {
            LOG.error("SQL error", e);
        } catch (IOException e) {
            LOG.error("Failed to read data", e);
        } 
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

    *//** Loads a CSV file of all the patterns defined for cleansing addresses *//*
    public static void initialiseAddressSubstitutionPatterns(PersistenceService persistenceService) {
        List<Substitution> substitutions;
        int patterns = 0;
        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        substitutions = substitutionsFileReader.readCsv(ADDRESS_PATTERNS_FILE_NAME);

        try {
            persistenceService.openSession();
            for (Substitution substitution : substitutions) {
                persistenceService.save(substitution);
                patterns++;
            }
            persistenceService.closeSession();

            LOG.info("Initialised "+patterns+" substitution patterns");

        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
    }*/

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Initialise some property advertisements for testing */
    public static void initialisePropertyAdvertisements(PersistenceService persistenceService) {

        PropertyAdvertisement advertisement = new PropertyAdvertisement(PropertyAdvertisementTypes.Lease);
        advertisement.setAddress(new PlainTextAddress("1/22 Spruson Street, Neutral Bay NSW 2089"));
        advertisement.setDateListed(new Date());
        advertisement.setDescription("Sample description");
        advertisement.setPrice(new AskingPrice(new Money(300, Currency.getInstance("AUD")), PeriodTypes.Week));

        PremiseAttributeSet attributeSet = new PremiseAttributeSet(new Date());
        attributeSet.setBedrooms(3);
        attributeSet.setBathrooms(2);
        attributeSet.setType(PropertyTypes.Unit);
        attributeSet.setBuildingArea(new Area(120, UnitsOfArea.SquareMetre));
        advertisement.setAttributes(attributeSet);

        try {
            persistenceService.save(advertisement);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }
    
}
