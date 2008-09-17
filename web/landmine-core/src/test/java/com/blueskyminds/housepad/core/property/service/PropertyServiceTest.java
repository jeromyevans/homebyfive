package com.blueskyminds.housepad.core.property.service;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.model.StateBean;
import com.blueskyminds.housepad.core.region.model.PostCodeBean;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.landmine.core.property.PropertyTypes;

import java.util.Set;

/**
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public class PropertyServiceTest extends OutOfContainerTestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private PropertyService propertyService;
    private SuburbBean neutralBay;

    public PropertyServiceTest() {
        super(PERSISTENCE_UNIT);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PropertyEAO propertyEAO = new PropertyEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);       
        propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostCodeBean postCode2089 = new PostCodeBean(au, nsw, "2089");
        em.persist(postCode2089);
        neutralBay = new SuburbBean(au, nsw, postCode2089, "Neutral Bay");
        em.persist(neutralBay);

        PropertyBean propertyBean = new PropertyBean(neutralBay, "1", "22", "Spruson", StreetType.Street, null, null, null, null, PropertyTypes.House, null);
        em.persist(propertyBean);

    }

    public void testCreate() throws Exception {
        PropertyBean propertyBean = new PropertyBean(neutralBay, "2", "22", "Spruson", StreetType.Street, null, null, null, null, PropertyTypes.House, null);
        propertyService.createProperty(propertyBean);

        Set<PropertyBean> properties = propertyService.listProperties("au", "nsw", "neutral+bay");
        assertEquals(2, properties.size());
    }
}
