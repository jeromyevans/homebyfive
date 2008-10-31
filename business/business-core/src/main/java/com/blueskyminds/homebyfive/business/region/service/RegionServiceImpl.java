package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
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
//                RegionFactory regionFactory = new RegionFactory();
//                country = regionFactory.createCountry(country.getName(), country.getAbbr(), null, null);
//                //country = addressService.createCountry(country.getName(), country.getAbbr(), null, null);
                em.persist(country);
            } else {
                throw new DuplicateRegionException(country);
            }
        } else {
            throw new InvalidRegionException(country);
        }
        return country;
    }

    /**
     * Update an existing country
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the cases of an InvalidRegionException or DuplicateRegionException as
     *  no writes occur
     * @param country
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public Country updateCountry(Country country) throws InvalidRegionException {
        country.populateAttributes();
        if (country.isValid()) {
            Country existing = countryEAO.lookupCountry(country.getPath());
            if (existing != null) {
                existing.mergeWith(country);
                em.persist(country);
            } else {
                throw new InvalidRegionException(country);
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
     * Create a new State
     * Propagates the change into the RegionGraph as well
     *
     * If the state is not bound to a country, the parentPath will be used to lookup the country
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public State createState(State state) throws DuplicateRegionException, InvalidRegionException {
        state.populateAttributes();
        State existing = stateEAO.lookupState(state.getPath());
        if (existing == null) {
            Country country = state.getCountry();
            if (country == null) {
                // see if the parent path references a country
                if (StringUtils.isNotBlank(state.getParentPath())) {
                    country = lookupCountry(state.getParentPath());
                    if (country != null) {
                        state.setCountry(country);
                    }
                }
            } 
            
//            state = addressService.createState(state.getName(), state.getAbbr(), state.getCountry());
            if (country != null) {
                em.persist(state);
                em.persist(country);
            } else {
                throw new InvalidRegionException("Invalid parent region (country)", state);
            }
        } else {
            throw new DuplicateRegionException(state);
        }
        return state;
    }

    /**
     * Update an existing state
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public State updateState(State state) throws InvalidRegionException {
        state.populateAttributes();
        State existing = stateEAO.lookupState(state.getPath());
        if (existing != null) {
            existing.mergeWith(state);
            em.persist(state);
        } else {
            throw new InvalidRegionException(state);
        }
        return state;
    }

    public State lookupState(String country, String state) {
        return lookupState(PathHelper.buildPath(country, state));
    }

    public State lookupState(String statePath) {
        return stateEAO.lookupState(statePath);
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
    public Suburb createSuburb(Suburb suburb) throws DuplicateRegionException, InvalidRegionException {
        suburb.populateAttributes();
        Suburb existing = suburbEAO.lookupSuburb(suburb.getPath());
        if (existing == null) {

            State state = suburb.getState();
            if (state == null) {
                // see if the parent path references a state
                if (StringUtils.isNotBlank(suburb.getParentPath())) {
                    state = lookupState(suburb.getParentPath());
                    if (state != null) {
                        suburb.setState(state);
                    }
                }
            }

            if (state != null) {
                em.persist(state);
                em.persist(suburb);
            } else {
                throw new InvalidRegionException("Invalid parent region (state)", suburb);
            }

//            suburb = addressService.createSuburb(suburb.getName(), suburb.getState());
        } else {
            throw new DuplicateRegionException(suburb);
        }
        return suburb;
    }

    /**
     * Update an existing suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public Suburb updateSuburb(Suburb suburb) throws InvalidRegionException {
        suburb.populateAttributes();
        Suburb existing = suburbEAO.lookupSuburb(suburb.getPath());
        if (existing != null) {
            existing.mergeWith(suburb);
            em.persist(suburb);
        } else {
            throw new InvalidRegionException(suburb);
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
     * Create a new PostalCode
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param postalCode
     */
    @Transactional(exceptOn = DuplicateRegionException.class) 
    public PostalCode createPostCode(PostalCode postalCode) throws DuplicateRegionException, InvalidRegionException {
        postalCode.populateAttributes();
        PostalCode existing = postCodeEAO.lookupPostCode(postalCode.getPath());
        if (existing == null) {

            State state = postalCode.getState();
            if (state == null) {
                // see if the parent path references a country
                if (StringUtils.isNotBlank(postalCode.getParentPath())) {
                    state = lookupState(postalCode.getParentPath());
                    if (state != null) {
                        postalCode.setState(state);
                    }
                }
            }
//            postalCode = addressService.createPostCode(postalCode.getName(), postalCode.getState());
            if (state != null) {
                em.persist(state);
                em.persist(postalCode);
            } else {
                throw new InvalidRegionException("Invalid parent", postalCode);
            }
        } else {
            throw new DuplicateRegionException(postalCode);
        }
        return postalCode;
    }

    /**
    * Update an existing postalcode
    * Propagates the change into the RegionGraph as well
    *
    * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
    */
   @Transactional(exceptOn = {InvalidRegionException.class})
   public PostalCode updatePostCode(PostalCode postalCode) throws InvalidRegionException {
       postalCode.populateAttributes();
       PostalCode existing = postCodeEAO.lookupPostCode(postalCode.getPath());
       if (existing != null) {
           existing.mergeWith(postalCode);
           em.persist(postalCode);
       } else {
           throw new InvalidRegionException(postalCode);
       }
       return postalCode;
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
