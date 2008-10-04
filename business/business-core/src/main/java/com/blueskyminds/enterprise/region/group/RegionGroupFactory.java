package com.blueskyminds.enterprise.region.group;

import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.index.*;
import com.blueskyminds.enterprise.region.composite.RegionCompositeFactory;
import com.blueskyminds.enterprise.region.reference.RegionRefFactory;
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
    public static RegionGroup createCountries(Collection<? extends CountryBean> countries) {
        RegionGroup regionGroup = new RegionGroup();
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();
        for (CountryBean country : countries) {
            regionGroup.add(regionCompositeFactory.createCountry(country));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single country
     * */
    public static RegionGroup createCountry(CountryBean countryBean) {
        ArrayList<CountryBean> list = new ArrayList<CountryBean>();
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
    public static RegionGroup createStates(Country country, Collection<? extends State> states) {
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (State stateHandle : states) {
            regionGroup.add(regionCompositeFactory.createState(stateHandle));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of suburbs.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createSuburbs(StateBean state, Collection<? extends SuburbBean> suburbs) {
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (SuburbBean suburb : suburbs) {
            regionGroup.add(regionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of suburbs.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createSuburbs(Collection<? extends SuburbBean> suburbs) {
        StateBean state = (StateBean) firstParent(suburbs);
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (SuburbBean suburb : suburbs) {
            regionGroup.add(regionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single suburb
     * */
    public static RegionGroup createSuburb(SuburbBean state) {
        ArrayList<SuburbBean> list = new ArrayList<SuburbBean>();
        list.add(state);
        return createSuburbs(list);
    }

    /**
     * Create a RegionGroup for a collection of states.
     * Each suburb is represented as a composite
     * */
    public static RegionGroup createStates(CountryBean country, Collection<? extends StateBean> states) {
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();
        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (StateBean state : states) {
            regionGroup.add(regionCompositeFactory.createState(state));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a collection of states.
     * Each state is represented as a composite
     * */
    public static RegionGroup createStates(Collection<? extends StateBean> states) {
        CountryBean country = (CountryBean) firstParent(states);
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(country));
        for (StateBean state : states) {
            regionGroup.add(regionCompositeFactory.createState(state));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single state
     * */
    public static RegionGroup createState(StateBean state) {
        ArrayList<StateBean> list = new ArrayList<StateBean>();
        list.add(state);
        return createStates(list);
    }

    private static RegionBean firstParent(Collection<? extends RegionBean> regionBeans) {
        RegionBean parent = null;
        if ((regionBeans != null) && (regionBeans.size() > 0)) {
            parent = regionBeans.iterator().next().getParent();
        }
        return parent;
    }

    public static RegionGroup createPostCodes(Collection<PostCodeBean> postCodes) {
        StateBean state = (StateBean) firstParent(postCodes);
        RegionCompositeFactory regionCompositeFactory = new RegionCompositeFactory();

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (PostCodeBean postCodeBean : postCodes) {
            regionGroup.add(regionCompositeFactory.createPostCode(postCodeBean));
        }
        return regionGroup;
    }

    /**
     * Create a RegionGroup for a single PostCode
     * */
    public static RegionGroup createPostCode(PostCodeBean postCodeBean) {
        ArrayList<PostCodeBean> list = new ArrayList<PostCodeBean>();
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
