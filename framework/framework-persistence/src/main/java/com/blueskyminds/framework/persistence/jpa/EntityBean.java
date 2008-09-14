package com.blueskyminds.framework.persistence.jpa;

/**
 * A bean that defines an Entity that needs to be mapped for JPA
 *
 * Used to configure a JPA provider
 *
 * Date Started: 21/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class EntityBean {

    private String className;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the className of the class that needs to be mapped */
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
