package com.blueskyminds.housepad.core.property.service;

import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.housepad.core.property.model.PropertyFormBean;
import com.blueskyminds.landmine.core.property.Premise;
import java.util.Set;

/**
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public interface PropertyService {

    Set<PropertyBean> listProperties(String country, String state, String suburb);

    PropertyBean createProperty(PropertyBean propertyBean) throws DuplicatePropertyException;

    /**
     * Create or modify a property entry from the current attributes of a Premise
     *
     * @param premise
     * @return
     */
    Set<PropertyBean> createOrUpdateProperties(Premise premise);

    PropertyBean lookupProperty(String country, String state, String suburb, String street, String streetNo);

    PropertyBean lookupProperty(String country, String state, String suburb, String street, String streetNo, String unitNo);

    PropertyBean lookupProperty(String path);

    /**
     * Lookup the PropertyBean for the Premise
     * @param premise
     * @return
     */
    Set<PropertyBean> lookupProperties(Premise premise);
}
