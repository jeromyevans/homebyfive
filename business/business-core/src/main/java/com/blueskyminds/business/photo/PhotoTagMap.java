package com.blueskyminds.business.photo;

import com.blueskyminds.business.tag.Tag;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * Date Started: 3/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
public class PhotoTagMap extends AbstractEntity {

    private Photo photo;

    private Integer xOffset;
    private Integer yOffset;

    private Tag tag;

    @ManyToOne
    @JoinColumn(name="PhotoId")
    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @Basic
    @Column(name="XOffset")
    public Integer getXOffset() {
        return xOffset;
    }

    public void setXOffset(Integer xOffset) {
        this.xOffset = xOffset;
    }

    @Basic
    @Column(name="YOffset")
    public Integer getYOffset() {
        return yOffset;
    }

    public void setYOffset(Integer yOffset) {
        this.yOffset = yOffset;
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

