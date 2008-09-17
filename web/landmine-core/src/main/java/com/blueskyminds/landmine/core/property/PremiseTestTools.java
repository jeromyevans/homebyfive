package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.address.dao.SuburbDAO;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.tools.RandomTools;
import com.blueskyminds.framework.tools.ResourceTools;
import com.blueskyminds.framework.tools.csv.CsvOptions;
import com.blueskyminds.framework.tools.csv.CsvTextReader;
import com.blueskyminds.landmine.core.property.attribute.PropertyTypeWrapper;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import com.blueskyminds.landmine.core.test.SamplePremiseGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Connection;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Methods to assist unit tests related to Premise
 *
 * Date Started: 29/10/2007
 * <p/>
 * History:
 */
public class PremiseTestTools extends TestTools {

    private static final Log LOG = LogFactory.getLog(PremiseTestTools.class);

     /** Initialise a bunch of sample premises for testing
     * hsqldb:mem:mem only
      * Premise data is loaded from a csv file.  There's typical one for each sample address 
      * @see SamplePremiseGenerator
      * */
    public static void initialiseSampleAusPremises() {
        initialiseSampleAusPremises(getConnection());
    }

    /**
     * Initialise a bunch of sample premises for testing
     * This method uses a direct JDBC connection
     **/
    public static void initialiseSampleAusPremises(Connection connection) {
        applySQLFiles(connection, SamplePremiseGenerator.PREMISE_SQL_FILES);
    }

    /**
     * Initialise a bunch of sample premises for testing
     * This method uses a direct JDBC connection
     **/
    public static void initialiseSampleAusPremisesWithAdvertisements(PropertyAdvertisementTypes type, int firstYear, int lastYear, EntityManager em) {
        applySQLFiles(getConnection(), SamplePremiseGenerator.PREMISE_SQL_FILES);
        initialiseRandomAdsForPremises(type, firstYear, lastYear, em);
    }


