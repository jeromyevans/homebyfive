package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefFactory;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Street;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Date Started: 20/11/2008
 */
public class StreetTableFactory {

    /**
     *
     * @param streets
     * @return
     */
    public static TableModel createTable(Collection<Street> streets) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Streets");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");

        populate(tableModel, streets);

        return tableModel;
    }

    /** Maps the Streets into the table */
    public static void populate(TableModel tableModel, Collection<Street> streets) {
        for (Street street : streets) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(street, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(Street street, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = street.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(street.getPath(), street.getName(), RegionRefType.Street);
                break;
        }

        return value;
    }
}