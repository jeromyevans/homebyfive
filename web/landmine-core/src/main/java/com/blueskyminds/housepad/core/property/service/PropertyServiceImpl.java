package com.blueskyminds.housepad.core.property.service;

import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyAttributesHelper;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.AddressHelper;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public class PropertyServiceImpl implements PropertyService {

    private static final Log LOG = LogFactory.getLog(PropertyServiceImpl.class);

    private PropertyEAO propertyEAO;
    private SuburbEAO suburbEAO;
    private EntityManager em;

    public PropertyServiceImpl() {
    }

    public PropertyServiceImpl(PropertyEAO propertyEAO, SuburbEAO suburbEAO, EntityManager em) {
        this.propertyEAO = propertyEAO;
        this.suburbEAO = suburbEAO;
        this.em = em;
    }

    public Set<PropertyBean> listProperties(String country, String state, String suburb) {
        Set<PropertyBean> properties = propertyEAO.listPropertiesInSuburb(PathHelper.buildPath(country, state, suburb));
        return properties;
    }

    /**
     * Create a PropertyBean
     *
     * @param propertyBean
     * @return
     * @throws DuplicatePropertyException
     */
    public PropertyBean createProperty(PropertyBean propertyBean) throws DuplicatePropertyException {
        propertyBean.populateAttributes();
        PropertyBean existing = propertyEAO.lookupProperty(propertyBean.getPath());
        if (existing == null) {
            em.persist(propertyBean);
        } else {
            throw new DuplicatePropertyException(propertyBean);
        }
        return propertyBean;
    }

    /**
     * Create a PropertyBean entry from a Premise.  If propertyBean(s) already exist they will be modified
     *
     * @param premise
     * @return
     */
    public Set<PropertyBean> createOrUpdateProperties(Premise premise) {
        Set<PropertyBean> propertes = null;
        Set<PropertyBean> existing;

        existing = propertyEAO.lookupProperties(premise);

        if (existing.size() == 0) {
            // create a new entry.
            // only create entries for premises with a street address
            Address address = premise.getAddress();
            propertes = new HashSet<PropertyBean>();

            if ((address != null) && (address.getStreet() != null)) {
                if (premise.getSuburb() != null) {
                    // create a new property bean entry
                    SuburbBean suburb = suburbEAO.lookupSuburb(PathHelper.buildPath(premise.getSuburb()));

                    if (suburb != null) {
                        PropertyBean property = new PropertyBean(suburb,
                                AddressHelper.unitNo(address),
                                AddressHelper.streetNo(address),
                                AddressHelper.streetName(address),
                                AddressHelper.streetType(address),
                                AddressHelper.streetSection(address),
                                premise);

                        PropertyAttributesHelper.copyAllAttributes(property, premise);
                        property.setLastEvent(premise.getLastEvent(new Date()));  // copy the last event

                        property.populateAttributes();
                        em.persist(property);

                        propertes.add(property);
                    } else {
                        LOG.warn("Attempted to create a PropertyBean for a Premise without a known Suburb");
                    }
                } else {
                    LOG.warn("Encountered a Premise not associated with a Suburb");
                }
            }
        } else {
            // modify the existing property bean entries...
            for (PropertyBean existingProperty : existing) {
                PropertyAttributesHelper.copyAllAttributes(existingProperty, premise);
                existingProperty.setLastEvent(premise.getLastEvent(new Date())); // copy the last event
                existingProperty.populateAttributes();
                
                em.persist(existingProperty);
            }

            propertes = existing;
        }

        return propertes;
    }

    public PropertyBean lookupProperty(String country, String state, String suburb, String street, String streetNo) {
        return propertyEAO.lookupProperty(PathHelper.buildPath(country, state, suburb, street, streetNo));
    }

    public PropertyBean lookupProperty(String country, String state, String suburb, String street, String streetNo, String unitNo) {
        return propertyEAO.lookupProperty(PathHelper.buildPath(country, state, suburb, street, streetNo, unitNo));
    }

    public PropertyBean lookupProperty(String path) {
        return propertyEAO.lookupProperty(path);
    }

    public Set<PropertyBean> lookupProperties(Premise premise) {
        return propertyEAO.lookupProperties(premise);
    }

    @Inject
    public void setPropertyEAO(PropertyEAO propertyEAO) {
        this.propertyEAO = propertyEAO;
    }

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Inject
    public void setSuburbEAO(SuburbEAO suburbEAO) {
        this.suburbEAO = suburbEAO;
    }
}
