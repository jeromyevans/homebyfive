package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.enterprise.region.index.*;
import com.blueskyminds.enterprise.region.graph.*;
import com.blueskyminds.enterprise.region.index.RegionIndex;

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

    RegionGroup listStatesAsGroup(String country);

    TableModel listStatesAsTable(String country);

    Set<State> listStates(String country);

    State createState(State stateBean) throws DuplicateRegionException;

    State lookupState(String country, String state);

    RegionGroup listSuburbsAsGroup(String country, String state);

    TableModel listSuburbsAsTable(String country, String state);

    Set<Suburb> listSuburbs(String country, String state);

    Suburb createSuburb(Suburb suburb) throws DuplicateRegionException;

    Suburb lookupSuburb(String country, String state, String suburb);

    Suburb lookupSuburb(String path);

    PostalCode createPostCode(PostalCode postCode) throws DuplicateRegionException;

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
