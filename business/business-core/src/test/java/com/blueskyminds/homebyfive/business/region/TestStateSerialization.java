package com.blueskyminds.homebyfive.business.region;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.framework.core.tools.xml.XMLSerializer;

/**
 * Date Started: 29/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestStateSerialization extends TestCase {

    public void testSerialization() {
        Country country = new Country("Australia", "AU");
        State state = new State(country, "New South Wales", "NSW");
        XMLSerializer<State> serializer = new XMLSerializer<State>();
        System.out.println(serializer.serialize(state));
    }
}
