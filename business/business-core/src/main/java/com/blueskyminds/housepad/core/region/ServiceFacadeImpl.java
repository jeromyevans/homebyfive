package com.blueskyminds.housepad.core.region;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.framework.tools.FileTools;
import com.blueskyminds.housepad.core.model.PremiseSummaryFactory;
import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.region.reference.RegionRef;
import com.blueskyminds.housepad.core.region.reference.RegionRefFactory;
import com.blueskyminds.housepad.core.region.composite.RegionComposite;
import com.blueskyminds.housepad.core.region.composite.RegionCompositeFactory;
import com.blueskyminds.housepad.core.region.group.RegionGroupFactory;
import com.blueskyminds.housepad.core.region.group.RegionGroup;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class ServiceFacadeImpl implements ServiceFacade {

    private static final Log LOG = LogFactory.getLog(ServiceFacadeImpl.class);

    private AddressService addressService;
    private RegionCompositeFactory regionCompositeFactory;
    private PremiseSummaryFactory premiseSummaryFactory;
    private PremiseService premiseService;

    public ServiceFacadeImpl() {
    }

    @Transactional
    public RegionComposite[] parseAddressCandidates(String addressString, String iso3CountryCode, int maxMatches) {
        List<RegionComposite> results = new LinkedList<RegionComposite>();
        try {
            List<Address> addresses = addressService.parseAddressCandidates(addressString, iso3CountryCode, maxMatches);
            for (Address address : addresses) {
                results.add(regionCompositeFactory.create(address));
            }
        } catch (AddressProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (results.size() > 0) {
            RegionComposite[] array = new RegionComposite[results.size()];
            RegionComposite[] regionComposites = results.toArray(array);

            try {
                FileTools.serializeToDisk(regionComposites, MockServiceFacade.REGION_COMPOSITE_DATA);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return regionComposites;
        } else {
            return null;
        }
    }

    @Transactional
    public TableModel listPremisesInSuburb(long suburbId) {
        SuburbHandle suburbHandle = addressService.getSuburbById(suburbId);
        TableModel tableModel = premiseSummaryFactory.createTable(premiseService.listPremises(suburbHandle));
        try {
            FileTools.serializeToDisk(tableModel, MockServiceFacade.SUBURB_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableModel;
    }

    @Transactional
    public TableModel listPremisesInPostCode(long postCodeId) {
        PostCodeHandle postCodeHandle = addressService.getPostCodeById(postCodeId);
        TableModel tableModel = premiseSummaryFactory.createTable(premiseService.listPremises(postCodeHandle));
        try {
            FileTools.serializeToDisk(tableModel, MockServiceFacade.POSTCODE_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableModel;
    }

    /**
     * List the states in a country
     *
     * @param countryId
     * @return
     */
    @Transactional
    public RegionGroup listStates(long countryId) {
        CountryHandle country = addressService.getCountryById(countryId);
        Set<StateHandle> states = addressService.listStates(country);
        RegionGroupFactory regionGroupFactory = new RegionGroupFactory();
        return regionGroupFactory.createStates(country, states);
    }


    /**
     * List the suburbs in a state
     *
     * @param stateId
     * @return
     */
    @Transactional
    public RegionGroup listSuburbs(long stateId) {
        StateHandle state = addressService.getStateById(stateId);
        Set<SuburbHandle> suburbs = addressService.listSuburbs(state);
        RegionGroupFactory regionGroupFactory = new RegionGroupFactory();
        return regionGroupFactory.createSuburbs(state, suburbs);
    }

    public RegionGroup listStreets(long suburbId) {
        LOG.warn("listStreets(suburb) is not implemented");
        return null;  
    }

    @Inject
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Transactional
    @Inject
    public void initSampleData(EntityManager em) {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");
            AddressTestTools.initialiseSampleAusAddresses(connection);
            PremiseTestTools.initialiseSampleAusPremises(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    @Inject
    public void setRegionCompositeFactory(RegionCompositeFactory regionCompositeFactory) {
        this.regionCompositeFactory = regionCompositeFactory;
    }

    @Inject
    public void setPremiseService(PremiseService premiseService) {
        this.premiseService = premiseService;
    }

    @Inject
    public void setPremiseSummaryFactory(PremiseSummaryFactory premiseSummaryFactory) {
        this.premiseSummaryFactory = premiseSummaryFactory;
    }
}
