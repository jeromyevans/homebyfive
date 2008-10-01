package com.blueskyminds.homebyfive.framework.framework.persistence.jdbc;

import com.blueskyminds.homebyfive.framework.framework.persistence.paging.QueryPage;
import com.blueskyminds.homebyfive.framework.framework.persistence.paging.Page;

import java.util.List;
import java.sql.PreparedStatement;

/**
 * Paging implementation over JDBC
 *
 * Todo: this is incomplete: A ResultSet doesn't readily translate into a List of objects (it already is)
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JdbcPage<ResultSet> extends QueryPage<ResultSet, PreparedStatement> implements Page<ResultSet> {

   // ------------------------------------------------------------------------------------------------------

    /**
     *
     * @param query     the prepared statement must include a parameter at index 1 called id that is an integer
     *               used in the form:        where id > ?
     *               To implement the paging in a vendor independent way (but this of course assumes ids are sequential)
     * @param pageSize
     * @param pageNo
     */
    public JdbcPage(PreparedStatement query, int pageSize, int pageNo) {
        super(query, pageSize, pageNo);
    }

    // ------------------------------------------------------------------------------------------------------

    @SuppressWarnings({"unchecked"})
    public List<ResultSet> doQuery(PreparedStatement query, int firstResult, int maxResults) {
        /*List<ResultSet> results = new LinkedLis

        query.setInt(1, firstResult);
        query.setMaxRows(maxResults);

        ResultSet resultSet = query.executeQuery();


        results = (List<ResultSet>) ((Query) query).setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();

*/
        return null;
    }
    // ------------------------------------------------------------------------------------------------------
}
