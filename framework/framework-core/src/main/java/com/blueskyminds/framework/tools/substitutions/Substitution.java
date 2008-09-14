package com.blueskyminds.framework.tools.substitutions;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.MergeUnsupportedException;

import javax.persistence.*;

/**
 * A class used to store a set of patterns and substitutions
 *
 * Date Started: 12/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
public class Substitution extends AbstractDomainObject {

    private String groupName;

    private String description;

    private String pattern;

    private String substitution;

    /** Whether the pattern matches a phrases exclusively, or if the phrase can be reused for other patterns matchers */
    private Boolean exclusive;

    /** Can be used to reference a group in the pattern if it's a regular expression */
    private Integer groupNo;

    /** The metadata can be used to reference another object or provide an additional value depending on the context*/
    private String metadata;

    // ------------------------------------------------------------------------------------------------------
    
    public Substitution(String groupName, String description, String pattern, String substitution, boolean exclusive, int groupNo, String metadata) {
        this.groupName = groupName;
        this.description = description;
        this.pattern = pattern;
        this.substitution = substitution;
        this.exclusive = exclusive;
        this.groupNo = groupNo;
        this.metadata = metadata;
    }

    public Substitution() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the group that this substitution belongs to */
    @Basic
    @Column(name="GroupName")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

        // ------------------------------------------------------------------------------------------------------

    /** Human-readable description of this pattern/substitution */
    @Basic
    @Column(name="Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The pattern detected for the substitution */
    @Basic
    @Column(name="Pattern")
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The optional value used as a substitution for the pattern */
    @Basic
    @Column(name="Substitution")
    public String getSubstitution() {
        return substitution;
    }

    public void setSubstitution(String substitution) {
        this.substitution = substitution;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Exclusive")
    public Boolean getExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Transient
    public boolean isExclusive() {
        if (exclusive != null) {
            return exclusive;
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="GroupNo")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Optional metadata to associate with the substitution */
    @Basic
    @Column(name="Metadata")
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Merge the properties of the other substitution into this one (excluding id), overwriting existing values
     *
     * @param other
     * @throws MergeUnsupportedException
     */
    public <T extends DomainObject> void updateFrom(T other) throws MergeUnsupportedException {
        updateSimpleProperties(other);
    }
}
