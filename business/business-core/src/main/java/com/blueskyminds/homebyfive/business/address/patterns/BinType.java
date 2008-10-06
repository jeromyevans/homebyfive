package com.blueskyminds.homebyfive.business.address.patterns;

/**
 * Date Started: 16/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public enum BinType {

    StateBin,
    UnitNumberBin,
    LotNumberBin,
    StreetNumberBin,
    StreetTypeBin,
    StreetSectionBin,
    SuburbNameBin,
    StreetNameBin,
    PostCodeBin;

    /**
     * The subset of bins that are relevant when the country is known.
     * Order is significant - its the recommended order to search
     * @return
     */
    public static BinType[] valuesInCountryContext() {
        return new BinType[]{StateBin, UnitNumberBin, LotNumberBin, StreetNumberBin, StreetTypeBin, SuburbNameBin, StreetNameBin, StreetSectionBin, PostCodeBin};
    }

    /**
     * The subset of bins that are relevant when the suburb is already known
     * Order is significant - its the recommended order to search
     * @return
     */
    public static BinType[] valuesInSuburbContext() {
        return new BinType[]{ UnitNumberBin, LotNumberBin, StreetNumberBin, StreetTypeBin, StreetNameBin, StreetSectionBin};
    }
}
