package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.framework.core.TypedManyRelationship;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.tools.Named;

import javax.persistence.*;
import java.util.*;

/**
 * The relationship between an individual with another party
 *
 * This is typed relationship
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Table(name="IndividualRelationship")
public class IndividualRelationship extends AbstractDomainObject implements TypedManyRelationship<Party, Individual, IndividualRoleMap>, Named {

    /** The party */
    private Party party;

    /** The referenced individual */
    private Individual individual;

    /** The roleMaps of the individual with the party */
    private Set<IndividualRoleMap> roleMaps;

    /** The name of the individual at the party, if applicable (eg. a title) */
    private String name;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a relationship (with roleMaps) between a Party and an individual
     *
     * @param party
     * @param individual
     * @param title Optional Title given to the individual with associated with the party
     * @param initialRole initial role given to the individual (others may be included)
     */
    public IndividualRelationship(Party party, Individual individual, String title, IndividualRole initialRole) {
        init(party, individual);
        this.name = title;
        roleMaps.add(new IndividualRoleMap(this, initialRole));
    }

    /** Default constructor for ORM */
    protected IndividualRelationship() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the relationship with the default attributes
     */
    private void init(Party party, Individual individual) {
        roleMaps = new HashSet<IndividualRoleMap>();
        this.party = party;
        this.individual = individual;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public Party getPrimary() {
        return party;
    }

    protected void setPrimary(Party party) {
        this.party = party;
    }

    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = com.blueskyminds.homebyfive.business.party.Individual.class)
    @JoinColumn(name="IndividualId")
    public Individual getReference() {
        return individual;
    }

    protected void setReference(Individual individual) {
        this.individual = individual;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the roleMaps of the individual at the party */
    @OneToMany(mappedBy = "relationship", cascade = CascadeType.ALL)
    public Set<IndividualRoleMap> getRoleMaps() {
        return roleMaps;
    }

    protected void setRoleMaps(Set<IndividualRoleMap> roleMaps) {
        this.roleMaps = roleMaps;
    }

    @Transient
    public Set<IndividualRole> getRoles() {
        Set<IndividualRole> roles = new HashSet<IndividualRole>();
        for (IndividualRoleMap map : roleMaps) {
            roles.add(map.getRole());
        }
        return roles;
    }

    public boolean hasRole(IndividualRole role) {
        return getRoles().contains(role);
    }

    public boolean addRole(IndividualRole role) {
        if (!hasRole(role)) {
            return roleMaps.add(new IndividualRoleMap(this, role));
        } else {
            return false;
        }
    }

    /**
     * Add the specified roles to this relationship if they don't already exist
     *
     * @return true if and roles were added
     */

    public boolean addRoles(Collection<IndividualRole> roles) {
        boolean added = false;
        for (IndividualRole newRole : roles) {
            if (!hasRole(newRole)) {
                if (addRole(newRole)) {
                    added = true;
                }
            }
        }
        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the relationship */
    @Basic
    @Column(name="Name", nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
