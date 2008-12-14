package com.blueskyminds.homebyfive.business.region.reference;

import com.blueskyminds.homebyfive.business.region.*;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.index.*;
import com.blueskyminds.homebyfive.business.address.Address;
//import com.blueskyminds.landmine.core.property.Premise;

import java.util.List;
import java.util.ArrayList;

/**
 * Creates a RegionRef from various sources
 *
 * Date Started: 31/10/2007
 * <p/>
 * History:
 */
public class RegionRefFactory {

     /**
     * Create a RegionRef from a RegionHandle
     *
     * @param regionHandle
     * @return
     */
    public static RegionRef createRef(Suburb regionHandle) {
        if (regionHandle != null) {
            RegionRef regionRef = new RegionRef(regionHandle.getId(), PathHelper.buildPath(regionHandle), regionHandle.getName(), null, RegionRefType.toRegionRefType(regionHandle.getType()));
            populateAttributes(regionRef, regionHandle);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a RegionHandle
     *
     * @param regionHandle
     * @return
     */
    public static RegionRef createRef(State regionHandle) {
        if (regionHandle != null) {
            RegionRef regionRef = new RegionRef(regionHandle.getId(), PathHelper.buildPath(regionHandle), regionHandle.getName(), null, RegionRefType.toRegionRefType(regionHandle.getType()));
            populateAttributes(regionRef, regionHandle);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a RegionHandle
     *
     * @param regionHandle
     * @return
     */
    public static RegionRef createRef(Country regionHandle) {
        if (regionHandle != null) {
            RegionRef regionRef = new RegionRef(regionHandle.getId(), PathHelper.buildPath(regionHandle), regionHandle.getName(), null, RegionRefType.toRegionRefType(regionHandle.getType()));
            populateAttributes(regionRef, regionHandle);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a RegionHandle
     *
     * @param regionHandle
     * @return
     */
    public static RegionRef createRef(PostalCode regionHandle) {
        if (regionHandle != null) {
            RegionRef regionRef = new RegionRef(regionHandle.getId(), PathHelper.buildPath(regionHandle), regionHandle.getName(), null, RegionRefType.toRegionRefType(regionHandle.getType()));
            populateAttributes(regionRef, regionHandle);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
    * Create a RegionRef from a RegionHandle
    *
    * @param regionHandle
    * @return
    */
    public static RegionRef createRef(Region regionHandle) {
        if (regionHandle != null) {
            RegionRef regionRef = new RegionRef(regionHandle.getId(), PathHelper.buildPath(regionHandle), regionHandle.getName(), null, RegionRefType.toRegionRefType(regionHandle.getType()));
            populateAttributes(regionRef, regionHandle);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a CountryBean
     *
     * @param countryBean
     * @return
     */
    public static RegionRef createRef(CountryBean countryBean) {
        if (countryBean != null) {
            RegionRef regionRef = new RegionRef(countryBean.getId(), countryBean.getPath(), countryBean.getName(), countryBean.getAbbr(), RegionRefType.Country);
            populateAttributes(regionRef, countryBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a StateBean
     *
     * @param stateBean
     * @return
     */
    public static RegionRef createRef(StateBean stateBean) {
        if (stateBean != null) {
            RegionRef regionRef = new RegionRef(stateBean.getId(), stateBean.getPath(), stateBean.getName(), stateBean.getAbbr(), RegionRefType.State);
            populateAttributes(regionRef, stateBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a SuburbBean
     *
     * @param suburbBean
     * @return
     */
    public static RegionRef createRef(SuburbBean suburbBean) {
        if (suburbBean != null) {
            RegionRef regionRef = new RegionRef(suburbBean.getId(), suburbBean.getPath(), suburbBean.getName(), null, RegionRefType.Suburb);
            populateAttributes(regionRef, suburbBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a PostCodeBean
     *
     * @param postCodeBean
     * @return
     */
    public static RegionRef createRef(PostalCodeBean postCodeBean) {
        if (postCodeBean != null) {
            RegionRef regionRef = new RegionRef(postCodeBean.getId(), postCodeBean.getPath(), postCodeBean.getName(), null, RegionRefType.PostCode);
            populateAttributes(regionRef, postCodeBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a Street
     *
     * @param street
     * @return
     */
    public static RegionRef createRef(Street street, Address address) {
        if (street != null) {
            RegionRef regionRef = new RegionRef(street.getId(), PathHelper.buildPath(address.getSuburb(), street), street.getFullName(), null, RegionRefType.Street);

            populateAttributes(regionRef, address);
            regionRef.setAttribute(RegionRefAttributes.STREET, RegionTools.encode(street.getFullName()));

            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from an Address
     *
     * @return
     */
    public static RegionRef createRef(Address address) {
        if (address != null) {
            RegionRef regionRef = new RegionRef(address.getId(), PathHelper.buildPath(address),  null, null, RegionRefType.Address);
            populateAttributes(regionRef, address);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef from a Premise.  Uses the primary address
     *
     * @return
     */
//    public static RegionRef createRef(Premise premise) {
//        if (premise != null) {
//            Address address = premise.getAddress();
//            if (address != null) {
//                RegionRef regionRef = new RegionRef(address.getId(), PathHelper.buildPath(address), null, null, RegionRefType.Premise);
//                populateAttributes(regionRef, address);
//                return regionRef;
//            }
//        }
//        return null;
//    }

    /**
     * @return
     */
    public static RegionRef createRef(String path, String name, RegionRefType type) {
        return new RegionRef(path, name, null, type);
    }


    /**
     * Create a RegionRef to the PostCode
     *
     * @param postCodeRef
     * @return
     */
    public static RegionRef createPostCodeRef(PostCodeRef postCodeRef) {
        if (postCodeRef != null) {
            RegionRef regionRef = new RegionRef(postCodeRef.getPostCodeId(), postCodeRef.getPostCodePath(), postCodeRef.getPostCodeName(), null, RegionRefType.PostCode);
            //populateAttributes(regionRef, suburbBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef to the State
     *
     * @param stateRef
     * @return
     */
    public static RegionRef createStateRef(StateRef stateRef) {
        if (stateRef != null) {
            RegionRef regionRef = new RegionRef(stateRef.getStateId(), stateRef.getStatePath(), stateRef.getStateName(), null, RegionRefType.State);
            //populateAttributes(regionRef, suburbBean);
            return regionRef;
        } else {
            return null;
        }
    }

    /**
     * Create a RegionRef to the Country
     *
     * @param countryRef
     * @return
     */
    public static RegionRef createCountryRef(CountryRef countryRef) {
        if (countryRef != null) {
            RegionRef regionRef = new RegionRef(countryRef.getCountryId(), countryRef.getCountryPath(), countryRef.getCountryName(), null, RegionRefType.Country);
            //populateAttributes(regionRef, suburbBean);
            return regionRef;
        } else {
            return null;
        }
    }

    public static List<RegionRef> createRefs(List<Region> regionHandles) {
        List<RegionRef> regionRefs = new ArrayList<RegionRef>(regionHandles.size());
        for (Region region : regionHandles) {
            regionRefs.add(createRef(region));
        }
        return regionRefs;
    }

    /** Applies some useful attributes to a RegionRef */
    private static void populateAttributes(RegionRef regionRef, Region regionHandle) {
/*        Region suburb;
        if (regionHandle instanceof Suburb) {
            suburb = regionHandle;
        } else {
            suburb = regionHandle.getParent(RegionTypes.Suburb);
        }

        if (suburb != null) {
            regionRef.setAttribute(RegionRefAttributes.SUBURB, RegionTools.encode(suburb.getName()));
        }

        Region state;
        if (regionHandle instanceof State) {
            state = regionHandle;
        } else {
            state = regionHandle.getParent(RegionTypes.State);
        }

        if (state != null) {
            regionRef.setAttribute(RegionRefAttributes.STATE, RegionTools.encode(state.getAbbr()));
        }

        Region postCode;
        if (regionHandle instanceof PostalCode) {
            postCode = regionHandle;
        } else {
            postCode = regionHandle.getParent(RegionTypes.PostCode);
        }

        if (postCode != null) {
            regionRef.setAttribute(RegionRefAttributes.POSTCODE, RegionTools.encode(postCode.getName()));
        }

        Region country;
        if (state != null) {
            country = state.getParent(RegionTypes.Country);
            if (country != null) {
                regionRef.setAttribute(RegionRefAttributes.COUNTRY, RegionTools.encode(country.getAbbr()));
            }
        }      */
    }

    /** Applies some useful attributes to a RegionRef */
    private static void populateAttributes(RegionRef regionRef, RegionIndex regionBean) {
    }

    /** Applies some useful attributes to a RegionRef */  
    public static void populateAttributes(RegionRef regionRef, Address address) {
/*        Region suburb = address.getSuburb();
        Region state = address.getState();
        Region postCode = address.getPostCode();
        if (suburb != null) {
            regionRef.setAttribute(RegionRefAttributes.SUBURB, RegionTools.encode(suburb.getName()));
        }
        if (postCode != null) {
            regionRef.setAttribute(RegionRefAttributes.POSTCODE, RegionTools.encode(postCode.getName()));
        }
        if (state != null) {
            regionRef.setAttribute(RegionRefAttributes.STATE, RegionTools.encode(state.getAbbr()));

            Region country = state.getParent(RegionTypes.Country);
            if (country != null) {
                regionRef.setAttribute(RegionRefAttributes.COUNTRY, RegionTools.encode(country.getAbbr()));
            }
        } */
    }
}
