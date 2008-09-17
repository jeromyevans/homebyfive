package com.blueskyminds.housepad.core.region;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.region.reference.RegionRef;
import com.blueskyminds.housepad.core.region.composite.RegionComposite;
import com.blueskyminds.housepad.core.region.group.RegionGroup;
import com.blueskyminds.framework.tools.FileTools;
import com.blueskyminds.framework.tools.ResourceTools;

import java.io.IOException;

/**
 * Provides a facade to access the landmine services
 *
 * A facade is used so remoting and persistence can be isolated from the web application entirely without
 * changes.
 *
 * If's intended that appropriate interceptors may be attached to these service methods
 *
 * Date Started: 23/10/2007
 * <p/>
 * History:
 */
public class MockServiceFacade implements ServiceFacade {

    public static final String REGION_COMPOSITE_DATA = "MockRegionComposite.data";
    public static final String SUBURB_DATA = "MockSuburb.data";
    public static final String POSTCODE_DATA = "MockPostCode.data";

    //@Inject
    private ServiceFacadeImpl delegate;

    public MockServiceFacade() {
    }

    public RegionComposite[] parseAddressCandidates(String addressString, String iso3CountryCode, int maxMatches) {
        try {
            return (RegionComposite[]) FileTools.serializeFromDisk(ResourceTools.locateResource(REGION_COMPOSITE_DATA));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TableModel listPremisesInSuburb(long suburbId) {
        try {
            return (TableModel) FileTools.serializeFromDisk(ResourceTools.locateResource(SUBURB_DATA));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TableModel listPremisesInPostCode(long postCodeId) {
        try {
            return (TableModel) FileTools.serializeFromDisk(ResourceTools.locateResource(POSTCODE_DATA));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a RegionRef instance for the region specified by Id
     *
     * @param regionId
     * @param type
     * @return
     */
   public RegionRef createRegionRef(long regionId, RegionTypes type) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    //    @Transactional
//    @Inject
//    public void initSampleData(EntityManager em) {
//        AddressTestTools.initialiseCountryList();
//        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
//
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");
//            AddressTestTools.initialiseSampleAusAddresses(connection);
//            PremiseTestTools.initialiseSampleAusPremises(connection);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * List the states in a country
     *
     * @param countryId
     * @return
     */
    public RegionGroup listStates(long countryId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * List the suburbs in a state
     *
     * @param stateId
     * @return
     */
    public RegionGroup listSuburbs(long stateId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RegionGroup listStreets(long suburbId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
