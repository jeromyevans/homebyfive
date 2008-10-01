package com.blueskyminds.homebyfive.framework.core.table.model;

/**
 * Used to map data from an Object into a TableModel
 *
 * It should use copy-on-write semantics
 *
 * Date Started: 31/10/2007
 * <p/>
 * History:
 */
public interface TableMapper<T> {

    /**
     * Get the property value to include in the model
     *
     * The name of the property and column index are both provided so the implementation can chose the most suitable implementation
     *
     * @param name
     * @param columnIndex
     * @return
     */
    Object getProperty(T object, int columnIndex, String name);
}
