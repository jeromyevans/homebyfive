package com.blueskyminds.housepad.core.region.service;

import com.blueskyminds.housepad.core.region.group.RegionGroup;
import com.blueskyminds.housepad.core.region.model.*;
import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.address.Street;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public interface RegionService {

    RegionGroup listCountries();

    CountryBean lookupCountry(String country);

    CountryBean createCountry(CountryBean countryBean) throws DuplicateRegionException, InvalidRegionException;

    RegionGroup listStatesAsGroup(String country);

    TableModel listStatesAsTable(String country);

    Set<StateBean> listStates(String country);

    StateBean createState(StateBean stateBean) throws DuplicateRegionException;

    StateBean lookupState(String country, String state);

    RegionGroup listSuburbsAsGroup(String country, String state);

    TableModel listSuburbsAsTable(String country, String state);

    Set<SuburbBean> listSuburbs(String country, String state);

    SuburbBean createSuburb(SuburbBean suburbBean) throws DuplicateRegionException;

    SuburbBean lookupSuburb(String country, String state, String suburb);

    SuburbBean lookupSuburb(SuburbHandle suburbHandle);

    SuburbBean lookupSuburb(String path);

    PostCodeBean createPostCode(PostCodeBean postCodeBean) throws DuplicateRegionException;

    Set<PostCodeBean> listPostCodes(String country, String state);

    RegionGroup listPostCodesAsGroup(String country, String state);

    TableModel listPostCodesAsTable(String country, String state); 

    PostCodeBean lookupPostCode(String country, String state, String postCode);

    RegionGroup listSuburbs(String country, String state, String postCode);

    TableModel listSuburbsAsTable(String country, String state, String postCode);

    RegionBean lookupRegion(String path);

    Street lookupStreet(String path);

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
    RegionBean mergeRegions(RegionBean target, RegionBean source);

    CountryBean persist(CountryBean countryBean);
    StateBean persist(StateBean stateBean);
    PostCodeBean persist(PostCodeBean postCodeBean);
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

     /**
     * Create/lookup a Country from an entry in the RegionGraph
     *
     */
    CountryBean lookupOrCreateCountry(CountryHandle countryHandle);

     /**
     * Create/lookup a State from an entry in the RegionGraph
     *
     */
    StateBean lookupOrCreateState(StateHandle stateHandle);

     /**
     * Create/lookup a PostCode from an entry in the RegionGraph
     *
     */
    PostCodeBean lookupOrCreatePostCode(PostCodeHandle postCodeHandle);
    /**
     * Create/lookup a new suburb from an entry in the RegionGraph
     *
     */
    SuburbBean lookupOrCreateSuburb(SuburbHandle suburbHandle);

}