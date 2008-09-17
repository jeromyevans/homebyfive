package com.blueskyminds.housepad.core.property.model;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.model.*;
import com.blueskyminds.housepad.core.region.model.PostCodeBean;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.landmine.core.property.PropertyTypes;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TesPropertyEAO extends OutOfContainerTestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private PropertyEAO propertyEAO;
    private static final String AU = "au";

    public TesPropertyEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        propertyEAO = new PropertyEAO(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostCodeBean postCode2089 = new PostCodeBean(au, nsw, "2089");
        em.persist(postCode2089);
        SuburbBean neutralBay = new SuburbBean(au, nsw, postCode2089, "Neutral Bay");
        em.persist(neutralBay);

        PropertyBean propertyBean = new PropertyBean(neutralBay, "1", "22", "Spruson", StreetType.Street, null, null, null, null, PropertyTypes.House, null);
        em.persist(propertyBean);
    }

    public void testListPropertiesInSuburb() {
        Set<PropertyBean> properties = propertyEAO.listPropertiesInSuburb("/au/nsw/neutral+bay");
        assertNotNull(properties);
        assertEquals(1, properties.size());
    }

    public void testListPropertiesInPostCode() {
        Set<PropertyBean> properties = propertyEAO.listPropertiesInPostCode("/au/nsw/2089");
        assertNotNull(properties);
        assertEquals(1, properties.size());
    }

    public void testListPropertiesInPath() {
        Set<PropertyBean> properties = propertyEAO.listProperties("/au/nsw/");
        assertNotNull(properties);
        assertEquals(1, properties.size());
    }


    public void testLookupProperty() {
        PropertyBean propertyBean = propertyEAO.lookupProperty("/au/nsw/neutral+bay/spruson+street/22/1");
        assertNotNull(propertyBean);
    }


}