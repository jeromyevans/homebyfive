package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.enterprise.region.index.*;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.index.RegionBean;

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

    SuburbBean lookupSuburb(Suburb suburbHandle);

    SuburbBean lookupSuburb(String path);

    PostCodeBean createPostCode(PostCodeBean postCodeBean) throws DuplicateRegionException;

    Set<PostCodeBean> listPostCodes(String country, String state);

    RegionGroup listPostCodesAsGroup(String country, String state);

    TableModel listPostCodesAsTable(String country, String state); 

    PostCodeBean lookupPostCode(String country, String state, String postCode);

    RegionGroup listSuburbs(String country, String state, String postCode);

    TableModel listSuburbsAsTable(String country, String state, String postCode);

    RegionBean lookupRegion(String path);

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
    CountryBean lookupOrCreateCountry(Country countryHandle);

     /**
     * Create/lookup a State from an entry in the RegionGraph
     *
     */
    StateBean lookupOrCreateState(State stateHandle);

     /**
     * Create/lookup a PostCode from an entry in the RegionGraph
     *
     */
    PostCodeBean lookupOrCreatePostCode(PostalCode postCodeHandle);
    /**
     * Create/lookup a new suburb from an entry in the RegionGraph
     *
     */
    SuburbBean lookupOrCreateSuburb(Suburb suburbHandle);

}
