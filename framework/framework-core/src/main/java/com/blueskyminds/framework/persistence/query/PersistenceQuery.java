package com.blueskyminds.framework.persistence.query;

import com.blueskyminds.framework.persistence.query.QueryParameter;

import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * A vendor-independent interface to a query for searching for objects in persistence.
 *
 *  This interface has two roles:
 *     1. hide the underlying implementation
 *     2. allow multiple query mechanisms to be be accessed through the same interface
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface PersistenceQuery extends ParameterisedQuery<PersistenceQuery> {

    String getQueryString();
}
