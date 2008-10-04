package com.blueskyminds.enterprise.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.enterprise.region.suburb.SuburbBean;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class SuburbEAO extends AbstractDAO<SuburbBean> {

    private static final String QUERY_ALL_SUBURBS_BY_PARENT_PATH = "hp.suburbs.byParentPath";
    private static final String QUERY_ALL_SUBURBS_BY_POSTCODE = "hp.suburbs.byPostCode";
    private static final String QUERY_SUBURB_BY_PATH = "hp.suburb.byPath";
    private static final String PARAM_PATH = "path";
    private static final String PARAM_POSTCODE_PATH = "postCode";
    private static final String QUERY_SUBURB_BY_HANDLE = "hp.suburb.byHandle";
    private static final String PARAM_HANDLE = "handle";

    @Inject   
    public SuburbEAO(EntityManager entityManager) {
        super(entityManager, SuburbBean.class);
    }

     /**
     * Get a list of all the suburbs in the specified state (eg. /au/nsw)
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<SuburbBean> listSuburbs(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);
        return setOf(query.getResultList());
    }

     /**
     * Get a list of all the suburbs in the specified postcode (eg. /au/nsw)
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<SuburbBean> listSuburbsInPostCode(String postCodePath) {

        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE_PATH, postCodePath);
        return setOf(query.getResultList());
    }

    /**
     * Get the suburb with the specified path (eg. /au/nsw/neutral+bay)
     *
     * @return State, or null if not found
     */
    public SuburbBean lookupSuburb(String path) {

        Query query = em.createNamedQuery(QUERY_SUBURB_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }


    /**
     * Get the suburb by its handle
     *
     * @return SuburbBean, or null if not found
     */
    public SuburbBean lookupSuburb(SuburbHandle suburbHandle) {

        Query query = em.createNamedQuery(QUERY_SUBURB_BY_HANDLE);
        query.setParameter(PARAM_HANDLE, suburbHandle);

        return firstIn(query.getResultList());
    }
}