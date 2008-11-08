package com.blueskyminds.homebyfive.business.tag;

/**
 * Reference to a Tag by its path
 *
 * Date Started: 21/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TagRef {

    private Long id;
    private String path;
    private String name;

    public TagRef(String basePath, Tag tag) {
        this(tag.getId(), basePath+tag.getName(), tag.getName());
    }

    public TagRef(Long id, String path, String name) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}