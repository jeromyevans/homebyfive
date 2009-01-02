package com.blueskyminds.homebyfive.business.region.group;

import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.region.index.*;
import com.blueskyminds.homebyfive.business.region.composite.RegionCompositeFactory;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefFactory;
//import com.blueskyminds.landmine.core.property.Premise;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates RegionGroups from various sources
 *
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class RegionGroupFactory {

    public static RegionGroup create(Region region) {
        RegionGroup result = null;

        if (region != null) {
            switch (region.getType()) {
                case Country:
                    result = createCountry((Country) region);
                    break;
                case PostCode:
                    result = createPostCode((PostalCode) region);
                    break;
                case State:
                    result = createState((State) region);
                    break;
                case Street:
                    result = createStreet((Street) region);
                    break;
                case Suburb:
                    result = createSuburb((Suburb) region);
                    break;
            }
        }

        return result;
    }

    /**
     * Create a RegionGroup for a collection of countries.
     * */
    public static RegionGroup createCountries(Collection<Country> countries) {
        RegionGroup regionGroup = new RegionGroup();        
        List<Country> sorted = new ArrayList<Country>(countries);
        Collections.sort(sorted);
        for (Country country : sorted) {
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
        Region state = firstParent(suburbs);

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(state));
        for (Suburb suburb : suburbs) {
            regionGroup.add(RegionCompositeFactory.createSuburb(suburb));
        }
        return regionGroup;
    }

     /**
     * Create a RegionGroup for a collection of streets.
     * Each street is represented as a composite
     * */
    public static RegionGroup createStreets(Collection<Street> streets) {
        Region suburb = firstParent(streets);

        RegionGroup regionGroup = new RegionGroup(RegionRefFactory.createRef(suburb));
        for (Street street : streets) {
            regionGroup.add(RegionCompositeFactory.createStreet(street));
        }
        return regionGroup;
    }


    /**
     * Create a RegionGroup for a single suburb
     * */
    public static RegionGroup createSuburb(Suburb suburb) {
        ArrayList<Suburb> list = new ArrayList<Suburb>();
        list.add(suburb);
        return createSuburbs(list);
    }

    /**
     * Create a RegionGroup for a single street
     * */
    public static RegionGroup createStreet(Street street) {
        ArrayList<Street> list = new ArrayList<Street>();
        list.add(street);
        return createStreets(list);
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
        Region country = firstParent(states);

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

    private static Region firstParent(Collection<? extends Region> regions) {
        Region parent = null;
        if ((regions != null) && (regions.size() > 0)) {
            parent = regions.iterator().next().getParent();
        }
        return parent;
    }

    public static RegionGroup createPostCodes(Collection<PostalCode> postCodes) {
        Region state = firstParent(postCodes);

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
