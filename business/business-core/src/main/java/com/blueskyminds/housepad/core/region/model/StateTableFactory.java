package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.model.ColumnModel;
import com.blueskyminds.housepad.core.region.reference.RegionRefFactory;
import com.blueskyminds.housepad.core.region.reference.RegionRefType;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 18/04/2008
 */
public class StateTableFactory {

    private static final Log LOG = LogFactory.getLog(StateTableFactory.class);

    /**
     *
     * @param states
     * @return
     */
    public static TableModel createTable(Collection<StateBean> states) {
        String countryName;
        if (states.size() > 0) {
            countryName = states.iterator().next().getCountryName();
        } else {
            countryName = "";
        }
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of States and Territories of "+countryName);

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");

        populate(tableModel, states);

        return tableModel;
    }

    /** Maps the PostCodes into the table */
    public static void populate(TableModel tableModel, Collection<StateBean> states) {
        for (StateBean suburb : states) {
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
    public static Object getProperty(StateBean stateBean, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = stateBean.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(stateBean.getPath(), stateBean.getName(), RegionRefType.State);
                break;
        }

        return value;
    }
}