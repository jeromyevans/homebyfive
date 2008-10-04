package com.blueskyminds.enterprise.region.composite;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.StreetAddress;
import com.blueskyminds.enterprise.address.UnitAddress;
import com.blueskyminds.enterprise.address.LotAddress;
import com.blueskyminds.enterprise.region.graph.SuburbHandle;
import com.blueskyminds.enterprise.region.graph.PostCodeHandle;
import com.blueskyminds.enterprise.region.index.PostCodeBean;
import com.blueskyminds.enterprise.region.graph.StateHandle;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.CountryHandle;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.index.SuburbBean;
import com.blueskyminds.enterprise.region.reference.RegionRefFactory;
import com.blueskyminds.enterprise.region.reference.RegionRefType;

import java.util.List;
import java.util.ArrayList;

/**
 * Creates a RegionComposite from various sources
 *
 * Date Started: 24/10/2007
 * <p/>
 * History:
 */
public class RegionCompositeFactory {

    /**
     * Create a RegionComposite from an address
     *
     * @param address
     * @return
     */
    public static RegionComposite create(Address address) {
        RegionComposite regionComposite = new RegionComposite();

        if (address instanceof UnitAddress) {
            UnitAddress streetAddress = (UnitAddress) address;
            regionComposite.add(RegionRefFactory.createRef(PathHelper.buildPath(address), streetAddress.getNumber(), RegionRefType.Premise));
            regionComposite.add(RegionRefFactory.createRef(streetAddress.getStreet(), streetAddress));
        } else {
            if (address instanceof LotAddress) {
                LotAddress streetAddress = (LotAddress) address;
                regionComposite.add(RegionRefFactory.createRef(PathHelper.buildPath(address), streetAddress.getNumber(), RegionRefType.Premise));
                regionComposite.add(RegionRefFactory.createRef(streetAddress.getStreet(), streetAddress));
            } else {
                if (address instanceof StreetAddress) {
                    StreetAddress streetAddress = (StreetAddress) address;
                    regionComposite.add(RegionRefFactory.createRef(PathHelper.buildPath(address), streetAddress.getNumber(), RegionRefType.Premise));
                    regionComposite.add(RegionRefFactory.createRef(streetAddress.getStreet(), streetAddress));
                }
            }
        }


        SuburbHandle suburb = address.getSuburb();
        if (suburb != null) {
            regionComposite.add(RegionRefFactory.createRef(suburb));
        }
        PostCodeHandle postCode = address.getPostCode();
        if (postCode != null) {
            regionComposite.add(RegionRefFactory.createRef(postCode));
        } else {
            if (suburb != null) {
                regionComposite.add(RegionRefFactory.createRef(suburb.getPostCode()));
            }
        }
        regionComposite.add(RegionRefFactory.createRef(address.getState()));
        
        return regionComposite;
    }

    /**
     * Create a RegionComposite from a RegionHandle
     *
     * @param regionHandle
     * @return
     */
    public static RegionComposite create(RegionHandle regionHandle) {
        RegionComposite result = null;
        switch (regionHandle.getType()) {
            case Country:
                result = createCountry((CountryHandle) regionHandle);
                break;
            case PostCode:
                result = createPostCode((PostCodeHandle) regionHandle);
                break;
            case State:
                result = createState((StateHandle) regionHandle);
                break;
            case Suburb:
                result = createSuburb((SuburbHandle) regionHandle);
                break;
        }

        return result;
    }

    public static List<RegionComposite> createList(List<RegionHandle> regionHandles) {
        List<RegionComposite> list = new ArrayList<RegionComposite>(regionHandles.size());
        for (RegionHandle regionHandle : regionHandles) {
            list.add(create(regionHandle));
        }
        return list;
    }

    /**
     * Create a RegionComposite from a suburb (suburb | postcode | state | country)
     *
     * @param suburb
     * @return
     */
    public static RegionComposite createSuburb(SuburbHandle suburb) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(suburb));
        regionComposite.add(RegionRefFactory.createRef(suburb.getPostCode()));
        StateHandle state = suburb.getState();
        regionComposite.add(RegionRefFactory.createRef(state));
        if (state != null) {
            regionComposite.add(RegionRefFactory.createRef(state.getCountry()));
        }

        return regionComposite;
    }

    /**
     * Create a RegionComposite from a suburb (suburb | postcode | state | country)
     * Uses the literal values out of the bean so it can be used outside an EntityManager
     * @param suburb
     * @return
     */
    public static RegionComposite createSuburb(SuburbBean suburb) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(suburb));
        regionComposite.add(RegionRefFactory.createPostCodeRef(suburb));
        regionComposite.add(RegionRefFactory.createStateRef(suburb));
        regionComposite.add(RegionRefFactory.createCountryRef(suburb));

        return regionComposite;
    }

    /**
     * Create a RegionComposite from a postcode (postcode | state | country)
     * Uses the literal values out of the bean so it can be used outside an EntityManager
     * @param postCode
     * @return
     */
    public static RegionComposite createPostCode(PostCodeBean postCode) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(postCode));
        regionComposite.add(RegionRefFactory.createStateRef(postCode));
        regionComposite.add(RegionRefFactory.createCountryRef(postCode));

        return regionComposite;
    }

    /**
     * Create a RegionComposite from a postcode (postcode | state | country)
     * Uses the literal values out of the bean so it can be used outside an EntityManager
     * @param postCode
     * @return
     */
    public static RegionComposite createPostCode(PostCodeHandle postCode) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(postCode));
        StateHandle stateHandle = postCode.getState();
        if (stateHandle != null) {
            regionComposite.add(RegionRefFactory.createRef(stateHandle));
            regionComposite.add(RegionRefFactory.createRef(stateHandle.getCountry()));
        }

        return regionComposite;
    }

     /**
     * Create a RegionComposite from a country (country)

     * @param countryHandle
     * @return
     */
    public static RegionComposite createCountry(CountryHandle countryHandle) {
        RegionComposite regionComposite = new RegionComposite();
        regionComposite.add(RegionRefFactory.createRef(countryHandle));        
        return regionComposite;
    }

    /**
     * Create a RegionComposite from a state (state | country)
     *
     * @param state
     * @return
     */
    public static RegionComposite createState(StateHandle state) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(state));
        regionComposite.add(RegionRefFactory.createRef(state.getCountry()));

        return regionComposite;
    }

    /**
     * Create a RegionComposite from a state (state | country)
     *
     * Uses the literal values out of the bean so it can be used outside an EntityManager
     *
     * @param state
     * @return
     */
    public static RegionComposite createState(StateBean state) {
        RegionComposite regionComposite = new RegionComposite();

        regionComposite.add(RegionRefFactory.createRef(state));
        regionComposite.add(RegionRefFactory.createCountryRef(state));

        return regionComposite;
    }

    /**
     * Create a RegionComposite from a country
     *
     * @param country
     * @return
     */
    public static RegionComposite createCountry(CountryBean country) {
        RegionComposite regionComposite = new RegionComposite();
        regionComposite.add(RegionRefFactory.createRef(country));
        return regionComposite;
    }
}
