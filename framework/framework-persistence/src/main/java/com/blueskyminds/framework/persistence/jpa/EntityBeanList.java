package com.blueskyminds.framework.persistence.jpa;

import java.util.List;
import java.util.LinkedList;

/**
 * Lists all of the entity beans in a Definition
 *
 * Date Started: 21/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class EntityBeanList {

    private List<EntityBean> entityBeans;

    // ------------------------------------------------------------------------------------------------------

    public  EntityBeanList() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the EntityBeanList with default attributes
     */
    private void init() {
        entityBeans = new LinkedList<EntityBean>();
    }

    /** Add the entity bean to the list */
    public void addEntity(EntityBean entityBean) {
        entityBeans.add(entityBean);
    }

    // ------------------------------------------------------------------------------------------------------

    public List<EntityBean> getEntityBeans() {
        return entityBeans;
    }

    public void setEntityBeans(List<EntityBean> entityBeans) {
        this.entityBeans = entityBeans;
    }
}
