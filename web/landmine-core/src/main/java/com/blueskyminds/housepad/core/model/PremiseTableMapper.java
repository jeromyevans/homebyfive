package com.blueskyminds.housepad.core.model;

import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.housepad.core.region.reference.RegionRefFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * Date Started: 31/10/2007
 * <p/>
 * History:
 */
public class PremiseTableMapper implements TableMapper<Premise> {

    public static final int COLUMN_ID           = 0;
    public static final int COLUMN_NO           = 1;
    public static final int COLUMN_STREET       = 2;
    public static final int COLUMN_SUBURB       = 3;
    public static final int COLUMN_POSTCODE     = 4;
    public static final int COLUMN_STATE        = 5;
    public static final int COLUMN_TYPE         = 6;
    public static final int COLUMN_BEDROOMS     = 7;
    public static final int COLUMN_BATHROOMS    = 8;
    public static final int NO_OF_COLUMNS       = 9;

    private Collection<Premise> premises;

    public PremiseTableMapper(Collection<Premise> premises) {
        this.premises = premises;
    }

    /**
     * Get the property value to include in the model
     *
     * @param name
     * @return
     */
    public Object getProperty(Premise premise, int columnIndex, String name) {
        Object value = null;

        switch (columnIndex) {
            case COLUMN_ID:
                value = premise.getId();
                break;
            case COLUMN_NO:
                value = premise.getAddress().getNumber();
                break;
            case COLUMN_STREET:
                value = RegionRefFactory.createRef(premise.getAddress().getStreet(), premise.getAddress());
                break;
            case COLUMN_SUBURB:
                value = RegionRefFactory.createRef(premise.getAddress().getSuburb());
                break;
            case COLUMN_POSTCODE:
                value = RegionRefFactory.createRef(premise.getAddress().getPostCode());
                break;
            case COLUMN_STATE:
                value = RegionRefFactory.createRef(premise.getAddress().getState());
                break;
            case COLUMN_TYPE:
                value = premise.getType().name();
                break;
            case COLUMN_BEDROOMS:
                value = premise.getBedrooms();
                break;
            case COLUMN_BATHROOMS:
                value = premise.getBathrooms();
                break;
        }

        return value;
    }

    /** Maps the Premises into the table */
    public void populate(TableModel tableModel) {
        for (Premise premise : premises) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (ColumnModel column : tableModel.getColumns()) {
                row.put(column.getName(), getProperty(premise, column.getIndex(), column.getName()));
            }
            tableModel.addRow(row);
        }
    }

}
