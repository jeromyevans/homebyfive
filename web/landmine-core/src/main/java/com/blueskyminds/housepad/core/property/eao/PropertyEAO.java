package com.blueskyminds.housepad.core.property.eao;

import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.landmine.core.property.Premise;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Set;
import java.util.List;

/**
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public class PropertyEAO extends AbstractDAO<PropertyBean> {

    private static final String QUERY_ALL_PROPERTIES_BY_SUBURB_PATH = "hp.properties.bySuburbPath";
    private static final String QUERY_ALL_PROPERTIES_BY_POSTCODE_PATH = "hp.properties.byPostCodePath";
    private static final String QUERY_ALL_PROPERTIES_BY_PATH = "hp.properties.startingWithPath";
    private static final String QUERY_PROPERTY_BY_PATH = "hp.property.byPath";
    private static final String QUERY_PROPERTY_BY_PREMISE = "hp.property.byPremise";

    private static final String PARAM_PATH = "path";
    private static final String PARAM_PREMISE = "premise";

    @Inject
    public PropertyEAO(EntityManager entityManager) {
        super(entityManager, PropertyBean.class);
    }

    public Set<PropertyBean> listPropertiesInSuburb(String suburbPath) {
        Query query = em.createNamedQuery(QUERY_ALL_PROPERTIES_BY_SUBURB_PATH);
        query.setParameter(PARAM_PATH, suburbPath);
        return setOf(query.getResultList());
    }

    public Set<PropertyBean> listPropertiesInPostCode(String postCodePath) {
        Query query = em.createNamedQuery(QUERY_ALL_PROPERTIES_BY_POSTCODE_PATH);
        query.setParameter(PARAM_PATH, postCodePath);
        return setOf(query.getResultList());
    }

    /**
     * List properties <em>starting</em> with the given path
     *
     * @param path
     * @return
     */
    public Set<PropertyBean> listProperties(String path) {
        Query query = em.createNamedQuery(QUERY_ALL_PROPERTIES_BY_PATH);
        query.setParameter(PARAM_PATH, path+"%");
        return setOf(query.getResultList());
    }

    /**
     * Lookup a property by its exact path
     *
     * @param path
     * @return
     */
    public PropertyBean lookupProperty(String path) {
        Query query = em.createNamedQuery(QUERY_PROPERTY_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        return firstIn(query.getResultList());
    }

    /** Lookup property beans from the premise they reference.  Note that it's legitimate for multiple
     * property beans to reference the same premise, one for each alternate address
     *
     * @param premise
     * @return
     */
    public Set<PropertyBean> lookupProperties(Premise premise) {
        Query query = em.createNamedQuery(QUERY_PROPERTY_BY_PREMISE);
        query.setParameter(PARAM_PREMISE, premise);
        return setOf(query.getResultList());
    }
}
