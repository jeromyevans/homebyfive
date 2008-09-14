package com.blueskyminds.framework;

/**
 * Date Started: 21/03/2008
 * <p/>
 * History:
 */
public class AbstractEntityHolder {

    private AbstractEntity model;

    public AbstractEntityHolder(AbstractEntity abstractEntity) {
        this.model = abstractEntity;
    }

    public AbstractEntity getModel() {
        return model;
    }
}
