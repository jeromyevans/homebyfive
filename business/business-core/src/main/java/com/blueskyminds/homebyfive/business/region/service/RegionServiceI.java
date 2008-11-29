package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagsTableFactory;
import com.blueskyminds.homebyfive.business.tag.service.InvalidTagException;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;
import java.util.List;

/**
 * Date Started: 3/03/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface RegionServiceI<T extends Region> {

    RegionGroup list(String path);

    T lookup(String path);

    T create(T country) throws DuplicateRegionException, InvalidRegionException;

    /**
     * Update an existing Region
     * Propagates the change into the RegionGraph as well
     * @param path
     * @param country
     */
    T update(String path, T country) throws InvalidRegionException;

    void delete(String id);

    TableModel listTags(String path);

    void assignTag(String path, String tag) throws InvalidRegionException, InvalidTagException; 

    void removeTag(String path, String tag) throws InvalidRegionException;

    Set<T> listByTag(String tagName);
}
