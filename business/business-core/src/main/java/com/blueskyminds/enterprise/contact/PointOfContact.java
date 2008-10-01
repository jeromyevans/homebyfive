package com.blueskyminds.enterprise.contact;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.enterprise.tag.Taggable;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.enterprise.tag.TagTools;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A point of contact is an abstract way to communicate with or address a party
 *
 * Implementations include email addresses, street addresses, phone numbers etc.
 *
 * The PointOfContact always has at least one role. eg. A business phone number
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("POC")
public abstract class PointOfContact extends AbstractDomainObject implements Taggable, ContactValue {

    /**
     * The roles of this point of contact.
     */
    private Set<POCRoleMap> roleMaps;

    /**
     * Tags assigned to this point of contact
     */
    private Set<POCTagMap> tagMaps;

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a point of contact with the default values */
    public PointOfContact(POCRole initialRole) {
        init();
        addRole(initialRole);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected PointOfContact() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PointOfContact with default values
     */
    private void init() {
        roleMaps = new HashSet<POCRoleMap>();
        tagMaps = new HashSet<POCTagMap>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Add another role for this point of contact
     * @param role
     * @return true if added
     */
    public boolean addRole(POCRole role) {
        if (!hasRole(role)) {
            return roleMaps.add(new POCRoleMap(this, role));
        } else {
            return false;
        }
    }

    /** Determine if this PointOfContact has the specified role */
    public boolean hasRole(POCRole role) {
        boolean found = false;
        for (POCRoleMap map : roleMaps) {
            if (map.getContactRole().equals(role)) {
                found = true;
                break;
            }
        }
        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the roles of this point of contact
     */
    @OneToMany(mappedBy = "pointOfContact", cascade = CascadeType.ALL)
    protected Set<POCRoleMap> getRoleMaps() {
        return roleMaps;
    }

    protected void setRoleMaps(Set<POCRoleMap> roleMaps) {
        this.roleMaps = roleMaps;
    }

    @Transient
    public Set<POCRole> getContactRoles() {
        Set<POCRole> roles = new HashSet<POCRole>();
        for (POCRoleMap map : roleMaps) {
            roles.add(map.getContactRole());
        }
        return roles;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "pointOfContact", cascade = CascadeType.ALL)
    public Set<POCTagMap> getTagMaps() {
        return tagMaps;
    }

    public void setTagMaps(Set<POCTagMap> tagMaps) {
        this.tagMaps = tagMaps;
    }

    @Transient
    public Set<Tag> getTags() {
        return TagTools.extractTags(tagMaps);
    }

    public void addTag(Tag tag) {        
        if (!TagTools.contains(tagMaps, tag)) {
            tagMaps.add(new POCTagMap(this, tag));
        }
    }

    /**
     * Determine if this PointOfContact has the specified tag
     *
     * @param tagName
     * @return
     */
    public boolean hasTag(String tagName) {
        return TagTools.contains(tagMaps, tagName);
    }

    /**
     * Determine if this PointOfContact has the specified tag
     *
     * @param tag
     * @return
     */
    public boolean hasTag(Tag tag) {
        return TagTools.contains(tagMaps, tag);
    }


    /** Add some tags to this PointOfContact */
    public Taggable withTags(Tag[] tags) {
        if (tags != null) {
            for (Tag tag : tags) {
                addTag(tag);
            }
        }
        return this;
    }

    /** Add a tag to this PointOfContact */
    public Taggable withTag(Tag tag) {
        if (tag != null) {
            addTag(tag);
        }
        return this;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
