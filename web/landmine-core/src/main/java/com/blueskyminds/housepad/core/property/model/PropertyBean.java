package com.blueskyminds.housepad.core.property.model;

import com.blueskyminds.framework.AbstractEntity;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.model.PostCodeBean;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.address.StreetSection;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.tools.KeyGenerator;
import com.blueskyminds.landmine.core.property.*;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.blueskyminds.landmine.core.property.attribute.BuildingArea;
import com.blueskyminds.landmine.core.property.attribute.LandArea;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * A simplified view of a premise
 *
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="hpProperty")
public class PropertyBean extends AbstractDomainObject implements PropertyAttributes {

    private String suburbPath;
    private SuburbBean suburbBean;
    private String postCodePath;
    private String streetPath;
    private String path;
    private String unitNo;
    private String streetNo;
    private String streetName;
    private StreetType streetType;
    private StreetSection streetSection;
    private String name;

    private Integer bedrooms;
    private Integer bathrooms;
    private Integer carspaces;
    private Integer storeys;
    private Integer noOfUnits;
    private PropertyTypes type;
    private Area buildingArea;
    private Area garagesArea;
    private Area verandaArea;
    private Area commonBuildingArea;
    private Area landArea;
    private Date constructionDate;
    private Premise premise;

    private Date eventDate;
    private String eventPath;
    private String eventType;
    private String eventDescription;
    private PremiseEvent lastEvent;

    public PropertyBean(SuburbBean suburbBean, String unitNo, String streetNo, String streetName, StreetType streetType, StreetSection streetSection, String name, Integer bedrooms, Integer bathrooms, PropertyTypes type, Premise premise) {
        this.suburbBean = suburbBean;
        this.unitNo = unitNo;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.streetType = streetType;
        this.streetSection = streetSection;
        this.name = name;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.type = type;
        this.premise = premise;
        populateAttributes();
    }

    public PropertyBean(SuburbBean suburbBean, String unitNo, String streetNo, String streetName, StreetType streetType, StreetSection streetSection, Premise premise) {
        this.suburbBean = suburbBean;
        this.unitNo = unitNo;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.streetType = streetType;
        this.streetSection = streetSection;
        this.premise = premise;
        populateAttributes();
    }

    public PropertyBean(SuburbBean suburbBean) {
        this.suburbBean = suburbBean;
        populateAttributes();
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.suburbPath = suburbBean.getPath();
        PostCodeBean postCodeBean = suburbBean.getPostCodeBean();
        if (postCodeBean != null) {
            this.postCodePath = postCodeBean.getPath();
        }
        String streetKey = PathHelper.buildStreetNameKey(streetName, streetType, streetSection);
        this.path = PathHelper.joinPaths(suburbPath, streetKey, KeyGenerator.generateId(streetNo), KeyGenerator.generateId(unitNo));
        this.streetPath = PathHelper.joinPaths(suburbPath, streetKey);
        if (lastEvent != null) {
            this.eventPath = PathHelper.buildPath(path, "events");
            this.eventType = lastEvent.getClass().getSimpleName();
            this.eventDate = lastEvent.getDateApplied();
            this.eventDescription = lastEvent.getDescription();
        } else {
            this.eventPath = null;
            this.eventDate = null;
            this.eventType = null;
            this.eventDescription = null;
        }
    }

    
    public PropertyBean() {
    }

    @ManyToOne
    @JoinColumn(name="SuburbBeanId")
    public SuburbBean getSuburbBean() {
        return suburbBean;
    }

    public void setSuburbBean(SuburbBean suburbBean) {
        this.suburbBean = suburbBean;
    }

    public String getSuburbPath() {
        return suburbPath;
    }

    public void setSuburbPath(String suburbPath) {
        this.suburbPath = suburbPath;
    }

    public String getPostCodePath() {
        return postCodePath;
    }

    public void setPostCodePath(String postCodePath) {
        this.postCodePath = postCodePath;
    }

    public String getStreetPath() {
        return streetPath;
    }

