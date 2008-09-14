package com.blueskyminds.framework.persistence.jdbc;

import java.util.Map;
import java.util.HashMap;

/**
 * A Data Mapper that can be extended to map between Domain Objects and Persistence
 *
 * Date Started: 12/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class AbstractDataMapper {

    protected Map loadedIdentityMap = new HashMap();

   // protected T abstractFind()

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractDataMapper with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    abstract protected String findStatement();
}
