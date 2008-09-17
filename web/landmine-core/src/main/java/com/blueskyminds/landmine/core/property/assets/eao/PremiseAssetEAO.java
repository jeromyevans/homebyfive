package com.blueskyminds.landmine.core.property.assets.eao;

import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.assets.PremiseAsset;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetMap;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Queries to access PremiseAsset's
 *
 * Date Started: 5/05/2008
 */
public class PremiseAssetEAO extends AbstractDAO<PremiseAsset> {

    private static final String QUERY_ALL_ASSETS = "premiseAsset.listAll";
    private static final String QUERY_ASSETS_FOR_PREMISE = "premiseAssetMaps.byPremise";
    private static final String QUERY_ASSET_BY_KEY = "premiseAsset.byKey";
    
    @Inject
    public PremiseAssetEAO(EntityManager entityManager) {
        super(entityManager, PremiseAsset.class);
    }
       
    public Set<PremiseAsset> listAssets() {
        Query query = em.createNamedQuery(QUERY_ALL_ASSETS);
        return setOf(query.getResultList());
    }

    public void persist(PremiseAsset asset) {
        em.persist(asset);
    }

    public Set<PremiseAssetMap> listAssetsForPremise(Premise premise) {
        Query query = em.createNamedQuery(QUERY_ASSETS_FOR_PREMISE);
        query.setParameter("premise", premise);
        return new HashSet<PremiseAssetMap>(query.getResultList());
    }

    public PremiseAsset lookupAsset(String key) {
        Query query = em.createNamedQuery(QUERY_ASSET_BY_KEY);
        query.setParameter("keyValue", key);
        return firstIn(query.getResultList());
    }
}
