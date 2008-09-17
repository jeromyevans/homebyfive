package com.blueskyminds.housepad.core.model.property;

import junit.framework.TestCase;
import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.housepad.core.property.model.PropertyTableFactory;

import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 18/04/2008
 */
public class TestPropertyTableFactory extends TestCase {

    public void testFactory() {
        PropertyBean propertyBean = new PropertyBean();
        List<PropertyBean> properties = new LinkedList<PropertyBean>();
        properties.add(propertyBean);
        PropertyTableFactory.createTable(properties);
    }
}
