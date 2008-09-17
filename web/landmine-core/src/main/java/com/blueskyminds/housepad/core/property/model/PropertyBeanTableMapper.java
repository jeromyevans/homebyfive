package com.blueskyminds.housepad.core.property.model;

import com.blueskyminds.housepad.core.model.TableMapper;
import com.blueskyminds.housepad.core.model.TableModel;
import com.blueskyminds.housepad.core.model.ColumnModel;
import com.blueskyminds.housepad.core.region.reference.RegionRefFactory;
import com.blueskyminds.housepad.core.region.reference.RegionRefType;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.landmine.core.property.events.PremiseEventRefFactory;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Date Started: 18/04/2008
 */
public class PropertyBeanTableMapper implements TableMapper<PropertyBean> {

    public static final int COLUMN_ID           = 0;
    public static final int COLUMN_NO           = 1;
    public static final int COLUMN_STREET       = 2;
    public static final int COLUMN_SUBURB       = 3;
    public static final int COLUMN_TYPE         = 4;
    public static final int COLUMN_BEDROOMS     = 5;
    public static final int COLUMN_BATHROOMS    = 6;
    public static final int COLUMN_LAST_EVENT   = 7;

    private Collection<PropertyBean> properties;

    public PropertyBeanTableMapper(Collection<PropertyBean> properties) {
        this.properties = properties;
    }

    /**
     * Get the property value to include in the model
     *
     * @param name
     * @return
     */
    public Object getProperty(PropertyBean property, int columnIndex, String name) {
        Object value = null;

        switch (columnIndex) {
            case COLUMN_ID:
                value = property.getId();
                break;
            case COLUMN_NO:
                value = RegionRefFactory.createRef(property.getPath(), property.getNumber(), RegionRefType.Premise);
                break;
            case COLUMN_STREET:
                value = RegionRefFactory.createRef(property.getStreetPath(), property.getStreetFullName(), RegionRefType.Street);
                break;
            case COLUMN_SUBURB:
                value = RegionRefFactory.createRef(property.getSuburbBean());
                break;
            case COLUMN_TYPE:
                PropertyTypes type = property.getType();
                if (type != null) {
                    value = type.name();
                }
                break;
            case COLUMN_BEDROOMS:
                value = property.getBedrooms();
                break;
            case COLUMN_BATHROOMS:
                value = property.getBathrooms();
                break;
            case COLUMN_LAST_EVENT:
                value = PremiseEventRefFactory.create(property);
                break;
        }

        return value;
    }

    /** Maps the Premises into the table */
    public void populate(TableModel tableModel) {
        for (PropertyBean premise : properties) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(premise, column.getIndex(), column.getName()));
            }
            tableModel.addRow(row);
        }
    }

}
