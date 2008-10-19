package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.table.TableModelBuilder;
import com.blueskyminds.homebyfive.framework.core.table.ColumnModel;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;

import java.util.*;

/**
 * Date Started: 4/05/2008
 */
public class SubstitutionTableModelFactory {

    /**
     *
     * @param substitutions
     * @return
     */
    public static TableModel createTable(List<Substitution> substitutions) {

        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of Substitutions");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addSortableColumn("Pattern", "pattern");
        tableModel.addSortableColumn("Substitution", "substitution");
        tableModel.addSortableColumn("Metadata", "metadata");
        tableModel.addSortableColumn("Group", "group");
        tableModel.addSortableColumn("SequenceNo", "sequenceNo");

        populate(tableModel, substitutions);

        return tableModel;
    }

    /** Maps the substitutions into the table */
    public static void populate(TableModel tableModel, Collection<Substitution> substitutions) {
        for (Substitution substitution : substitutions) {
            Map<String, Object> row = new HashMap<String, Object>();
            Iterator<ColumnModel> iterator = tableModel.columnIterator();
            while (iterator.hasNext()) {
                ColumnModel column = iterator.next();
                row.put(column.getName(), getProperty(substitution, column.getIndex()));
            }
            tableModel.addRow(row);
        }
    }

    /**
     * Get the property value to include in the model
     *
     * @return
     */
    public static Object getProperty(Substitution substitution, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = substitution.getId();
                break;
            case 1:
                value = substitution.getPattern();
                break;
            case 2:
                value = substitution.getSubstitution();
                break;
            case 3:
                value = substitution.getMetadata();
                break;
            case 4:
                value = substitution.getGroupName();
                break;
            case 5:
                value = substitution.getSequenceNo();
                break;
        }

        return value;
    }
}