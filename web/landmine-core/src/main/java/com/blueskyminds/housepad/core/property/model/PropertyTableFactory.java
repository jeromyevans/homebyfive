package com.blueskyminds.housepad.core.property.model;

import com.blueskyminds.housepad.core.model.*;
import com.blueskyminds.housepad.core.region.model.DefaultPerspectives;

import java.util.Collection;

/**
 * Date Started: 18/04/2008
 */
public class PropertyTableFactory {

    public static TableModel createTable(Collection<PropertyBean> properties) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of properties");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addColumn("Street No", "streetNo").formattedAs("Region").withAbbr("No.");
        tableModel.addSortableColumn("Street", "street").formattedAs("Region").withAbbr("St.");
        tableModel.addSortableColumn("Suburb", "suburb").formattedAs("Region").withPerspective(DefaultPerspectives.WIDE_FORM);
        tableModel.addSortableColumn("Type", "type").withPerspective(DefaultPerspectives.WIDE_FORM);
        tableModel.addSortableColumn("Bedrooms", "bedrooms").withType(ColumnType.Number).withPerspective(DefaultPerspectives.WIDE_FORM);
        tableModel.addSortableColumn("Bathrooms", "bathrooms").withType(ColumnType.Number).withPerspective(DefaultPerspectives.WIDE_FORM);
        tableModel.addSortableColumn("Recent Events", "event").formattedAs("Event").withPerspective(DefaultPerspectives.WIDE_FORM);

        new PropertyBeanTableMapper(properties).populate(tableModel);

        return tableModel;
    }

}