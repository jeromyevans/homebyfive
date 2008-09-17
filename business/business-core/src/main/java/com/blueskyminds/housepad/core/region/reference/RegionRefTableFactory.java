package com.blueskyminds.housepad.core.region.reference;

import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.model.TableModelBuilder;
import com.blueskyminds.housepad.core.model.ColumnModel;
import com.blueskyminds.enterprise.region.RegionHandle;

import java.util.*;

/**
 * Creates a single-column TableModel of RegionRefs
 *
 * Date Started: 14/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegionRefTableFactory {

    public static TableModel createFromHandles(List<RegionHandle> regionHandles) {
        List<RegionRef> regionRefs = RegionRefFactory.createRefs(regionHandles);
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
