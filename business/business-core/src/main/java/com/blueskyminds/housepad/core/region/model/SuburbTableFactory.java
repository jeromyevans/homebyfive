package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.model.ColumnModel;
import com.blueskyminds.housepad.core.region.reference.RegionRefFactory;
import com.blueskyminds.housepad.core.region.reference.RegionRefType;
import com.blueskyminds.enterprise.region.suburb.SuburbBean;

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
    public static TableModel createTable(Collection<SuburbBean> suburbs) {
        String stateName;
        if (suburbs.size() > 0) {
            stateName = suburbs.iterator().next().getStateName();
        } else {
            stateName = "";
        }

        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Suburbs in "+stateName);

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");
        tableModel.addSortableColumn("Post Code", "postCode").formattedAs("Region").withPerspective(DefaultPerspectives.WIDE_FORM);

        populate(tableModel, suburbs);

        return tableModel;
    }

    /** Maps the Suburbs into the table */
    public static void populate(TableModel tableModel, Collection<SuburbBean> suburbs) {
        for (SuburbBean suburb : suburbs) {
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
    public static Object getProperty(SuburbBean suburb, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = suburb.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(suburb.getPath(), suburb.getName(), RegionRefType.Suburb);
                break;
            case 2:
                value = RegionRefFactory.createPostCodeRef(suburb);
                break;
        }

        return value;
    }
}

