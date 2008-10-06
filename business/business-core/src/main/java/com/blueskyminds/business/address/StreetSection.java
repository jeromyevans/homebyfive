package com.blueskyminds.business.address;

/**
 * A section of a street.  Eg. North, South, East, West
 *
 * Date Created: 15 Feb 2007
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */

public enum StreetSection {

    Unknown,
    NA,
    North,
    South,
    East,
    West,
    NorthEast,
    NorthWest,
    SouthEast,
    SouthWest,
    Central;

    /**
     * Similar to the valueOf method but accepts null values, invalid values and ignores case
     * @param str
     * @return the StreetSection if matched, otherwise null
     */
    public static StreetSection valueOfIgnoreCase(String str) {
        if (str != null) {
            for (StreetSection value : values()) {
                if (str.equalsIgnoreCase(value.name())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static boolean equalsAnyIgnoreCase(String str) {
        return valueOfIgnoreCase(str) != null;
    }
}