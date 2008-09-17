package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.MergeUnsupportedException;
import com.blueskyminds.framework.tools.ReflectionTools;
import com.blueskyminds.framework.measurement.Area;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

/**
 * The specification of attributes and features for a property.
 *
 * Contains a list of attributes/features for the property and the date that they are 'applied' to the property
 *
 * A AttributeSet can be created without being associated with a property.  Association can be performed later.
 * The AttributeSet can only be associated with a single property however.
 *
 * A property may have multiple feature specifications - usually at different times or from different
 * sources for the same property.
 *
 * NOTE: In the current implementation, the AttributeSet has a finite set of properties.
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class PremiseAttributeSet extends AbstractDomainObject implements PropertyAttributes {

    private Date dateApplied;

    /** Optional - the premise this attributeSet has been associated with */
    private Premise premise;

    private Integer bedrooms;
    private Integer bathrooms;
    private Area buildingArea;
    private Area garagesArea;
    private Area verandaArea;
    private Area commonBuildingArea;
    private Area landArea;
    private PropertyTypes type;
    private Integer carspaces;
    private Date constructionDate;
    private Integer storeys;
    private Integer noOfUnits;
    
    private String sourceType;
    private Long sourceId;

    // ------------------------------------------------------------------------------------------------------

    public PremiseAttributeSet(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public PremiseAttributeSet(Date dateApplied, PropertyTypes type, Integer bedrooms, Integer bathrooms, Integer carspaces, Area buildingArea, Area landArea, Date constructionDate) {
        this.dateApplied = dateApplied;
        this.bedrooms = bedrooms;
        this.buildingArea = buildingArea;
        this.bathrooms = bathrooms;
        this.landArea = landArea;
        this.type = type;
        this.carspaces = carspaces;
        this.constructionDate = constructionDate;
    }

    public PremiseAttributeSet(Date dateApplied, PropertyTypes type, String sourceType, Long sourceId) {
        this.dateApplied = dateApplied;
        this.type = type;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
    }


    /** Default constructor for ORM */
    protected PremiseAttributeSet() {
    }


    // ------------------------------------------------------------------------------------------------------

    /** Set if this AttributeSet has been associated with a property */
    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateApplied")
    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the number of bedrooms specified in this AttributeSet.
     *
     * @return The number of bedrooms, or null if not defined in this spec
     */
    @Basic
    @Column(name="NoOfBedrooms")
    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer noOfBedrooms) {
        this.bedrooms = noOfBedrooms;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the number of bathrooms specified in this AttributeSet.
     *
     * @return The number of bathrooms, or null if not defined in this spec
     */
    @Basic
    @Column(name="NoOfBathrooms")
    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer noOfBathrooms) {
        this.bathrooms = noOfBathrooms;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the PropertyType specified in this AttributeSet.
     *
     * @return The property type, or null if not defined in this spec
     */
    @Enumerated
    @Column(name="PropertyType")
    public PropertyTypes getType() {
        return type;
    }

    public void setType(PropertyTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Get the land area included in this attribute set
     *
     * @return The land area, or null if not defined in this spec
     */
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

    // ------------------------------------------------------------------------------------------------------


    /**
     * Get the building area specified in this AttributeSet.
     *
     * @return The building area, or null if not defined in this spec
     */
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

    /**
     * Get the number of carspaces specified in the attribute set
     * @return
     */
    @Basic
    @Column(name="NoOfCarspaces")
    public Integer getCarspaces() {
        return carspaces;
    }

    public void setCarspaces(Integer noOfCarspaces) {
        this.carspaces = noOfCarspaces;
    }

    @Temporal(TemporalType.DATE)
    public Date getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(Date constructionDate) {
        this.constructionDate = constructionDate;
    }

    /**
     * Get the number of storeys specified in the attribute set
     * @return
     */
    @Basic
    @Column(name="NoOfStoreys")
    public Integer getStoreys() {
        return storeys;
    }

    public void setStoreys(Integer storeys) {
        this.storeys = storeys;
    }

    /**
     * This attribute only applies for complexes
     * @return
     */
    @Enumerated
    @Column(name="NoOfUnits")
    public Integer getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Integer noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- "+getIdentityName()+" ---");
        try {
            Map map = BeanUtils.describe(this);
            for (Object entry : map.entrySet()) {
                out.println("   "+((Map.Entry) entry).getKey()+": "+((Map.Entry) entry).getValue());
            }
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InvocationTargetException e) {
            //ignored
        } catch (NoSuchMethodException e) {
            //ignored
        }
    }

    @Basic
    @Column(name="SourceType")
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Basic
    @Column(name="SourceId")
    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Return a new instance of an AttributeSet equivalent to this one, but without an identity and
     * no Premise reference
     * */
    public PremiseAttributeSet duplicate() {
        PremiseAttributeSet newAttributeSet = new PremiseAttributeSet(dateApplied);

        newAttributeSet.setBathrooms(bathrooms);
        newAttributeSet.setBedrooms(bedrooms);
        newAttributeSet.setBuildingArea(buildingArea);
        newAttributeSet.setCommonBuildingArea(commonBuildingArea);
        newAttributeSet.setVerandaArea(verandaArea);
        newAttributeSet.setGaragesArea(garagesArea);
        newAttributeSet.setLandArea(landArea);
        newAttributeSet.setType(type);
        newAttributeSet.setCarspaces(carspaces);
        newAttributeSet.setStoreys(storeys);
        newAttributeSet.setNoOfUnits(noOfUnits);
        newAttributeSet.setConstructionDate(constructionDate);
        newAttributeSet.setSourceType(sourceType);
        newAttributeSet.setSourceId(sourceId);

        return newAttributeSet;
    }

    /**
      * Returns the property attribute in this spec for the specified attribute class.
      *
      * @return The attribute instance, or null of it's not defined
      */
     @Transient
     public Object getAttributeOfType(PropertyAttributeTypes type) {
        return PropertyAttributesHelper.getAttributeOfType(this, type);
     }

    /**
     * Update the properties in this set from the other set (non-null values in the other set are applied)
     * @param other
     * @throws MergeUnsupportedException
     */
    public <T extends DomainObject> void updateFrom(T other) throws MergeUnsupportedException {
        if (other instanceof PremiseAttributeSet) {
            ReflectionTools.updateSimpleProperties(this, other);
        }
    }

    /**
     * Merge the properties in the other set into this set (null values in this set are updated)
     *
     * @param other
     * @throws MergeUnsupportedException
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        if (other instanceof PremiseAttributeSet) {
            ReflectionTools.mergeSimpleProperties(this, other);
        }
    }

    // ------------------------------------------------------------------------------------------------------

}
