package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.tools.Named;

import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * The roles an individual may have when associated with another Party.
 *  Roles are not position titles.  An individual may have many roles.
 *
 * This is extendable reference data
 *
 * Date Created: 18 Aug 2006
 *
 * ---[ Blue Sky Minds Pty Ltd ]-----------------------------------------------------------------------------
 */
@Entity(name = "IndividualRole")
public class IndividualRole extends AbstractDomainObject implements Named {

    private String name;
    private String description; 

    public IndividualRole(String name) {
        this.name = name;
    }

    public IndividualRole(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected IndividualRole() {
    }

    @Basic
    @Column(name="Name")
    public String getName() {           
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name="Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
