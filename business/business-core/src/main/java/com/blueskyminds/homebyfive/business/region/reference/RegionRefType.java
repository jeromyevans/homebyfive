package com.blueskyminds.homebyfive.business.region.reference;

import com.blueskyminds.homebyfive.business.region.RegionTypes;

/**
 * // todo: this can probably be merged into RegionTypes
 *
 * Date Started: 25/10/2007
 * <p/>
 * History:
 */
public enum RegionRefType {
    Premise,
    Address,
    Street,
    Suburb,
    PostCode,
    State,
    Country,
    Region;

    /**
     * Map the RegionRefType to a RegionType.
     * RegionType's are a subset of the RegionRefType
     *
     * @param type
     * @return
     */
    public static RegionRefType toRegionRefType(RegionTypes type) {
        switch (type) {
            case Country:
                return Country;
            case PostCode:
                return PostCode;
            case State:
                return State;
            case Suburb:
                return Suburb;
            default:
                return null;
        }
    }
}
