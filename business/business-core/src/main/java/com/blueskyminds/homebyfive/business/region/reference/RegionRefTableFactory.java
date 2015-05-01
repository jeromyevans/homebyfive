package com.blueskyminds.homebyfive.business.region.reference;

import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.*;

/**
 * Creates a single-column TableModel of RegionRefs
 *
 * Date Started: 14/06/2008
 * <p/>
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class RegionRefTableFactory {

    public static TableModel createFromHandles(List<Region> regions) {
        List<RegionRef> regionRefs = RegionRefFactory.createRefs(regions);
        return create(regionRefs);
    }

    public static TableModel createFromHandles(Set<Region> regions) {
        List<Region> sorted = new ArrayList<Region>(regions);
        Collections.sort(sorted);
        List<RegionRef> regionRefs = RegionRefFactory.createRefs(sorted);
        return create(regionRefs);
    }

    public static TableModel create(List<RegionRef> regionRefs) {

        TableModel tableModel = TableModelBuilder.createModel().withCaption("Regions");
        tableModel.addSortableColumn("Region", "region").formattedAs("Region");
        populate(tableModel, regionRefs);

        return tableModel;
    }

    /** Maps the AggregateSets into the table */
    protected static void populate(TableModel tableModel, Collection<RegionRef> regions) {
        List<RegionRef> sorted = new ArrayList<RegionRef>(regions);
        for (RegionRef region : sorted) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), region);
            }
            tableModel.addRow(row);
        }
    }
}
