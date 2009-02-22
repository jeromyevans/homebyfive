package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.index.*;
import com.blueskyminds.homebyfive.business.region.graph.*;

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
    private CountryService countryService;
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private SuburbService suburbService;
    private StreetService streetService;
    
    public RegionServiceImpl() {
    }

    public RegionServiceImpl(EntityManager em, CountryService countryService, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService, StreetService streetService) {
        this.em = em;
        this.countryService = countryService;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
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
                region = countryService.lookup("/"+components[0]);
                break;
            case 2 :
                region = stateService.lookup(components[0], components[1]);
                break;
            case 3 :
                region = suburbService.lookup(components[0], components[1], components[2]);
                if (region == null) {
                    region = postalCodeService.lookup(components[0], components[1], components[2]);
                }
                break;
            case 4 :
                region = streetService.lookup(components[0], components[1], components[2], components[3]);
                break;
            case 5 :
                region = streetService.lookup(components[0], components[1], components[2], components[3]);
                break;
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

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Inject
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setPostalCodeService(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Inject
    public void setSuburbService(SuburbService suburbService) {
        this.suburbService = suburbService;
    }

    @Inject
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }
}
