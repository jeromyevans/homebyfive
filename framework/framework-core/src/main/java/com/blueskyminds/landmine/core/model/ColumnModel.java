package com.blueskyminds.landmine.core.model;

import java.io.Serializable;

/**
 * Date Started: 31/10/2007
 * <p/>
 * History:
 */
public class ColumnModel implements Serializable {

    private static final long serialVersionUID = 2222541274340772354L;

    private int index;
    private String heading;
    private String name;
    private String abbr;
    private ColumnType type;
    private Object format;
    private boolean hidden;
    private boolean sortable;
    private String[] perspectives;

    public ColumnModel() {
    }

    public ColumnModel(String heading, String propertyName) {
        this.heading = heading;
        this.name = propertyName;
    }

    public ColumnModel withAbbr(String abbr) {
        this.abbr = abbr;
        return this;
    }

    public ColumnModel formattedAs(Object format) {
        this.format =  format;
        return this;
    }

    public ColumnModel withType(ColumnType type) {
        this.type = type;
        return this;
    }

    protected void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    protected void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /**
     * Assign one or more perspectives to this columnModel.  Perspectives can be used to control which columns
     *  are visible in different views of the same table.
     *
     * Each perspective is identified by a unique string.  No perspectives (default) means the column belongs to
     * all perspectives.
     *
     * @param perspectives
     */
    public ColumnModel withPerspectives(String[] perspectives) {
        this.perspectives = perspectives;
        return this;
    }

    /**
     * Assign a single perspective to this columnModel.  Perspectives can be used to control which columns
     *  are visible in different views of the same table.
     *
     * Each perspective is identified by a unique string.  No perspectives (default) means the column belongs to
     * all perspectives.
     *
     * @param perspectiveName
     */
    public ColumnModel withPerspective(String perspectiveName) {
        this.perspectives = new String[] {perspectiveName};
        return this;
    }

    /** @param propertyName     the name of the property for row data */
    public ColumnModel(int index, String heading, String abbr, String propertyName, ColumnType type, ColumnFormat format, boolean sortable, boolean hidden) {
        this.index = index;
        this.heading = heading;
        this.name = propertyName;
        this.abbr = abbr;
        this.type = type;
        this.format = format;
        this.sortable = sortable;
        this.hidden = hidden;
    }

     public ColumnModel(int index, String heading, String abbr, String propertyName, ColumnType type, Object format, boolean sortable, boolean hidden) {
        this.index = index;
        this.heading = heading;
        this.name = propertyName;
        this.abbr = abbr;
        this.type = type;
        this.format = format;
        this.sortable = sortable;
        this.hidden = hidden;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getHeading() {
        return heading;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    public ColumnType getType() {
        return type;
    }

    public Object getFormat() {
        return format;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String[] getPerspectives() {
        return perspectives;
    }
       
}
