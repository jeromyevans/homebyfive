package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.tag.TagMap;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * Maps a tag to a Party
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="PartyTagMap")
public class PartyTagMap extends AbstractEntity implements TagMap {

    private Party party;
    private Tag tag;

    public PartyTagMap(Party party, Tag tag) {
        this.party = party;
        this.tag = tag;
    }

    /** Default constructor for ORM*/
    public PartyTagMap() {
    }

    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
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
        return party;
    }
}
