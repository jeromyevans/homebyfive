package com.blueskyminds.enterprise.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.enterprise.region.postcode.PostCodeBean;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
public class PostCodeEAO extends AbstractDAO<PostCodeBean> {

    private static final String QUERY_ALL_POSTCODES_BY_PARENT_PATH = "hp.postCodes.byParentPath";
    private static final String QUERY_POSTCODE_BY_PATH = "hp.postCode.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_POSTCODE_BY_HANDLE = "hp.postCode.byHandle";
    private static final String PARAM_HANDLE = "handle";

    @Inject  
    public PostCodeEAO(EntityManager entityManager) {
        super(entityManager, PostCodeBean.class);
    }

    /**
     * Get a list of all the postcodes in the specified state (eg. /au/nsw)
     *
     * @return PostCodes, or empty set if not found
     */
    public Set<PostCodeBean> listPostCodes(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);

        return setOf(query.getResultList());
    }

    /**
     * Get the postcode with the specified path (eg. /au/nsw/2089)
     *
     * @return PostCode, or null if not found
     */
    public PostCodeBean lookupPostCode(String path) {

        Query query = em.createNamedQuery(QUERY_POSTCODE_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }

      /**
     * Get the postcode by its handle
     *
     * @return PostCodeBean, or null if not found
     */
    public PostCodeBean lookupPostCode(PostCodeHandle postCodeHandle) {

        Query query = em.createNamedQuery(QUERY_POSTCODE_BY_HANDLE);
        query.setParameter(PARAM_HANDLE, postCodeHandle);

        return firstIn(query.getResultList());
    }

}