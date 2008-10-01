package com.blueskyminds.homebyfive.framework.core.persistence.query;

/**
 * A vendor independent named Query
 *
 * Simple contains a name.  The vendor-specific PersistenceService will need to transform it.
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface PersistenceNamedQuery extends PersistenceQuery {    

    /** The name of the query */
    String getName();

}
