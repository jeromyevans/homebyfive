package com.blueskyminds.enterprise.party;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.PrintStream;

/**
 * A special case party representing a system.  It's only property is it's name.
 *
 * Date Started: 1/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("System")
public class SystemParty extends Party {

    private String name;

    /** Create a new party that is a System */
    public SystemParty(String name) {
        super(PartyTypes.System);
        this.name = name;
    }

    /** Default constructor for ORM */
    public SystemParty() {
        super(PartyTypes.System);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the name of the company
     * @return name
     */
    @Basic
    public String getName() {
        return name;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * set the name of the company
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getName()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("Name:"+getName());
        super.print(out);
    }

    // ------------------------------------------------------------------------------------------------------

}
