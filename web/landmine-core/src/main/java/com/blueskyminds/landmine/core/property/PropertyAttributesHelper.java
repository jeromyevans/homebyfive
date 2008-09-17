package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.housepad.core.property.model.PropertyBean;

import javax.persistence.Transient;
import java.util.Date;

/**
 * Date Started: 30/04/2008
 */
public class PropertyAttributesHelper {

    /**
      * Returns the property attribute in the spec for the specified attribute type.
      *
      * @return The attribute instance, or null of it's not defined
      */
     public static Object getAttributeOfType(PropertyAttributes attributes, PropertyAttributeTypes type) {
        Object attributeValue = null;

        if (attributes != null) {
            switch (type) {
                case Bathrooms:
                    attributeValue = attributes.getBathrooms();
                    break;
                case Bedrooms:
                    attributeValue = attributes.getBedrooms();
                    break;
                case BuildingArea:
                    attributeValue = attributes.getBuildingArea();
                    break;
                case Carspaces:
                    attributeValue = attributes.getCarspaces();
                    break;
                case ConstructionDate:
                    attributeValue = attributes.getConstructionDate();
                    break;
                case LandArea:
                    attributeValue = attributes.getLandArea();
                    break;
                case PropertyType:
                    attributeValue = attributes.getType();
                    break;
                case Storeys:
                    attributeValue = attributes.getStoreys();
                    break;
                case CommonBuildingArea:
                    attributeValue = attributes.getCommonBuildingArea();
                    break;
                case GaragesArea:
                    attributeValue = attributes.getGaragesArea();
                    break;
                case NoOfUnits:
                    attributeValue = attributes.getNoOfUnits();
                    break;
                case VerandaArea:
                    attributeValue = attributes.getVerandaArea();
                    break;
            }
        }
        return attributeValue;
    }

    /**
      * Sets the property attribute in the bean for the specified attribute type.
      */
    public static void setAttributeOfType(PropertyBean attributes, PropertyAttributeTypes type, Object value) {
        if (attributes != null) {
            switch (type) {
                case Bathrooms:
                    attributes.setBathrooms((Integer) value);
                    break;
                case Bedrooms:
                    attributes.setBedrooms((Integer) value);
                    break;
                case BuildingArea:
                    attributes.setBuildingArea((Area) value);
                    break;
                case Carspaces:
                    attributes.setCarspaces((Integer) value);
                    break;
                case ConstructionDate:
                    attributes.setConstructionDate((Date) value);
                    break;
                case LandArea:
                    attributes.setLandArea((Area) value);
                    break;
                case PropertyType:
                    attributes.setType((PropertyTypes) value);
                    break;
                case Storeys:
                    attributes.setStoreys((Integer) value);
                    break;
                case CommonBuildingArea:
                    attributes.setCommonBuildingArea((Area) value);
                    break;
                case GaragesArea:
                    attributes.setCommonBuildingArea((Area) value);
                    break;
                case NoOfUnits:
                    attributes.setNoOfUnits((Integer) value);
                    break;
                case VerandaArea:
                    attributes.setVerandaArea((Area) value);
                    break;
            }
        }
    }

    /**
     * Copy attributes from the source to the target bean, overwriting all existing values.
     *
     * @param target
     * @param source
     */
    public static void copyAllAttributes(PropertyBean target, PropertyAttributes source) {
        if (target != null && source != null) {
            for (PropertyAttributeTypes type : PropertyAttributeTypes.values()) {
                setAttributeOfType(target, type, source.getAttributeOfType(type));
            }
        }
    }
}
