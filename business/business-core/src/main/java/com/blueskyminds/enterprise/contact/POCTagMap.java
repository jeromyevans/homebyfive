package com.blueskyminds.enterprise.contact;

import com.blueskyminds.homebyfive.framework.framework.AbstractEntity;
import com.blueskyminds.enterprise.tag.TagMap;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.enterprise.tag.Taggable;

import javax.persistence.*;

/**
 * Maps a tag to a PointOfContact
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="POCTagMap")
public class POCTagMap extends AbstractEntity implements TagMap {

    private PointOfContact pointOfContact;
    private Tag tag;

    public POCTagMap(PointOfContact pointOfContact, Tag tag) {
        this.pointOfContact = pointOfContact;
        this.tag = tag;
    }

    /** Default constructor for ORM */
    public POCTagMap() {
    }

    @ManyToOne
    @JoinColumn(name="PointOfContactId")
    public PointOfContact getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(PointOfContact pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    @ManyToOne
    @JoinColumn(name="TagId")
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Transient
    public Taggable getTarget() {
        return pointOfContact;
    }
}
