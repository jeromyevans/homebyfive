package com.blueskyminds.homebyfive.framework.core.table.model;

import java.util.*;
import java.io.Serializable;

/**
 * A model for a table
 *
 * Date Started: 31/10/2007
 * <p/>
 * History:
 */
public class TableModel<T> implements Serializable {

    private static final long serialVersionUID = 4764866437574631186L;

    private String summary;
    private String caption;
    private List<ColumnModel> columns;
    private List<Map<String, Object>> rows;
    
    public TableModel() {
        this.columns = new ArrayList<ColumnModel>(20);
        this.rows = new LinkedList<Map<String, Object>>();
    }

    public TableModel withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public TableModel withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public ColumnModel addColumn(String heading, String propertyName) {
        ColumnModel columnModel = new ColumnModel(heading, propertyName);
        columns.add(columnModel);
        columnModel.setIndex(columns.indexOf(columnModel));
        return columnModel;
    }

    public ColumnModel addHiddenColumn(String heading, String propertyName) {
        ColumnModel columnModel = addColumn(heading, propertyName);
        columnModel.setHidden(true);
        return columnModel;
    }

    public ColumnModel addSortableColumn(String heading, String propertyName) {
        ColumnModel columnModel = addColumn(heading, propertyName);
        columnModel.setSortable(true);
        return columnModel;
    }

    public String getSummary() {
        return summary;
    }

    public String getCaption() {
        return caption;
    }

    public ColumnModel[] getColumns() {
        ColumnModel[] array = new ColumnModel[(columns.size())];
        return columns.toArray(array);
    }

    public Iterator<ColumnModel> columnIterator() {
        return columns.iterator();
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void addRow(Map<String, Object> row) {
        rows.add(row);
    }
}
