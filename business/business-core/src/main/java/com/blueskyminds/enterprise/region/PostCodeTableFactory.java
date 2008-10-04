package com.blueskyminds.enterprise.region;

import com.blueskyminds.homebyfive.framework.core.table.model.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.model.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.model.ColumnModel;
import com.blueskyminds.enterprise.region.reference.RegionRefFactory;
import com.blueskyminds.enterprise.region.reference.RegionRefType;
import com.blueskyminds.enterprise.region.index.PostalCodeBean;

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
    public static TableModel createTable(Collection<PostalCodeBean> postCodes) {
        String stateName;
        if (postCodes.size() > 0) {
            stateName = postCodes.iterator().next().getStateName();
        } else {
            stateName = "";
        }
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Post Codes in "+stateName);

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Post Code", "postCode").formattedAs("Region");

        populate(tableModel, postCodes);

        return tableModel;
    }

    /** Maps the PostCodes into the table */
    public static void populate(TableModel tableModel, Collection<PostalCodeBean> postCodes) {
        for (PostalCodeBean suburb : postCodes) {
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
    public static Object getProperty(PostalCodeBean postCodeBean, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = postCodeBean.getId();
                break;
            case 1:
                value = RegionRefFactory.createRef(postCodeBean.getPath(), postCodeBean.getName(), RegionRefType.PostCode);
                break;
        }

        return value;
    }
}