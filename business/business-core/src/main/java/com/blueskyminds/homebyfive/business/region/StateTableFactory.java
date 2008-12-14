package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefFactory;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;
import com.blueskyminds.homebyfive.business.region.graph.State;

import java.util.*;

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
    public static TableModel createTable(Collection<State> states) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of States");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");

        List<State> sorted = new ArrayList<State>(states);
        Collections.sort(sorted);
        populate(tableModel, sorted);

        return tableModel;
    }

    /** Maps the PostCodes into the table */
    public static void populate(TableModel tableModel, Collection<State> states) {
        for (State state : states) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(state, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(State state, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = state.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(state.getPath(), state.getName(), RegionRefType.State);
                break;
        }

        return value;
    }
}