package com.blueskyminds.landmine.core.property.tag;

import com.blueskyminds.enterprise.tag.TagMap;
import com.blueskyminds.enterprise.tag.Taggable;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;

/**
 * Map a tag to a Premise
 *
 * Date Started: 2/05/2008
 */
@Entity
@Table(name="PremiseTagMap")
public class PremiseTagMap extends AbstractDomainObject implements TagMap {

    private Premise premise;
    private Tag tag;

    public PremiseTagMap(Premise premise, Tag tag) {
        this.premise = premise;
        this.tag = tag;
    }

    public PremiseTagMap() {
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @Transient
    public Taggable getTarget() {
        return premise;
    }

    @ManyToOne
    @JoinColumn(name="TagId")
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
