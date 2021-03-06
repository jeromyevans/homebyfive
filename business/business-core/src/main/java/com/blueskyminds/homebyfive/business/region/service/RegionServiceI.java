package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpression;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface RegionServiceI<T extends Region> {

    RegionGroup list(String path);

    T lookup(String path);

    T create(T region) throws DuplicateRegionException, InvalidRegionException;

    /**
     * Update an existing Region
     * Propagates the change into the RegionGraph as well
     * @param path         current path
     * @param suburb
     */
    T update(String path, T suburb) throws InvalidRegionException;

    void delete(String id);

    TableModel listTags(String path);

    void assignTag(String path, String tag) throws InvalidRegionException;

    void removeTag(String path, String tag) throws InvalidRegionException;

    Set<T> listByTag(String tagName);

    /** List regions matching the TagExpression */
    Set<T> listByTags(TagExpression tagExpression);
}
