package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.measurement.Area;
import java.util.Date;

/**
 * Date Started: 25/04/2008
 */
public interface PropertyAttributes {

    PropertyTypes getType();

    Integer getBedrooms();

    Integer getBathrooms();

    Integer getCarspaces();

    Integer getStoreys();

    Integer getNoOfUnits();

    Area getBuildingArea();

    Area getLandArea();

    Area getGaragesArea();

    Area getVerandaArea();

    Area getCommonBuildingArea();

    Date getConstructionDate();
    
    Object getAttributeOfType(PropertyAttributeTypes type);
}
