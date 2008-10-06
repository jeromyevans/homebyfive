package com.blueskyminds.enterprise.region;

import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.model.ColumnModel;
import com.blueskyminds.enterprise.region.reference.RegionRefFactory;
import com.blueskyminds.enterprise.region.reference.RegionRefType;
import com.blueskyminds.enterprise.region.graph.Suburb;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Date Started: 18/04/2008
 */
public class SuburbTableFactory {

    /**
     *
     * @param suburbs
     * @return
     */
    public static TableModel createTable(Collection<Suburb> suburbs) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Suburbs");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");

        populate(tableModel, suburbs);

        return tableModel;
    }

    /** Maps the Suburbs into the table */
    public static void populate(TableModel tableModel, Collection<Suburb> suburbs) {
        for (Suburb suburb : suburbs) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(suburb, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(Suburb suburb, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = suburb.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(suburb.getPath(), suburb.getName(), RegionRefType.Suburb);
                break;
        }

        return value;
    }
}

