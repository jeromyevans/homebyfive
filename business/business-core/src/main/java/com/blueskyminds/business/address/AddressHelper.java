package com.blueskyminds.business.address;

import com.blueskyminds.business.region.graph.Country;
import com.blueskyminds.business.region.graph.*;

/**
 * Access fields of an address without knowing its implementation
 *
 * Date Started: 7/04/2008
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AddressHelper {

    /**
     * Returns the unit number component of an address if defined
     *
     * @param address
     * @return
     */
    public static String unitNo(Address address) {
        if (address instanceof UnitAddress) {
            return ((UnitAddress) address).getUnitNumber();
        }
        return null;
    }

    /**
     * Returns the lot number component of an address if defined
     *
     * @param address
     * @return
     */
    public static String lotNo(Address address) {
        if (address instanceof LotAddress) {
            return ((LotAddress) address).getLotNumber();
        }
        return null;
    }

    /**
     * Returns the street number component of an address if defined
     *
     * @param address
     * @return
     */
    public static String streetNo(Address address) {
        if (address instanceof StreetAddress) {
            return ((StreetAddress) address).getStreetNumber();
        }
        return null;
    }

    /**
     * Returns the number component of an address if defined
     * The format of the number component depends on the type.  It may include symbols.
     * eg. 1/22
     *
     * @param address
     * @return
     */
    public static String number(Address address) {
        return address.getNumber();
    }

    /**
     * Returns the street component of an address if defined
     *
     * @param address
     * @return
     */
    public static Street street(Address address) {
        return address.getStreet();
    }

    /**
     * Returns the street name of an address if defined
     * The street name does not include the street type or section
     *
     * @param address
     * @return
     */
    public static String streetName(Address address) {
        Street street = address.getStreet();
        if (street != null) {
            return street.getName();
        }
        return null;
    }

    /**
     * Returns the street name of an address if defined
     * The full street name includes the street type and section
     *
     * @param address
     * @return
     */
    public static String streetNameFull(Address address) {
        Street street = address.getStreet();
        if (street != null) {
            return street.getFullName();
        }
        return null;
    }

    /**
     * Returns the street type of an address if defined
     *
     * @param address
     * @return
     */
    public static StreetType streetType(Address address) {
        Street street = address.getStreet();
        if (street != null) {
            return street.getStreetType();
        }
        return null;
    }

    /**
     * Returns the street section of an address if defined
     *
     * @param address
     * @return
     */
    public static StreetSection streetSection(Address address) {        
        Street street = address.getStreet();
        if (street != null) {
            return street.getSection();
        }
        return null;
    }

    /**
     * Returns the suburb section of an address if defined
     *
     * @param address
     * @return
     */
    public static String suburb(Address address) {
        Suburb suburb = address.getSuburb();
        if (suburb != null) {
            return suburb.getName();
        }
        return null;
    }

    /**
     * Returns the postcode section of an address if defined
     *
     * @param address
     * @return
     */
    public static String postCode(Address address) {
        PostalCode postCodeHandle = address.getPostCode();
        if (postCodeHandle != null) {
            return postCodeHandle.getName();
        }
        return null;
    }

    /**
     * Returns the state section of an address if defined
     *
     * @param address
     * @return
     */
    public static String state(Address address) {
        State stateHandle = address.getState();
        if (stateHandle != null) {
            return stateHandle.getName();
        }
        return null;
    }

    /**
     * Returns the country section of an address if defined
     *
     * @param address
     * @return
     */
    public static String country(Address address) {
        Country countryHandle = address.getCountry();
        if (countryHandle != null) {
            return countryHandle.getName();
        }
        return null;
    }
}
