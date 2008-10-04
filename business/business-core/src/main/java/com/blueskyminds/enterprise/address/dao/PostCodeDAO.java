package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Postcode specific queries
 *
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class PostCodeDAO extends AbstractDAO<PostalCode> {

    public PostCodeDAO(EntityManager em) {
        super(em, PostalCode.class);
    }
}
