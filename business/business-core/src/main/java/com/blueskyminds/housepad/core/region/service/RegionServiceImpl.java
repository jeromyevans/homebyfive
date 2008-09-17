package com.blueskyminds.housepad.core.region.service;

import com.blueskyminds.housepad.core.region.eao.CountryEAO;
import com.blueskyminds.housepad.core.region.eao.StateEAO;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.region.eao.PostCodeEAO;
import com.blueskyminds.housepad.core.region.group.RegionGroupFactory;
import com.blueskyminds.housepad.core.region.group.RegionGroup;
import com.blueskyminds.housepad.core.region.*;
import com.blueskyminds.housepad.core.region.model.*;
import com.blueskyminds.housepad.core.model.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.DomainObjectStatus;
import com.blueskyminds.framework.tools.text.StringTools;
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
    public CountryBean lookupCountry(String country) {
        return countryEAO.lookupCountry(PathHelper.buildPath(country));
    }

    /**
     * Create a new country
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the cases of an InvalidRegionException or DuplicateRegionException as
     *  no writes occur
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public CountryBean createCountry(CountryBean countryBean) throws DuplicateRegionException, InvalidRegionException {
        countryBean.populateAttributes();
        if (countryBean.isValid()) {
            CountryBean existing = countryEAO.lookupCountry(countryBean.getPath());
            if (existing == null) {
                // check if the node exists in the region graph and reference it/create it
                CountryHandle country = addressService.lookupCountry(countryBean.getAbbr());
                if (country == null) {
                    country = addressService.createCountry(countryBean.getName(), countryBean.getAbbr(), null, null);
                }
                countryBean.setCountryHandle(country);
                em.persist(countryBean);
            } else {
                throw new DuplicateRegionException(countryBean);
            }
        } else {
            throw new InvalidRegionException(countryBean);
        }
        return countryBean;
    }

    @Transactional
    public RegionGroup listCountries() {
        Set<CountryBean> countries = countryEAO.listCountries();
        return RegionGroupFactory.createCountries(countries);
    }

    @Transactional
    public RegionGroup listStatesAsGroup(String country) {
        Set<StateBean> states = listStates(country);
        return RegionGroupFactory.createStates(states);
    }

    @Transactional
    public TableModel listStatesAsTable(String country) {
        Set<StateBean> states = listStates(country);
        return StateTableFactory.createTable(states);
    }

    @Transactional
    public Set<StateBean> listStates(String country) {
        return stateEAO.listStates(PathHelper.buildPath(country));
    }

    /**
     * Create a new Status
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public StateBean createState(StateBean stateBean) throws DuplicateRegionException {
        stateBean.populateAttributes();
        StateBean existing = stateEAO.lookupState(stateBean.getPath());
        if (existing == null) {
            // check if the node exists in the region graph and reference it/create it
            StateHandle stateHandle = addressService.lookupStateByAbbr(stateBean.getAbbr(), stateBean.getCountryBean().getCountryHandle());
            if (stateHandle == null) {
                stateHandle = addressService.createState(stateBean.getName(), stateBean.getAbbr(), stateBean.getCountryBean().getCountryHandle());
            }
            stateBean.setStateHandle(stateHandle);
            em.persist(stateBean);
        } else {
            throw new DuplicateRegionException(stateBean);
        }
        return stateBean;
    }

    @Transactional
    public StateBean lookupState(String country, String state) {
        return stateEAO.lookupState(PathHelper.buildPath(country, state));
    }

    @Transactional
    public RegionGroup listSuburbsAsGroup(String country, String state) {
        Set<SuburbBean> suburbs = listSuburbs(country, state);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    @Transactional
    public TableModel listSuburbsAsTable(String country, String state) {
        Set<SuburbBean> suburbs = listSuburbs(country, state);
        return SuburbTableFactory.createTable(suburbs);
    }

    @Transactional
    public Set<SuburbBean> listSuburbs(String country, String state) {
        return suburbEAO.listSuburbs(PathHelper.buildPath(country, state));
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public SuburbBean createSuburb(SuburbBean suburbBean) throws DuplicateRegionException {
        suburbBean.populateAttributes();
        SuburbBean existing = suburbEAO.lookupSuburb(suburbBean.getPath());
        if (existing == null) {
            // check if the node exists in the region graph and reference it/create it
            SuburbHandle suburbHandle = addressService.lookupSuburb(suburbBean.getName(), suburbBean.getStateBean().getStateHandle());
            if (suburbHandle == null) {
                suburbHandle = addressService.createSuburb(suburbBean.getName(), suburbBean.getStateBean().getStateHandle());
            }
            suburbBean.setSuburbHandle(suburbHandle);
            em.persist(suburbBean);
        } else {
            throw new DuplicateRegionException(suburbBean);
        }
        return suburbBean;
    }

    public SuburbBean lookupSuburb(String country, String state, String suburb) {
        return suburbEAO.lookupSuburb(PathHelper.buildPath(country, state, suburb));
    }

    public SuburbBean lookupSuburb(SuburbHandle suburbHandle) {
        return suburbEAO.lookupSuburb(suburbHandle);
    }

    public SuburbBean lookupSuburb(String path) {
        return suburbEAO.lookupSuburb(path);
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = DuplicateRegionException.class) 
    public PostCodeBean createPostCode(PostCodeBean postCodeBean) throws DuplicateRegionException {
        postCodeBean.populateAttributes();
        PostCodeBean existing = postCodeEAO.lookupPostCode(postCodeBean.getPath());
        if (existing == null) {
            // check if the node exists in the region graph and reference it/create it
            PostCodeHandle postCodeHandle = addressService.lookupPostCode(postCodeBean.getName(), postCodeBean.getStateBean().getStateHandle());
            if (postCodeHandle == null) {
                postCodeHandle = addressService.createPostCode(postCodeBean.getName(), postCodeBean.getStateBean().getStateHandle());
            }
            postCodeBean.setPostCodeHandle(postCodeHandle);
            em.persist(postCodeBean);
        } else {
            throw new DuplicateRegionException(postCodeBean);
        }
        return postCodeBean;
    }

    @Transactional
    public Set<PostCodeBean> listPostCodes(String country, String state) {
        return postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
    }

    @Transactional
    public RegionGroup listPostCodesAsGroup(String country, String state) {
        Set<PostCodeBean> postCodes = listPostCodes(country, state);
        return RegionGroupFactory.createPostCodes(postCodes);
    }

    @Transactional
    public TableModel listPostCodesAsTable(String country, String state) {
        Set<PostCodeBean> postCodes = postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
        return PostCodeTableFactory.createTable(postCodes);
    }

    @Transactional
    public PostCodeBean lookupPostCode(String country, String state, String postCode) {
        return postCodeEAO.lookupPostCode(PathHelper.buildPath(country, state, postCode));
    }

    @Transactional
    public RegionGroup listSuburbs(String country, String state, String postCode) {
        Set<SuburbBean> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    @Transactional
    public TableModel listSuburbsAsTable(String country, String state, String postCode) {
        Set<SuburbBean> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return SuburbTableFactory.createTable(suburbs);
    }

    /**
     * Lookup a region from the given path.  It must refer to a valid region exactly, without additional suffixes.
     * The implementation takes a rough guess based on the number of sections in the path
     *
     * @param path
     * @return
     */
    public RegionBean lookupRegion(String path) {
        RegionBean region = null;
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

    public Street lookupStreet(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    public RegionBean mergeRegions(RegionBean target, RegionBean source) {

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

    public PostCodeBean persist(PostCodeBean postCodeBean) {
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

    private void deleteRegion(RegionBean regionBean) {
        if (regionBean != null) {
            RegionHandle regionHandle = regionBean.getRegionHandle();
            regionBean.setStatus(DomainObjectStatus.Deleted);
            em.persist(regionBean);
            deleteRegionHandle(regionHandle);
        }
    }

    private void deleteRegionHandle(RegionHandle regionHandle) {
        if (regionHandle != null) {
            addressService.deleteRegionById(regionHandle.getId());
        }
    }

    /**
     * Create/lookup a country from an entry in the RegionGraph
     *
     */
    @Transactional
    public CountryBean lookupOrCreateCountry(CountryHandle countryHandle) {
        CountryBean existing = countryEAO.lookupCountry(countryHandle);
        if (existing == null) {
            CountryBean countryBean =  new CountryBean(countryHandle.getName(), countryHandle.getCountry().getIso2CountryCode());
            countryBean.setCountryHandle(countryHandle);
            em.persist(countryBean);

            return countryBean;
        } else {
            return existing;
        }
    }

     /**
     * Create/lookup a State from an entry in the RegionGraph
     *
     */
    @Transactional
    public StateBean lookupOrCreateState(StateHandle stateHandle) {
        StateBean existing = stateEAO.lookupState(stateHandle);
        if (existing == null) {
            CountryBean country = lookupOrCreateCountry(stateHandle.getCountry());
            StateBean stateBean =  new StateBean(country, stateHandle.getName(),  stateHandle.getState().getAbbreviation());
            stateBean.setStateHandle(stateHandle);
            em.persist(stateBean);

            return stateBean;
        } else {
            return existing;
        }
    }

     /**
     * Create/lookup a PostCode from an entry in the RegionGraph
     *
     */
    @Transactional
    public PostCodeBean lookupOrCreatePostCode(PostCodeHandle postCodeHandle) {
        PostCodeBean existing = postCodeEAO.lookupPostCode(postCodeHandle);
        if (existing == null) {
            CountryBean country = lookupOrCreateCountry(postCodeHandle.getState().getCountry());
            StateBean state = lookupOrCreateState(postCodeHandle.getState());
            PostCodeBean postCodeBean =  new PostCodeBean(country, state, postCodeHandle.getName());
            postCodeBean.setPostCodeHandle(postCodeHandle);
            em.persist(postCodeBean);

            return postCodeBean;
        } else {
            return existing;
        }
    }

    /**
     * Create a new suburb from an entry in the RegionGraph
     *
     */
    @Transactional()
    public SuburbBean lookupOrCreateSuburb(SuburbHandle suburbHandle) {
        SuburbBean existing = suburbEAO.lookupSuburb(suburbHandle);
        if (existing == null) {
            StateHandle stateHandle = suburbHandle.getState();
            CountryBean country = lookupOrCreateCountry(stateHandle.getCountry());
            StateBean state = lookupOrCreateState(suburbHandle.getState());
            PostCodeBean postCode = lookupOrCreatePostCode(suburbHandle.getPostCode());

            SuburbBean suburb =  new SuburbBean(country, state, postCode,  suburbHandle.getName());
            suburb.setSuburbHandle(suburbHandle);
            em.persist(suburb);

            return suburb;
        } else {
            return existing;
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
