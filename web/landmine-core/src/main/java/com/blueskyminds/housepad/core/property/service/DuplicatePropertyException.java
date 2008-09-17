package com.blueskyminds.housepad.core.property.service;

import com.blueskyminds.housepad.core.property.model.PropertyBean;

/**
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public class DuplicatePropertyException extends Exception {
    private static final long serialVersionUID = 8721546729552267958L;

    public DuplicatePropertyException() {
    }

    public DuplicatePropertyException(PropertyBean propertyBean) {
         super(DuplicatePropertyException.class.getSimpleName()+":"+propertyBean.getPath());
    }

    public DuplicatePropertyException(String message) {
        super(message);
    }

    public DuplicatePropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatePropertyException(Throwable cause) {
        super(cause);
    }
}
