package com.blueskyminds.enterprise.party;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;

/**
 * Maps an Individual's relationship with a Party to Roles
 *
 * Date Started: 3/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class IndividualRoleMap extends AbstractDomainObject {

    private IndividualRelationship relationship;
    private IndividualRole role;

    public IndividualRoleMap(IndividualRelationship relationship, IndividualRole role) {
        this.relationship = relationship;
        this.role = role;
    }

    protected IndividualRoleMap() {
    }

    @ManyToOne
    @JoinColumn(name = "RelationshipId")
    public IndividualRelationship getRelationship() {
        return relationship;
    }

    public void setRelationship(IndividualRelationship relationship) {
        this.relationship = relationship;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RoleId")
    public IndividualRole getRole() {
        return role;
    }

    public void setRole(IndividualRole role) {
        this.role = role;
    }
}
