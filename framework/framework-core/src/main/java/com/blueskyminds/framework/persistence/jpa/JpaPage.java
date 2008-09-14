package com.blueskyminds.framework.persistence.jpa;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.persistence.paging.QueryPage;
import com.blueskyminds.framework.persistence.paging.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;
import java.util.List;
import java.io.Serializable;

/**
 * A pagination implementation for JPA
 *
 * PageNo starts at 0
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaPage<T extends DomainObject> extends QueryPage<T, Object> implements Page<T>, Serializable {

    private static Log LOG = LogFactory.getLog(JpaPage.class);
    private static final long serialVersionUID = -8064580682869873542L;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public JpaPage(Query query, int pageNo, int pageSize) {
        super(query, pageNo, pageSize);
    }   

    // ------------------------------------------------------------------------------------------------------

    @SuppressWarnings({"unchecked"})
    public List<T> doQuery(Object query, int firstResult, int maxResults) {
        List<T> results;

        results = (List<T>) ((Query) query).setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();


        return results;
    }

    // ------------------------------------------------------------------------------------------------------
}