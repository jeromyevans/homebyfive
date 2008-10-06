package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefFactory;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 18/04/2008
 */
public class PostCodeTableFactory {

    private static final Log LOG = LogFactory.getLog(PostCodeTableFactory.class);

    /**
     *
     * @param postCodes
     * @return
     */
    public static TableModel createTable(Collection<PostalCode> postCodes) {

        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Post Codes");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Post Code", "postCode").formattedAs("Region");

        populate(tableModel, postCodes);

        return tableModel;
    }

    /** Maps the PostCodes into the table */
    public static void populate(TableModel tableModel, Collection<PostalCode> postCodes) {
        for (PostalCode postalCode : postCodes) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(postalCode, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }
    
    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(PostalCode postalCode, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = postalCode.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(postalCode.getPath(), postalCode.getName(), RegionRefType.PostCode);
                break;
        }

        return value;
    }
}