package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.business.region.index.*;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.wideplay.warp.persist.Transactional;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public interface RegionService {

    RegionGroup listCountries();

    Country lookupCountry(String country);

    Country createCountry(Country country) throws DuplicateRegionException, InvalidRegionException;

    /**
     * Update an existing country
     * Propagates the change into the RegionGraph as well
     * @param country
     */
    Country updateCountry(Country country) throws InvalidRegionException;

    RegionGroup listStatesAsGroup(String country);

    TableModel listStatesAsTable(String country);

    Set<State> listStates(String country);

    State createState(State stateBean) throws DuplicateRegionException, InvalidRegionException;

    /**
     * Update an existing state
     * Propagates the change into the RegionGraph as well
     */
    State updateState(State state) throws InvalidRegionException;

    State lookupState(String country, String state);

    RegionGroup listSuburbsAsGroup(String country, String state);

    TableModel listSuburbsAsTable(String country, String state);

    Set<Suburb> listSuburbs(String country, String state);

    Suburb createSuburb(Suburb suburb) throws DuplicateRegionException, InvalidRegionException;
    Suburb updateSuburb(Suburb suburb) throws InvalidRegionException;

    Suburb lookupSuburb(String country, String state, String suburb);

    Suburb lookupSuburb(String path);

    PostalCode createPostCode(PostalCode postCode) throws DuplicateRegionException, InvalidRegionException;
    PostalCode updatePostCode(PostalCode postalCode) throws InvalidRegionException;
    
    Set<PostalCode> listPostCodes(String country, String state);

    RegionGroup listPostCodesAsGroup(String country, String state);

    TableModel listPostCodesAsTable(String country, String state); 

    PostalCode lookupPostCode(String country, String state, String postCode);

    RegionGroup listSuburbs(String country, String state, String postCode);

    TableModel listSuburbsAsTable(String country, String state, String postCode);

    Region lookupRegion(String path);

    /**
     * Permanently merge two regions into one.
     *
     * The target region inherits the parents of the source
     * The target region inherits the children of the source
     * The target region inherits the aliases of the source
     *
     * This merge operation cannot be undone
     *
     * @param target
     * @param source
     * @return
     */
    RegionIndex mergeRegions(RegionIndex target, RegionIndex source);

    CountryBean persist(CountryBean countryBean);
    StateBean persist(StateBean stateBean);
    PostalCodeBean persist(PostalCodeBean postCodeBean);
    SuburbBean persist(SuburbBean suburbBean);

    /**
     * Delete the suburb specified by its ID
     * @param id
     */
    public void deleteSuburb(Long id);

    /**
     * Delete the state specified by its ID
     * @param id
     */
    public void deleteState(Long id);
    /**
     * Delete the postcode specified by its ID
     * @param id
     */
    public void deletePostCode(Long id);
    /**
     * Delete the country specified by its ID
     * @param id
     */
    void deleteCountry(Long id);

}
