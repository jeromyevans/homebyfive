package com.blueskyminds.business.photo;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Date Started: 3/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
public class Photo extends AbstractEntity {

    private String thumbnailPath;
    private String path;
    private String name;

    private Set<PhotoTagMap> tagMap;

    @Basic
    @Column(name="ThumbnailPath", length=1024)
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Basic
    @Column(name="Path", length=1024)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    public Set<PhotoTagMap> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Set<PhotoTagMap> tagMap) {
        this.tagMap = tagMap;
    }
}

