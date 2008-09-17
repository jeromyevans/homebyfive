package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.test.HypersonicTestCase;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.landmine.core.property.advertisement.*;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Tests persistence of the property advertisement beans
 *
 * Date Started: 13/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestPropertyAdvertisementBean extends HypersonicTestCase {

    public TestPropertyAdvertisementBean(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestPropertyAdvertisementBean with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

     private void loadSampleData(Connection connection) throws SQLException {

        PersistenceTools.executeUpdate(connection, PropertyAdvertisementBeanFinder.CREATE_STATEMENT);
        PersistenceTools.executeUpdate(connection, PropertyAdvertisementAgencyBeanFinder.CREATE_STATEMENT);
        PersistenceTools.executeUpdate(connection, PropertyAdvertisementContactBeanFinder.CREATE_STATEMENT);

        PropertyAdvertisementBean bean = new PropertyAdvertisementBean();
        bean.setId(1L);
        bean.setAddress1("196 Seventh Road");   //reference: REIWA 16671
        bean.setAddress2(null);
        bean.setBathrooms(0);
        bean.setBedrooms(0);
        bean.setConstructionDate(null);
        bean.setDescription("196 SEVENTH ROAD, ARMADALE3 BEDROOM, 1 BATHROOM HOUSE WITH LOUNGE/DINING/KITCHEN, SEPARATE FORMAL LOUNGE/FAMILY ROOM, WOOD FIRE, PATIO, UNDERCIVER PARKING, LOCATED ACROSS THE ROAD FROM NEERIGEN BROOK PRIMARY SCHOOL.");
        bean.addFeature("Exterior Construction: See Remarks");
        bean.addFeature("Roof: See Remarks");
        bean.setFloorArea(null);
        bean.setLandArea(null);
        bean.setPostCode(null);
        bean.setPrice("$ 130 Rent per wk.");
        bean.setPropertyType("House");
        bean.setState("WA");
        bean.setSuburb("Armadale");
        bean.setType(PropertyAdvertisementTypes.Lease);
        PropertyAdvertisementAgencyBean agentBean = new PropertyAdvertisementAgencyBean();
        agentBean.setId(1L);
        agentBean.setAgencyName("Kelmscott First National Real Estate");
        agentBean.setAgencyPhone("9495 1212");
        agentBean.setWebsite("www.kelmscottfn.com.au");
        agentBean.setAgencyEmail("properties@kelmscottfn.com.au");
        PropertyAdvertisementContactBean contact = new PropertyAdvertisementContactBean(agentBean);
        contact.setContactName("Sarah Williams");
        contact.setId(1L);
        agentBean.addContact(contact);
        bean.setAgent(agentBean);

        bean.insert(connection);                  
    }

    public void testAdvertisementBeanFinder() throws Exception {
       Connection connection = getConnection();
       loadSampleData(connection);
       PropertyAdvertisementBeanFinder finder = new PropertyAdvertisementBeanFinder(connection);

       PropertyAdvertisementBean bean = finder.findById(1);

       assertNotNull(bean);
       assertEquals(1, (long) bean.getId());
       assertNotNull(bean.getAgent());
       assertEquals(1, bean.getAgent().getContacts().size());
       connection.close();
   }
}
