package com.blueskyminds.landmine.core.property.attribute;

import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *  An attribute for a property identifying it's building area
 *
 * Date Started: 10/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class BuildingArea extends PropertyAttribute<Area> {

    private Area buildingArea;

    // ------------------------------------------------------------------------------------------------------

    public BuildingArea(Area buildingArea) {
        this.buildingArea = buildingArea;
    }

    public BuildingArea(BigDecimal amount, UnitsOfArea unitsOfArea) {
        this.buildingArea = new Area(amount, unitsOfArea);
    }

    /** Default constructor for ORM */
    protected BuildingArea() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Area getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(Area buildingArea) {
        this.buildingArea = buildingArea;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the land area value */
    public Area value() {
        return getBuildingArea();
    }

    private Area buildingArea() {
        if (buildingArea == null) {
            buildingArea =  Area.newInstance();
        }
        return buildingArea;
    }

    @Transient
    public BigDecimal getAmount() {
        return buildingArea.getAmount();
    }

    public void setAmount(BigDecimal amount) {
        buildingArea().setAmount(amount);
    }

    @Transient
    public UnitsOfArea getUnits() {
        return buildingArea.getUnits();
    }

    public void setUnits(UnitsOfArea units) {
        buildingArea().setUnits(units);
    }

    // ------------------------------------------------------------------------------------------------------
}