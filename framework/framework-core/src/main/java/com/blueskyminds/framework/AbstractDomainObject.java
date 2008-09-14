package com.blueskyminds.framework;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.FastHashMap;

import javax.persistence.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.io.PrintStream;

import com.blueskyminds.framework.persistence.jpa.EntityEventCallback;
import com.blueskyminds.framework.reflection.MergeablePropertyFilter;
import com.blueskyminds.framework.reflection.PropertyDescriptor;
import com.blueskyminds.framework.tools.ReflectionTools;

/**
 * AbstractDomainObject is a layer supertype for the busines domain objects.
 * In addition to the standard persistence mechanisms, the DomainObject's have dateCreated, lastUpdates and
 * status properties and support merging.
 *
 * Date Started: 5/05/2006
 *
 * History:
 *    6 Jan 2007 - added EntityEventCallback
 *               - added LastUpdated property callback
 *   14 Jan 2007 - overridden equals and hashcode to perform identity checking.
 *     NOTE: the id is used to generate the hashcode.  As the id is only assigned once the DomainObject has been
 *  persisted its possible for the hashcode to change.  If the DomainObject is used an a HashSet prior to being
 *  persisted the HashSet contract is broken.  This means the usual methods (contains, remove etc) will no longer
 *  locate the DomainObject in the hashset - take care.
 *   17 Jun 2007 - the id related fields have been moved out into the AbstractEntity class so we can have
 *  persistent objects with an Id but none of the other attributes of a DomainObject
 *
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@MappedSuperclass
@EntityListeners(EntityEventCallback.class)
public abstract class AbstractDomainObject extends AbstractEntity implements DomainObject {

    /** The status of this DomainObject */
    private DomainObjectStatus status;

    /** The date/time this object was first committed */
    private Date dateCreated;

    /** The date/time this object was last updated (committed to persistence) */
    private Date lastUpdated;

    /**
     *  A cache of the PropertyDescriptors for each subclass of AbstractDomainObject.
     *
     * The cache is populated when the first instance of each subclass is instantiated.  It's used for faster access
     *  to the properties by name.
     *
     */
    //private static FastHashMap propertyDescriptorCache = new FastHashMap();

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractDomainObject with default attributes
     */
    public AbstractDomainObject() {
        this.status = DomainObjectStatus.Valid;  // default status - may be overridden
//        inspectProperties();
        calculateDateCreated();                  // set the dateCreated and lastUpdated to now
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the status of this AbstractDomainObject
     * @return the status
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name="Status")
    @IgnoreMerge
    public DomainObjectStatus getStatus() {
        return status;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Set the status of this AbstractDomainObject
     * @param status
     */
    public void setStatus(DomainObjectStatus status) {
        this.status = status;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the date this object was first created
     *
     * If the value is null then the object has not been persisted
     *
     * @return the timestamp set when the instace first created
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateCreated")
    @IgnoreMerge
    public Date getDateCreated() {
        return dateCreated;
    }

    protected void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * A call-back method used to record the creation date for the entity
     */
    //@PrePersist                   // this prevents the dateCreated from being used prior to persistence
    private void calculateDateCreated() {
        if (dateCreated == null) {
            setDateCreated(new Date());
            setLastUpdated(getDateCreated());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the timestamp this object was last updated (committed to persistence)
     * @return the timestamp set when the instance was last committed
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LastUpdated")
    @IgnoreMerge
    public Date getLastUpdated() {
        return lastUpdated;
    }

    protected void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * A call-back method record the last updated timestamp for the entity
     */
    @PreUpdate
    private void calculateLastUpdate() {
        setLastUpdated(new Date());
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return true if the status of this domain object is 'Deleted' */
    @Transient
    public boolean isDeleted() {
        return (DomainObjectStatus.Deleted.equals(status));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return true if the status of this domain object is 'Valid' */
    @Transient
    public boolean isValid() {
        return (DomainObjectStatus.Valid.equals(status));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the value for the named property in this domain object, converted to a string.
     *
     * @param name
     * @return the property value, or null if it's not recognised.
     */
    @Transient
    public String getProperty(String name) {
        String value = null;

        try {
            value = BeanUtils.getSimpleProperty(this, name);
        } catch(IllegalAccessException e) {
            //
        } catch(InvocationTargetException e) {
            //
        } catch(NoSuchMethodException e) {
            //
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------



    /**
     *  A DomainObject is said to be persistent if it has an Id
     *
     * @return true if the DomainObject has an identity assigned */
    @Transient
    public boolean isPersistent() {
        return (getId() != null);
    }

    /**
     * Manages a cache of the properties in this (sub)class for fast retrieval
     *
     * If this (sub)class has already had its properties cached the method exits immediately
     */
/*    private void inspectProperties() {
        if (!propertyDescriptorCache.containsKey(getClass())) {
            // lookup the properties in this subclass and cache them for later retrieval
            List<PropertyDescriptor> propertyDescriptors = PropertyDescriptor.getProperties(getClass(), mergeablePropertyFilter);
            propertyDescriptorCache.put(getClass(), propertyDescriptors);
        }
    }     */

    /**
     * Merges the simple properties of the other DomainObject into this DomainObject
     *
     * Simple properties are those that are not collections, are not transient and do not carry the
     *  IgnoreMerge annotation
     *
     * A merge is performed only if this DomainObject has a null value for the property.
     * For an marge that overwrites existing properties see {@link AbstractDomainObject#updateSimpleProperties}
     *
     * @param other
     */
    @SuppressWarnings({"unchecked"})
    protected final void mergeSimpleProperties(DomainObject other) {
        ReflectionTools.mergeSimpleProperties(this, other);
    }

    /**
     * Merge the properties of another DomainObject into this DomainObject.
     *
     * The default implementation throws {@link MergeUnsupportedException}.
     *
     * An implementation should copy the non-null values of the other object into this object where the
     *  value does not already exist.
     *
     * @param other     the object to extract properties from into this object
     * @throws MergeUnsupportedException    when this domain object hasn't implemented the operation
     * @see AbstractDomainObject#updateFrom(DomainObject)
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        throw new MergeUnsupportedException();
    }

    /**
     * Merges the simple properties of the other DomainObject into this DomainObject
     *
     * Simple properties are those that are not collections, are not transient and do not carry the
     *  IgnoreMerge annotation
     *
     * Null properties of the other DomainObject are ignored.  Existing values of this domain object are overridden.
     *
     * @param other
     * @see AbstractDomainObject#mergeSimpleProperties
     */
    @SuppressWarnings({"unchecked"})
    protected final void updateSimpleProperties(DomainObject other) {
        ReflectionTools.updateSimpleProperties(this, other);
    }

    /**
     * Merge the properties of another DomainObject into this DomainObject.
     *
     * The default implementation throws {@link MergeUnsupportedException}.
     *
     * An implementation should copy the non-null values of the other object into this object overriding existing
     * values except those values that form the identity and/or date created/last updated.
     *
     * @param other     the object to extract properties from into this object
     * @throws MergeUnsupportedException    when this domain object hasn't implemented the operation
     * @see AbstractDomainObject#mergeWith(DomainObject)
     */
    public <T extends DomainObject> void updateFrom(T other) throws MergeUnsupportedException {
        throw new MergeUnsupportedException();
    }

    @Override
    public void print(PrintStream out) {
        super.print(out);
    }
}
