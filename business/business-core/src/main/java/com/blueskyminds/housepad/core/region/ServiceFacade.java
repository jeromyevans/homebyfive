package com.blueskyminds.housepad.core.region;

import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.region.reference.RegionRef;
import com.blueskyminds.housepad.core.region.composite.RegionComposite;
import com.blueskyminds.housepad.core.region.group.RegionGroup;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.wideplay.warp.persist.Transactional;

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
public interface ServiceFacade {

    RegionComposite[] parseAddressCandidates(String addressString, String iso3CountryCode, int maxMatches);

    TableModel listPremisesInSuburb(long suburbId);

    TableModel listPremisesInPostCode(long postCodeId);   

    /**
     * List the states in a country
     *
     * @param countryId
     * @return
     */
    RegionGroup listStates(long countryId);


    /**
     * List the suburbs in a state
     *
     * @param stateId
     * @return
     */    
    RegionGroup listSuburbs(long stateId);

    /**
     * List the streets in a suburb
     *
     * @param suburbId
     * @return
     */
    RegionGroup listStreets(long suburbId);
}