    /**
     * Creates a Premise for a random address.
     * The only attributes of the property are that it has a type, and a certain number of bedrooms and bathrooms
     * The Premise has not been persisted.
     *
     * Set Suburb to null to use a random suburb
     *  */
    public static Premise createRandomPremise(SuburbHandle suburb, EntityManager em) {

        Premise property ;
        Address address = null;

        // First, choose a type (exclude block of units and unknown types)
        PropertyTypes type = RandomTools.randomEnum(PropertyTypes.class, PropertyTypes.Complex, PropertyTypes.Unknown);

        // create a random address
        Street street = randomStreet();
        String streetNumber = Integer.toString(RandomTools.randomInt(1, 300));
        if (suburb == null) {
            suburb = randomSuburb(em);
        }

        if (PropertyTypeWrapper.isTypeOfUnit(type)) {
            String unitNumber = Integer.toString(RandomTools.randomInt(1, 20));
            address = new UnitAddress(unitNumber, streetNumber, street, suburb, suburb.getPostCode());
        } else {
            address = new StreetAddress(streetNumber, street, suburb, suburb.getPostCode());
        }

        property = new Premise();
        Date dateApplied = RandomTools.randomDate(1990, 2006);
        property.associateAddress(address, dateApplied);
        PremiseAttributeSet attributes = new PremiseAttributeSet(dateApplied);
        attributes.setType(type);
        attributes.setBedrooms(RandomTools.randomInt(1,5));
        attributes.setBathrooms(RandomTools.randomInt(1,2));
        property.associateWithAttributes(attributes);

        return property;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Generate a random advertisement for the specified property
     * @param type - type of advertisement, or null for random
     **/
    public static PropertyAdvertisement createRandomPropertyAdvertisement(Premise premise, PropertyAdvertisementTypes type, int firstYear, int lastYear) {
        return createRandomPropertyAdvertisement(premise, type, null, firstYear, lastYear);
    }
    /**
     * Generate a random advertisement for the specified property
     * @param type              type of advertisement, or null for random
     * @param propertyType      property type in advertisement, or null to inherit from premise
     **/
    public static PropertyAdvertisement createRandomPropertyAdvertisement(Premise premise, PropertyAdvertisementTypes type, PropertyTypes propertyType, int firstYear, int lastYear) {
        if (type == null) {
            type = RandomTools.randomEnum(PropertyAdvertisementTypes.class);
        }
        PropertyAdvertisement advertisement = new PropertyAdvertisement(type);

        Date dateListed = RandomTools.randomDate(firstYear, lastYear);
        advertisement.setAddress(premise.getAddress());
        advertisement.setDateListed(dateListed);
        advertisement.setDescription("Sample description");

        switch (type) {
            case Auction:
                break;
            case Lease:
                advertisement.setPrice(new AskingPrice(new Money(RandomTools.randomDouble(60, 1000), Currency.getInstance("AUD")), PeriodTypes.Week));
                break;
            case PrivateTreaty:
                advertisement.setPrice(new AskingPrice(new Money(RandomTools.randomDouble(50000, 1000000), Currency.getInstance("AUD")), PeriodTypes.OnceOff));
                break;
            case Unknown:
                break;
        }

        PremiseAttributeSet attributeSet = new PremiseAttributeSet(dateListed);
        attributeSet.setBedrooms(premise.getBedrooms());
        attributeSet.setBathrooms(premise.getBathrooms());
        attributeSet.setType(propertyType != null ? propertyType : premise.getType());
        attributeSet.setBuildingArea(new Area(RandomTools.randomDouble(75, 250), UnitsOfArea.SquareMetre));
        advertisement.setAttributes(attributeSet);

        return advertisement;
    }


    // ------------------------------------------------------------------------------------------------------

    private static final String ADVERTISMENTS_FILE_NAME = "sampleAdvertisementsSetA.csv";
    private static final String NULL_VALUE = "\\N";

    /** Loads a CSV file of sample advertisements for processing */
    public static void initialiseExampleAdvertisements(EntityManager em) {
        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        try {
            CsvTextReader csvReader = new CsvTextReader(ResourceTools.openStream(ADVERTISMENTS_FILE_NAME), csvOptions);
            int advertisements = 0;

            while (csvReader.read()) {
                if (csvReader.isNonBlank()) {
                    String[] values = csvReader.getAsStrings();

                    if (values.length > 0) {

                        // convert '\N' to null
                        for (int index = 0; index < values.length; index++) {
                            if (NULL_VALUE.equals(values[index])) {
                                values[index] = null;
                            }
                        }

                        try {
                            int bedrooms = -1;
                            int bathrooms = -1;

                            String address = values[0];
                            String description = values[1];
                            String typeName = values[2];
                            String bedroomsString = values[3];
                            String bathroomsString = values[4];
                            String landAreaString = values[5];
                            String priceString= values[6];


                            try {
                                bedrooms = Integer.parseInt(bedroomsString);
                            } catch (NumberFormatException e) {
                                // couldn't get a valid number
                            }

                            try {
                                bathrooms = Integer.parseInt(bathroomsString);
                            } catch (NumberFormatException e) {
                                // couldn't get a valid number
                            }

                            PropertyAdvertisement advertisement = new PropertyAdvertisement(PropertyAdvertisementTypes.Unknown);
                            if (address != null) {
                                advertisement.setAddress(new PlainTextAddress(address));
                            }
                            advertisement.setDateListed(new Date());
                            advertisement.setDescription(description);
                            //advertisement.setPrice(new AskingPrice(new Money(300, Currency.getInstance("AUD")), PeriodTypes.Week));

                            PremiseAttributeSet attributeSet = new PremiseAttributeSet(new Date());
                            attributeSet.setBedrooms(bedrooms);
                            attributeSet.setBathrooms(bathrooms);
                            attributeSet.setType(PropertyTypes.Unknown);
                            attributeSet.setBuildingArea(new Area(120, UnitsOfArea.SquareMetre));
                            advertisement.setAttributes(attributeSet);

                            em.persist(advertisement);
                            advertisements++;
                        }
                        catch (ArrayIndexOutOfBoundsException e2) {
                            LOG.warn("Couldn't parse CSV line in "+ADVERTISMENTS_FILE_NAME+" - skipped line = '"+ StringUtils.join(values,",")+"'");
                        }
                    }
                }
            }

            em.flush();

            LOG.info("Initialised "+advertisements+" example advertisements");
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static List<Street> sampleStreets;

    /** Loads a CSV file of street names into a static variable for use in the randomStreet method */
    private static void loadSampleStreets() {
        sampleStreets = new LinkedList<Street>();

        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        CsvTextReader csvReader = null;
        try {
            csvReader = new CsvTextReader(ResourceTools.openStream("streetNames.csv"), csvOptions);

            while (csvReader.read()) {
                if (csvReader.isNonBlank()) {
                    String[] values = csvReader.getAsStrings();
                    String streetName = values[0];
                    String streetTypeName = values[1];
                    StreetType streetType = StreetType.valueOf(streetTypeName);

                    sampleStreets.add(new Street(streetName, streetType, StreetSection.NA));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    // ------------------------------------------------------------------------------------------------------

    /** Loads a CSV file of of sample street names, caches it, and then returns one of the streets in the sample */
    public static Street randomStreet() {
        Street street;
        if (sampleStreets == null) {
            loadSampleStreets();
        }
        return sampleStreets.get(RandomTools.randomInt(0, sampleStreets.size()-1));
    }

     public static SuburbHandle randomSuburb(EntityManager em) {
        return new SuburbDAO(em).findRandom();
    }

    /**
     * Create advertisements of the specified type for all properties
     **/
    public static void initialiseRandomAdsForPremises(PropertyAdvertisementTypes type, int firstYear, int lastYear, EntityManager em) {
        initialiseRandomAdsForPremises(type, null, firstYear, lastYear, em);
    }
    /**
     * Create advertisements of the specified type for all properties
     **/
    public static void initialiseRandomAdsForPremises(PropertyAdvertisementTypes type, PropertyTypes propertyType, int firstYear, int lastYear, EntityManager em) {

        PropertyAdvertisement advertisement;
        PropertyAdvertisement secondAdvertisement;
        AskingPrice price;
        int properties = 0;
        int withAds = 0;
        int withTwoAds = 0;

        List<Premise> propertyList = new PremiseEAO(em).findAll();
        properties = 0;
        for (Premise premise : propertyList) {

            if (RandomTools.randomInt(0, 9) == 0) {
                // one in ten properties don't get an advertisement
            } else {
                advertisement = PremiseTestTools.createRandomPropertyAdvertisement(premise, type, propertyType, firstYear, lastYear);
                premise.associateWithAttributes(advertisement.getAttributes());
                advertisement.associateWithPremise(premise);
                em.persist(advertisement);
                withAds++;
                if (RandomTools.randomInt(0, 4) == 0) {
                    // one in five have a second advertisement

                    // create another advertisement for this property at a lower price and slightly in the future
                    secondAdvertisement = advertisement.duplicate();
                    // override values
                    secondAdvertisement.setDateListed(DateTools.addTimespan(advertisement.getDateListed(), new Interval(1, PeriodTypes.Fortnight)));
                    price = advertisement.getPrice();
                    if (price != null) {
                        secondAdvertisement.setPrice(price.adjustedPrice(1.1));
                    } else {
                        secondAdvertisement.setPrice(null);
                    }

                    premise.associateWithAttributes(secondAdvertisement.getAttributes());
                    secondAdvertisement.associateWithPremise(premise);
                    em.persist(secondAdvertisement);
                    //gateway.save(premise);
                    withTwoAds++;
                }
            }
            em.persist(premise);
            properties++;
          //  transaction.commit();
        }

        LOG.info("Updated "+properties+" properties, "+withAds+" with ads, "+withTwoAds+" with two ads");

    }
}
