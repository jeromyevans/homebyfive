package com.blueskyminds.framework.persistence.hibernate;

import org.hibernate.Query;
import org.hibernate.Criteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.persistence.paging.QueryPage;

/**
 *
 * A pagination implementation for Hibernate
 *
 * PageNo starts at 0
 *
 * Date Started: 21/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernatePageImpl<T extends DomainObject> extends QueryPage<T, Object> implements Page<T> {

    private static Log LOG = LogFactory.getLog(HibernatePageImpl.class);

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public HibernatePageImpl(Query query, int pageNo, int pageSize) {
        super(query, pageSize, pageNo);
    }

    public HibernatePageImpl(Criteria query, int pageNo, int pageSize) {
        super(query, pageSize, pageNo);
    }

    // ------------------------------------------------------------------------------------------------------

    @SuppressWarnings({"unchecked"})
    public List<T> doQuery(Object query, int firstResult, int maxResults) {
        List<T> results;

        if (query instanceof Criteria) {
            results = (List<T>) ((Criteria) query).setFirstResult(firstResult)
                    .setMaxResults(maxResults)
                    .list();
        } else {
            results = (List<T>) ((Query) query).setFirstResult(firstResult)
                    .setMaxResults(maxResults)
                    .list();
        }

        return results;
    }

    // ------------------------------------------------------------------------------------------------------
}