package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.business.address.AddressPathComponents;
import com.blueskyminds.homebyfive.business.address.StreetType;
import junit.framework.TestCase;

/**
 * Date Started: 5/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestAddressPathComponents extends TestCase {

    public void testAddressPathComponents() {
        AddressPathComponents components = new AddressPathComponents("/au/nsw/neutral+bay/darley+street/21/2");

        assertEquals("/au", components.getCountryPath());
        assertEquals("/au/nsw", components.getStatePath());
        assertEquals("/au/nsw/neutral+bay", components.getSuburbPath());
        assertEquals("/au/nsw/neutral+bay/darley+street", components.getStreetPath());
        assertEquals("/au/nsw/neutral+bay/darley+street/21", components.getStreetNoPath());
        assertEquals("/au/nsw/neutral+bay/darley+street/21/2", components.getUnitNoPath());
        assertEquals(null, components.getPostCodePath());

        assertEquals("Darley", components.getStreetName());
        assertEquals(StreetType.Street, components.getStreetType());
        assertEquals(null, components.getStreetSection());

    }
}
