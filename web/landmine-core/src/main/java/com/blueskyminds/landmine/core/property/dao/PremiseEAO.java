package com.blueskyminds.landmine.core.property.dao;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.Street;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.events.PremiseEvent;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Implements queries for Properties (premises)
 *
 * Date Started: 31/03/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PremiseEAO extends AbstractDAO<Premise> {

    private static final String QUERY_PREMISE_BY_ADDRESS = "premise.byAddress";
    private static final String PARAM_ADDRESS = "address";
    private static final String QUERY_PREMISE_BY_PATH = "premise.byPath";
    private static final String QUERY_PREMISE_EVENTS_BY_PATH = "premiseEvents.byPath";
    private static final String PARAM_PATH = "path";

    private static final String QUERY_ALL_PREMISES_BY_SUBURB = "premise.listAllBySuburb";
    private static final String QUERY_ALL_PREMISES_BY_POSTCODE = "premise.listAllByPostCode";
    private static final String QUERY_ALL_PREMISES_BY_STREET = "premise.listAllByStreet";
    private static final String QUERY_ALL_PREMISES_BY_COMPLEX = "premise.listAllByComplex";
    private static final String PARAM_STREET = "street";
    private static final String PARAM_SUBURB = "suburb";
    private static final String PARAM_POSTCODE = "postCode";
    private static final String PARAM_COMPLEX = "complex";


    @Inject
    public PremiseEAO(EntityManager em) {
        super(em, Premise.class);
    }


    /**
     * Searches for the property referencing the address by the address id
     *
     * @return Premise instance, or null if not found
     */
    public Premise getPremiseByAddress(Address address) {
        Premise property;

        Query query = em.createNamedQuery(QUERY_PREMISE_BY_ADDRESS);
        query.setParameter(PARAM_ADDRESS, address);
        property = firstIn(query.getResultList());

        return property;
    }

    /**
     * Get the set of all known premises in the specified suburb
     *
     * @return
     */
    public Set<Premise> listPremisesInSuburb(SuburbHandle suburbHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_PREMISES_BY_SUBURB);
        query.setParameter(PARAM_SUBURB, suburbHandle);
        return new HashSet<Premise>(FilterTools.getNonNull(query.getResultList()));
    }

    /**
     * Get the set of all known premises in the specified postcode
     *
     * @return
     */
    public Set<Premise> listPremisesInPostCode(PostCodeHandle postCodeHandle) {
        Query query = em.createNamedQuery(QUERY_ALL_PREMISES_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE, postCodeHandle);
        return new HashSet<Premise>(FilterTools.getNonNull(query.getResultList()));
    }

      /**
     * Get the set of all known premises in the specified street
     *
     * @return
     */
    public Set<Premise> listPremisesInStreet(Street street) {
        Query query = em.createNamedQuery(QUERY_ALL_PREMISES_BY_STREET);
        query.setParameter(PARAM_STREET, street);
        return new HashSet<Premise>(FilterTools.getNonNull(query.getResultList()));
    }

    /**
     * Get the set of all known premises in the specified complex
     *
     * @return
     */
    public Set<Premise> listPremisesInComplex(Premise complex) {
        Query query = em.createNamedQuery(QUERY_ALL_PREMISES_BY_COMPLEX);
        query.setParameter(PARAM_COMPLEX, complex);
        return new HashSet<Premise>(FilterTools.getNonNull(query.getResultList()));
    }

     /**
     * Lookup a Premise by its exact path in the PropertyBean that references it
     *
     * @param path
     * @return
     */
    public Premise lookupPremise(String path) {
        Query query = em.createNamedQuery(QUERY_PREMISE_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        return firstIn(query.getResultList());
    }

    /**
     * List the events tor the premise identified by its path
     *
     * @param path
     * @return
     */
    public List<PremiseEvent> listEvents(String path) {
        Query query = em.createNamedQuery(QUERY_PREMISE_EVENTS_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        return query.getResultList();
    }

    public void persist(Premise premise) {
        em.persist(premise);
    }
}
