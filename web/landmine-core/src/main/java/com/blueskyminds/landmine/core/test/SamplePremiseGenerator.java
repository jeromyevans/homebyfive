package com.blueskyminds.landmine.core.test;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.framework.tools.FileTools;
import com.blueskyminds.framework.tools.RandomTools;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * A test-case application that generates a large number of Australian addresses for insertion into a database.
 *
 * Runs in several steps:
 *   1. reads all the data from csv
 *   2. loads into the database using hibernate
 *   3. unloads all the data into a text file
 *
 * Date Started: 29/10/2007
 *
 * <p/>
 * History:
 */
public class SamplePremiseGenerator {

    private static final String TARGET_PATH = "src/test/resources/";

    public static final String[] PREMISE_SQL_FILES = {
            "PREMISE",
            "PREMISEADDRESSMAP",
            "PREMISEATTRIBUTESET",
            "PREMISEATTRIBUTESETMAP",
            "HPPROPERTY"

    };

    private EntityManager em;

    public SamplePremiseGenerator(EntityManager em) {
        this.em = em;
    }

    private static void unloadTables(Connection connection, String[] tableNames, String targetPath) throws IOException, SQLException {
        for (String table : tableNames) {
            FileTools.writeTextFile(targetPath+table+".sql", PersistenceTools.unload(connection, table));
        }
    }

    public void generateSamplePremises() throws Exception {
        AddressTestTools.initialiseCountryList();
        LoadRegionBeanData.load();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();

        AddressDAO addressDAO = new AddressDAO(em);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        AddressService addressService = new AddressServiceImpl(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        AdvertisementCampaignDAO advertisementCampaignDAO = new AdvertisementCampaignDAO(em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, advertisementCampaignDAO);

        List<Address> addresses = addressDAO.findAll();

        for (Address address : addresses) {
            Premise premise = new Premise();
            Date dateApplied = RandomTools.randomDate(1990, 2007);
            premise.associateAddress(address, dateApplied);
            PremiseAttributeSet attributes = new PremiseAttributeSet(dateApplied);
             // First, choose a type (exclude block of units and unknown types)
            PropertyTypes type = RandomTools.randomEnum(PropertyTypes.class, PropertyTypes.Complex, PropertyTypes.Unknown);
            attributes.setType(type);
            if (!PropertyTypes.Land.equals(type)) {
                attributes.setBedrooms(RandomTools.randomInt(1,5));
                attributes.setBathrooms(RandomTools.randomInt(1,2));
            }
            premise.associateWithAttributes(attributes);

            premiseService.persist(premise);
            em.flush();
        }

        unloadTables(((Session) em.getDelegate()).connection(), PREMISE_SQL_FILES, TARGET_PATH);
    }

}
