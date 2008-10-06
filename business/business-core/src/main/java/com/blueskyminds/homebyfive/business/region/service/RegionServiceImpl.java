package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.landmine.core.model.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.region.index.*;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.*;
import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class RegionServiceImpl implements RegionService {

    private EntityManager em;
    private CountryEAO countryEAO;
    private StateEAO stateEAO;
    private SuburbEAO suburbEAO;
    private PostCodeEAO postCodeEAO;
    private AddressService addressService;

    public RegionServiceImpl() {
    }

    public RegionServiceImpl(EntityManager em, CountryEAO countryEAO, StateEAO stateEAO, SuburbEAO suburbEAO, PostCodeEAO postCodeEAO, AddressService addressService) {
        this.em = em;
        this.countryEAO = countryEAO;
        this.stateEAO = stateEAO;
        this.suburbEAO = suburbEAO;
        this.postCodeEAO = postCodeEAO;
        this.addressService = addressService;
    }

    /**
     *
     * @param country  ISO 2 digit country code
     * @return
     */
    @Transactional
    public Country lookupCountry(String country) {
        return countryEAO.lookupCountry(PathHelper.buildPath(country));
    }

    /**
     * Create a new country
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the cases of an InvalidRegionException or DuplicateRegionException as
     *  no writes occur
     * @param country
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public Country createCountry(Country country) throws DuplicateRegionException, InvalidRegionException {
        country.populateAttributes();
        if (country.isValid()) {
            Country existing = countryEAO.lookupCountry(country.getPath());
            if (existing == null) {
                country = addressService.createCountry(country.getName(), country.getAbbr(), null, null);
                em.persist(country);
            } else {
                throw new DuplicateRegionException(country);
            }
        } else {
            throw new InvalidRegionException(country);
        }
        return country;
    }

    public RegionGroup listCountries() {
        Set<Country> countries = countryEAO.listCountries();
        return RegionGroupFactory.createCountries(countries);
    }

    public RegionGroup listStatesAsGroup(String country) {
        Set<State> states = listStates(country);
        return RegionGroupFactory.createStates(states);
    }

    public TableModel listStatesAsTable(String country) {
        Set<State> states = listStates(country);
        return StateTableFactory.createTable(states);
    }

    public Set<State> listStates(String country) {
        return stateEAO.listStates(PathHelper.buildPath(country));
    }

    /**
     * Create a new Status
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public State createState(State state) throws DuplicateRegionException {
        state.populateAttributes();
        State existing = stateEAO.lookupState(state.getPath());
        if (existing == null) {
            state = addressService.createState(state.getName(), state.getAbbr(), state.getCountry());            
            em.persist(state);
        } else {
            throw new DuplicateRegionException(state);
        }
        return state;
    }

    public State lookupState(String country, String state) {
        return stateEAO.lookupState(PathHelper.buildPath(country, state));
    }

    public RegionGroup listSuburbsAsGroup(String country, String state) {
        Set<Suburb> suburbs = listSuburbs(country, state);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state) {
        Set<Suburb> suburbs = listSuburbs(country, state);
        return SuburbTableFactory.createTable(suburbs);
    }

    public Set<Suburb> listSuburbs(String country, String state) {
        return suburbEAO.listSuburbs(PathHelper.buildPath(country, state));
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param suburb
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public Suburb createSuburb(Suburb suburb) throws DuplicateRegionException {
        suburb.populateAttributes();
        Suburb existing = suburbEAO.lookupSuburb(suburb.getPath());
        if (existing == null) {
            suburb = addressService.createSuburb(suburb.getName(), suburb.getState());
            em.persist(suburb);
        } else {
            throw new DuplicateRegionException(suburb);
        }
        return suburb;
    }

    public Suburb lookupSuburb(String country, String state, String suburb) {
        return suburbEAO.lookupSuburb(PathHelper.buildPath(country, state, suburb));
    }

    public Suburb lookupSuburb(String path) {
        return suburbEAO.lookupSuburb(path);
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param postCode
     */
    @Transactional(exceptOn = DuplicateRegionException.class) 
    public PostalCode createPostCode(PostalCode postCode) throws DuplicateRegionException {
        postCode.populateAttributes();
        PostalCode existing = postCodeEAO.lookupPostCode(postCode.getPath());
        if (existing == null) {
            postCode = addressService.createPostCode(postCode.getName(), postCode.getState());
            em.persist(postCode);
        } else {
            throw new DuplicateRegionException(postCode);
        }
        return postCode;
    }

    public Set<PostalCode> listPostCodes(String country, String state) {
        return postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
    }

    public RegionGroup listPostCodesAsGroup(String country, String state) {
        Set<PostalCode> postCodes = listPostCodes(country, state);
        return RegionGroupFactory.createPostCodes(postCodes);
    }

    public TableModel listPostCodesAsTable(String country, String state) {
        Set<PostalCode> postCodes = postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
        return PostCodeTableFactory.createTable(postCodes);
    }

    public PostalCode lookupPostCode(String country, String state, String postCode) {
        return postCodeEAO.lookupPostCode(PathHelper.buildPath(country, state, postCode));
    }

    public RegionGroup listSuburbs(String country, String state, String postCode) {
        Set<Suburb> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state, String postCode) {
        Set<Suburb> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return SuburbTableFactory.createTable(suburbs);
    }

    /**
     * Lookup a region from the given path.  It must refer to a valid region exactly, without additional suffixes.
     * The implementation takes a rough guess based on the number of sections in the path
     *
     * @param path
     * @return
     */
    public Region lookupRegion(String path) {
        Region region = null;
        String components[] = StringUtils.split(path, "/", 5);
        switch (components.length) {
            case 1 :
                region = lookupCountry(components[0]);
                break;
            case 2 :
                region = lookupState(components[0], components[1]);
                break;
            case 3 :
                region = lookupSuburb(components[0], components[1], components[2]);
                if (region == null) {
                    region = lookupPostCode(components[0], components[1], components[2]);
                }
                break;
//            case 4 :
//                region = lookupStreet(components[0], components[1], components[2], components[3]);
//                break;
//            case 5 :
//                region = lookupStreet(components[0], components[1], components[2], components[3]);
//                break;
        }
        return region;
    }  

    /**
     * Permanently merge two regions into one.
     * <p/>
     * The target region inherits the parents of the source
     * The target region inherits the children of the source
     * The target region inherits the aliases of the source
     * <p/>
     * This merge operation cannot be undone
     *
     * @param target
     * @param source
     * @return
     */
    @Transactional
    public RegionIndex mergeRegions(RegionIndex target, RegionIndex source) {

        target.mergeWith(source);
        em.persist(source);
        em.persist(target);

        return target;
    }

    public CountryBean persist(CountryBean countryBean) {
        em.persist(countryBean);
        return countryBean;
    }

    public StateBean persist(StateBean stateBean) {
        em.persist(stateBean);
        return stateBean;
    }

    public PostalCodeBean persist(PostalCodeBean postCodeBean) {
        em.persist(postCodeBean);
        return postCodeBean;
    }

    public SuburbBean persist(SuburbBean suburbBean) {
        em.persist(suburbBean);
        return suburbBean;
    }

    /**
     * Delete the suburb specified by its ID
     * @param id
     */
    public void deleteSuburb(Long id) {
        deleteRegion(suburbEAO.findById(id));
    }

    /**
     * Delete the state specified by its ID
     * @param id
     */
    public void deleteState(Long id) {
        deleteRegion(stateEAO.findById(id));
    }

    /**
     * Delete the postcode specified by its ID
     * @param id
     */
    public void deletePostCode(Long id) {
        deleteRegion(postCodeEAO.findById(id));
    }

    /**
     * Delete the country specified by its ID
     * @param id
     */
    public void deleteCountry(Long id) {
        deleteRegion(countryEAO.findById(id));
    }

    private void deleteRegion(Region regionHandle) {
        if (regionHandle != null) {
            addressService.deleteRegionById(regionHandle.getId());
        }
    }

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Inject
    public void setCountryEAO(CountryEAO countryEAO) {
        this.countryEAO = countryEAO;
    }

    @Inject
    public void setStateEAO(StateEAO stateEAO) {
        this.stateEAO = stateEAO;
    }

    @Inject
    public void setSuburbEAO(SuburbEAO suburbEAO) {
        this.suburbEAO = suburbEAO;
    }

    @Inject
    public void setPostCodeEAO(PostCodeEAO postCodeEAO) {
        this.postCodeEAO = postCodeEAO;
    }

    @Inject
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }
}
