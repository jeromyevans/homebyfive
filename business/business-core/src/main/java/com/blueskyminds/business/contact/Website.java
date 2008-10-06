package com.blueskyminds.business.contact;

import javax.persistence.*;

/**
 * A website is a type of Point Of Contact
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Website")
public class Website extends PointOfContact {

    /** The URL for the website */
    private String url;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new website point of contact with the initial role specified */
    public Website(String url, POCRole initialRole) {
        super(initialRole);
        this.url = url;
    }

    /** Default constructor for ORM */
    protected Website() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getUrl()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return url;
    }

    /** The contextless value for this PointOfContact */
    @Transient
    public String getValue() {
        return url;
    }
}