    public void setStreetPath(String streetPath) {
        this.streetPath = streetPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Enumerated
    public StreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    /** Name of the property (eg. a building name) */
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated
    public StreetSection getStreetSection() {
        return streetSection;
    }

    public void setStreetSection(StreetSection streetSection) {
        this.streetSection = streetSection;
    }

    @Basic
    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    @Basic
    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    @Basic
    public Integer getStoreys() {
        return storeys;
    }    

    public void setStoreys(Integer storeys) {
        this.storeys = storeys;
    }

    @Basic
    public Integer getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Integer noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    @Basic
    @Column(name="Carspaces")
    public Integer getCarspaces() {
        return carspaces;
    }

    public void setCarspaces(Integer carspaces) {
        this.carspaces = carspaces;
    }

    @Enumerated
    public PropertyTypes getType() {
        return type;
    }

    public void setType(PropertyTypes type) {
        this.type = type;
    }

    @Embedded
    @AttributeOverrides( {
        @AttributeOverride(name = "amount", column = @Column(name="BuildingArea")),
        @AttributeOverride(name = "units", column = @Column(name="BuildingAreaUnits"))
    })
    public Area getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(Area buildingArea) {
        this.buildingArea = buildingArea;
    }

     /**
    * Get the building area component that is for garages
    *
    * @return The land area, or null if not defined in this spec
    */
   @Embedded
   @AttributeOverrides( {
       @AttributeOverride(name = "amount", column = @Column(name="GaragesArea")),
       @AttributeOverride(name = "units", column = @Column(name="GaragesAreaUnits"))
   })
    public Area getGaragesArea() {
        return garagesArea;
    }

    public void setGaragesArea(Area garagesArea) {
        this.garagesArea = garagesArea;
    }

    /**
    * Get the building area component that is for verandahs
    *
    * @return The land area, or null if not defined in this spec
    */
   @Embedded
   @AttributeOverrides( {
       @AttributeOverride(name = "amount", column = @Column(name="VerandaArea")),
       @AttributeOverride(name = "units", column = @Column(name="VerandaAreaUnits"))
   })
    public Area getVerandaArea() {
        return verandaArea;
    }

    public void setVerandaArea(Area verandaArea) {
        this.verandaArea = verandaArea;
    }

    /**
    * Get the common building area for the complex
    *
    * @return The land area, or null if not defined in this spec
    */
   @Embedded
   @AttributeOverrides( {
       @AttributeOverride(name = "amount", column = @Column(name="CommonBuildingArea")),
       @AttributeOverride(name = "units", column = @Column(name="CommonBuildingAreaUnits"))
   })
    public Area getCommonBuildingArea() {
        return commonBuildingArea;
    }

    public void setCommonBuildingArea(Area commonBuildingArea) {
        this.commonBuildingArea = commonBuildingArea;
    }

    @Embedded
    @AttributeOverrides( {
        @AttributeOverride(name = "amount", column = @Column(name="LandArea")),
        @AttributeOverride(name = "units", column = @Column(name="LandAreaUnits"))
    })
    public Area getLandArea() {
        return landArea;
    }

    public void setLandArea(Area landArea) {
        this.landArea = landArea;
    }

    @Temporal(TemporalType.DATE)
    public Date getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(Date constructionDate) {
        this.constructionDate = constructionDate;
    }

    /**
     * The premise that this PropertyBean was created from
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }


    @Transient
    public String getNumber() {
        if (unitNo != null) {
            if (streetNo != null) {
                return unitNo + "/"+streetNo;
            } else {
                return "Unit "+unitNo;
            }
        } else {
            return streetNo;
        }
    }

    /** Gets a full displayable name for this street (eg.  North Street) */
    @Transient
    public String getStreetFullName() {
        List<String> parts = new LinkedList<String>();
        StringBuilder result = new StringBuilder();
        if (StreetType.The.equals(streetType)) {
            parts.add(type.name());
        }
        parts.add(streetName);
        if (streetType != null) {
            if (!StreetType.The.equals(streetType)) {
                parts.add(streetType.name());
            }
        }
        if ((streetSection != null) && (StreetSection.NA != streetSection)) {
            parts.add(streetSection.name());
        }
        return StringUtils.join(parts.iterator(), " ");
    }

    /**
      * Returns the property attribute in this bean for the specified attribute type.
      *
      * @return The attribute value, or null of it's not defined
      */
     @Transient
     public Object getAttributeOfType(PropertyAttributeTypes type) {
        return PropertyAttributesHelper.getAttributeOfType(this, type);
    }

    @Transient
    public String getSuburbName() {
        return suburbBean.getName();
    }

    @Transient
    public String getStateName() {
        return suburbBean.getStateBean().getName();
    }

    @Transient
    public String getCountryName() {
        return suburbBean.getStateBean().getCountryBean().getName();
    }

    @Transient
    public String getPostCodeName() {
        PostCodeBean pcBean = suburbBean.getPostCodeBean();
        if (pcBean != null) {
            return pcBean.getName();
        } else {
            return null;
        }
    }

    @Temporal(TemporalType.DATE)
    @Column(name="EventDate")
    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Basic
    @Column(name="EventType")
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Basic
    @Column(name="EventPath")
    public String getEventPath() {
        return eventPath;
    }

    public void setEventPath(String eventPath) {
        this.eventPath = eventPath;
    }    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PremiseEventId")
    public PremiseEvent getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(PremiseEvent lastEvent) {
        this.lastEvent = lastEvent;
    }

    @Basic
    @Column(name="EventDescription")
    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
