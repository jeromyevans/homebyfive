package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.SuburbTableFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.address.patterns.SuburbPatternMatcher;
import com.blueskyminds.homebyfive.business.address.patterns.SuburbPatternMatcherFactory;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherException;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.google.inject.Inject;
import com.sun.jndi.toolkit.url.GenericURLContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SuburbServiceImpl extends CommonRegionServices<Suburb> implements SuburbService {

    private static final Log LOG = LogFactory.getLog(SuburbServiceImpl.class);
    
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private SuburbPatternMatcherFactory suburbPatternMatcherFactory;
    
    private transient Map<State, SuburbPatternMatcher> suburbMatcherCache;

    /** Cache of recently used suburb names */
    private transient Map<String, Suburb> suburbNameCache;


    public SuburbServiceImpl(EntityManager em, TagService tagService, StateService stateService, SuburbEAO regionDAO) {
        super(em, regionDAO, tagService);
        this.stateService = stateService;
        init();
    }

    public SuburbServiceImpl() {
        init();
    }

    private void init() {
        suburbMatcherCache = new HashMap<State, SuburbPatternMatcher>();
        suburbNameCache = new HashMap<String, Suburb>();
    }

    /** List the suburbs in the specified state */
    public Set<Suburb> listSuburbs(State state) {
        return regionDAO.list(state.getPath());
    }

    public RegionGroup list(String parentPath) {
        Set<Suburb> suburbs = regionDAO.list(parentPath);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public RegionGroup list(String country, String state) {
        Set<Suburb> suburbs = regionDAO.list(PathHelper.buildPath(country, state));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state) {
        Set<Suburb> suburbs = regionDAO.list(PathHelper.buildPath(country, state));
        return SuburbTableFactory.createTable(suburbs);
    }

    public RegionGroup listSuburbsAsGroup(String country, String state) {
        Set<Suburb> suburbs = listSuburbs(country, state);
        return RegionGroupFactory.createSuburbs(suburbs);
    }
     
    public Set<Suburb> listSuburbs(String country, String state) {
        return regionDAO.list(PathHelper.buildPath(country, state));
    }
    
    public RegionGroup listSuburbs(String country, String state, String postCode) {
        Set<Suburb> suburbs = ((SuburbEAO) regionDAO).listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state, String postCode) {
        Set<Suburb> suburbs = ((SuburbEAO) regionDAO).listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return SuburbTableFactory.createTable(suburbs);
    }

    public Set<Suburb> listSuburbs(Country country) {
        return ((SuburbEAO) regionDAO).listSuburbsInCountry(country.getPath());        
    }

    /**
     * Find a suburb in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> find(String name, String countryCode) {

        Set<Suburb> suburbs = ((SuburbEAO) regionDAO).listSuburbsInCountry(PathHelper.buildPath(countryCode));
        return LevensteinDistanceTools.matchName(name, suburbs);
    }

    /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> find(String name, List<State> states) {

        List<Suburb> suburbs = new LinkedList<Suburb>();
        List<State> statesNotMatched = new LinkedList<State>();

        // find using the cache first
        for (State state : states) {
            Suburb suburbHandle = suburbNameCache.get(state.getName()+":"+name);
            if (suburbHandle != null) {
                if (!suburbHandle.isInvalid()) {  // when it's not null but invalid we should discard it as we know not to search
                    suburbs.add(suburbHandle);
                }
            } else {
                statesNotMatched.add(state);
            }
        }

        if (statesNotMatched.size() > 0) {
            List<State> statesStillNotMatched = new LinkedList<State>();

            // find using exact match
            for (State state : statesNotMatched) {

                Suburb suburbHandle;

                if (LOG.isInfoEnabled()) {
                    LOG.info("Finding suburb '"+name+"' using exact pattern matching in "+state.getName());
                }
                suburbHandle = regionDAO.lookup(PathHelper.generatePath(state.getPath(), name));
                if (suburbHandle != null) {
                    suburbs.add(suburbHandle);
                } else {
                    statesStillNotMatched.add(state);
                }
            }

            // find using fuzzy matching
            if (statesStillNotMatched.size() > 0) {
                if (suburbPatternMatcherFactory != null) {
                    for (State state : statesStillNotMatched) {
                        try {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Finding suburb '"+name+"' using fuzzy pattern matching in "+state.getName());
                            }
                            SuburbPatternMatcher patternMatcher = selectSuburbMatcher(state);
                            Suburb suburb = patternMatcher.extractBest(name);
                            if (suburb != null) {
                                suburbs.add(suburb);
                            }
                        } catch (PatternMatcherException e) {
                            LOG.error("Could not initialize SuburbPatternMatcher", e);
                        }
                    }
                } else {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Finding suburb '"+name+"' using fuzzy word matching across all states");
                    }

                    Set<Suburb> suburbSet = new HashSet<Suburb>();
                    for (State state : statesNotMatched) {
                        suburbSet.addAll(regionDAO.list(state.getPath()));
                    }

                    suburbs.addAll(LevensteinDistanceTools.matchName(name, suburbSet));
                }
            }

            if (suburbs.size() > 0) {
                // put suburb into the cache
                for (State state : states) {
                    for (Suburb suburb : suburbs) {
                        if (suburb.getState().equals(state)) {
                            suburbNameCache.put(state.getName()+":"+name, suburb);
                        }
                    }
                }
            } else {
                // store invalid suburb entries in the cache so we don't look again
                for (State state : states) {
                    suburbNameCache.put(state.getName()+":"+name, Suburb.INVALID);
                }
            }
        }

        return suburbs;
    }

    /**
     * Select or create a SuburbPatternMatcher for the specified state.  They'll be reused if they can be
     * @param state
     * @return
     * @throws PatternMatcherInitialisationException
     */
    private SuburbPatternMatcher selectSuburbMatcher(State state) throws PatternMatcherInitialisationException {
        SuburbPatternMatcher patternMatcher = suburbMatcherCache.get(state);
        if (patternMatcher == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("SuburbMatcher for state "+state.getName()+" is not cached");
            }
            patternMatcher = suburbPatternMatcherFactory.create(state);
            suburbMatcherCache.put(state, patternMatcher);
        } else {
            patternMatcher.reset();
        }
        return patternMatcher;
    }

    /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> find(String name, State state) {
        LinkedList<State> states = new LinkedList<State>();
        states.add(state);
        return find(name, states);
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param suburb
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public Suburb create(Suburb suburb) throws DuplicateRegionException, InvalidRegionException {
        suburb.populateAttributes();
        Suburb existing = regionDAO.lookup(suburb.getPath());
        if (existing == null) {

            State state = suburb.getState();
            if (state == null) {
                // see if the parent path references a state
                if (StringUtils.isNotBlank(suburb.getParentPath())) {
                    state = stateService.lookup(suburb.getParentPath());
                    if (state != null) {
                        state.addSuburb(suburb);
                        suburb.setState(state);
                    }
                }
            } else {
                state.addSuburb(suburb);
            }

            if (state != null) {
                em.persist(state);
            } else {
                throw new InvalidRegionException("Invalid parent region (state is null)", suburb);
            }

            // optional postalcode
            PostalCode postalCode = suburb.getPostalCode();
            if (postalCode == null) {
                // see if the postalcode path references a postalcode
                if (StringUtils.isNotBlank(suburb.getPostalCodePath())) {
                    postalCode = postalCodeService.lookup(suburb.getPostalCodePath());
                    if (postalCode != null) {
                        postalCode.addSuburb(suburb);                        
                    }
                }
            } else {
                postalCode.addSuburb(suburb);
            }

            if (postalCode != null) {
                em.persist(state);
            }
        } else {
            throw new DuplicateRegionException(suburb);
        }
        return suburb;
    }
  

    public Suburb lookup(String country, String state, String suburb) {
        return regionDAO.lookup(PathHelper.buildPath(country, state, suburb));
    }

    public Suburb lookup(String path) {
        return regionDAO.lookup(path);
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setRegionDAO(SuburbEAO regionDAO) {
        this.regionDAO = regionDAO;
    }

    @Inject(optional = true)
    public void setSuburbPatternMatcherFactory(SuburbPatternMatcherFactory suburbPatternMatcherFactory) {
        this.suburbPatternMatcherFactory = suburbPatternMatcherFactory;
    }

    @Inject
    public void setPostalCodeService(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }
}
