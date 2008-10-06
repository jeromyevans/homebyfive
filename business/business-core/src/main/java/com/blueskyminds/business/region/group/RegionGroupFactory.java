package com.blueskyminds.business.region.group;

import com.blueskyminds.business.region.graph.*;
import com.blueskyminds.business.region.index.RegionIndex;
import com.blueskyminds.business.region.index.*;
import com.blueskyminds.business.region.composite.RegionCompositeFactory;
import com.blueskyminds.business.region.reference.RegionRefFactory;
import com.blueskyminds.business.region.RegionTypes;
//import com.blueskyminds.landmine.core.property.Premise;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Creates RegionGroups from various sources
 *
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class RegionGroupFactory {

    /**
     * Create a RegionGroup for a collection of countries.
     * */
    public static RegionGroup createCountries(Collection<Country> countries) {
        RegionGroup regionGroup = new RegionGroup();
        for (Country country : countries) {
            regionGroup.add(RegionCompositeFactory.createCountry(country));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single country
     * */
    public static RegionGroup createCountry(Country countryBean) {
        ArrayList<Country> list = new ArrayList<Country>();
        list.add(countryBean);
        return createCountries(list);
    }

    /**
     * Create a RegionGroup for a collection of suburbs.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createSuburbs(State state, Collection<? extends Suburb> suburbs) {
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (Suburb suburb : suburbs) {
            regionGroup.add(regionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of states.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createStates(Country country, Collection<State> states) {
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (State stateHandle : states) {
            regionGroup.add(RegionCompositeFactory.createState(stateHandle));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of suburbs.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createSuburbs(StateBean state, Collection<Suburb> suburbs) {
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (Suburb suburb : suburbs) {
            regionGroup.add(RegionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of suburbs.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createSuburbs(Collection<Suburb> suburbs) {
        State state = (State) firstParent(suburbs, RegionTypes.State);

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (Suburb suburb : suburbs) {
            regionGroup.add(RegionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single suburb
     * */
    public static RegionGroup createSuburb(Suburb state) {
        ArrayList<Suburb> list = new ArrayList<Suburb>();
        list.add(state);
        return createSuburbs(list);
    }

    /**
     * Create a RegionGroup for a collection of states.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createStates(CountryBean country, Collection<State> states) {
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (State state : states) {
            regionGroup.add(RegionCompositeFactory.createState(state));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of states.
     * Each state is represented as a composite
     * */
    public static RegionGroup createStates(Collection<State> states) {
        Country country = (Country) firstParent(states, RegionTypes.Country);

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (State state : states) {
            regionGroup.add(RegionCompositeFactory.createState(state));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single state
     * */
    public static RegionGroup createState(State state) {
        ArrayList<State> list = new ArrayList<State>();
        list.add(state);
        return createStates(list);
    }

    private static RegionIndex firstParent(Collection<? extends RegionIndex> regionBeans) {
        RegionIndex parent = null;
        if ((regionBeans != null) && (regionBeans.size() > 0)) {
            parent = regionBeans.iterator().next().getParent();
        }
        return parent;
    }

    private static Region firstParent(Collection<? extends Region> regions, RegionTypes type) {
        Region parent = null;
        if ((regions != null) && (regions.size() > 0)) {
            parent = regions.iterator().next().getParent(type);
        }
        return parent;
    }

    public static RegionGroup createPostCodes(Collection<PostalCode> postCodes) {
        State state = (State) firstParent(postCodes, RegionTypes.PostCode);

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (PostalCode postCodeBean : postCodes) {
            regionGroup.add(RegionCompositeFactory.createPostCode(postCodeBean));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single PostCode
     * */
    public static RegionGroup createPostCode(PostalCode postCodeBean) {
        ArrayList<PostalCode> list = new ArrayList<PostalCode>();
        list.add(postCodeBean);
        return createPostCodes(list);
    }

//    public static RegionGroup createAddresses(Premise premise, Collection<Address> addresses) {
//        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(premise));
//
//        for (Address address : addresses) {
//            regionGroup.add(RegionCompositeFactory.create(address));
//        }
//
//        return regionGroup;
//    }
}