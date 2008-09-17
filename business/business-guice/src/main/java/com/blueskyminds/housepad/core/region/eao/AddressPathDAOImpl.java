package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.AddressPath;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Date Started: 3/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AddressPathDAOImpl extends AbstractDAO<AddressPath> implements AddressPathDAO {

    private static final String QUERY_ADDRESS_STRING = "hp.addressPath.byAddressString";

    private static final String PARAM_ADDRESS_STRING = "addressString";

    @Inject
    public AddressPathDAOImpl(EntityManager em) {
        super(em, AddressPath.class);
    }

    public AddressPath lookupAddress(String addressString) {
        if (addressString != null) {
            Query query = em.createNamedQuery(QUERY_ADDRESS_STRING);
            query.setParameter(PARAM_ADDRESS_STRING, addressString.toLowerCase());
            return firstIn(query.getResultList());
        } else {
            return null;
        }
    }

    public AddressPath persist(AddressPath addressPath) {
        em.persist(addressPath);
        return addressPath;
    }
}