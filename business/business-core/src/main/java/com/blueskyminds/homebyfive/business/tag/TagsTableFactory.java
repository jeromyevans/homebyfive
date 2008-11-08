package com.blueskyminds.homebyfive.business.tag;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;

import java.util.*;

/**
 * Prepare a table of tags
 * <p/>
 * Date Started: 21/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TagsTableFactory {

    public static TableModel create(String basePath, Collection<Tag> tags) {

        TableModel tableModel = TableModelBuilder.createModel().withCaption("tags");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Name", "name").formattedAs("Tag");

        populate(tableModel, tags, basePath);
        return tableModel;
    }

    /**
     * Maps the tags into the table
     */
    protected static void populate(TableModel tableModel, Collection<Tag> tagMaps, String basePath) {
        List<Tag> sorted = new ArrayList<Tag>(tagMaps);
        for (Tag map : sorted) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(map, column.getIndex(), basePath));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    protected static Object getProperty(Tag tag, int columnIndex, String basePath) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = tag.getId();
                break;
            case 1:
                value = new TagRef(basePath, tag);
                break;
        }

        return value;
    }
}