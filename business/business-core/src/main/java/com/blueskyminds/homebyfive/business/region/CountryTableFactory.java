package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefFactory;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 18/04/2008
 */
public class CountryTableFactory {

    private static final Log LOG = LogFactory.getLog(CountryTableFactory.class);

    /**
     *
     * @param countries
     * @return
     */
    public static TableModel createTable(Collection<Country> countries) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Countries");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Region");

        List<Country> sorted = new ArrayList<Country>(countries);
        Collections.sort(sorted);
        populate(tableModel, sorted);

        return tableModel;
    }

    /** Maps the Country into the table */
    public static void populate(TableModel tableModel, Collection<Country> countries) {
        for (Country country : countries) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(country, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(Country country, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = country.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(country.getPath(), country.getName(), RegionRefType.Country);
                break;
        }

        return value;
    }
}