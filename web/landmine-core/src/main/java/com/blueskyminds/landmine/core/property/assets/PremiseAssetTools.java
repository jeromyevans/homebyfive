package com.blueskyminds.landmine.core.property.assets;

import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.tools.filters.Filter;
import com.blueskyminds.framework.transformer.Transformer;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * Date Started: 2/05/2008
 */
public class PremiseAssetTools {

     /**
     * Get the PremiseAssets from a collection of PremiseAssetMaps
     *
     * @param collection    collection of PremiseAssetMaps
     * @return
     */
    public static <T extends PremiseAssetMap> Set<PremiseAsset> extractPremiseAssets(Collection<T> collection) {
        return new HashSet<PremiseAsset>(FilterTools.getTransformed(collection, new Transformer<T, PremiseAsset>() {
            public PremiseAsset transform(PremiseAssetMap premiseAssetMap) {
                return premiseAssetMap.getAsset();
            }
        }));
    }

    /**
     * Determines if the collection of PremiseAsset maps contains the PremiseAsset
     * @param premiseAssetMaps
     * @param premiseAsset
     * @return
     * */
    public static <T extends PremiseAssetMap> boolean contains(Collection<T> premiseAssetMaps, final PremiseAsset premiseAsset) {
        return FilterTools.matchesAny(premiseAssetMaps, new Filter<T>(){
            public boolean accept(T premiseAssetMap) {
                return premiseAssetMap.getAsset().equals(premiseAsset);
            }
        });
    }

    /**
     * Determines if the collection of PremiseAsset maps contain the PremiseAsset specified by name
     * @param premiseAssetMaps
     * @param name
     * @return true if an exact match
     * */
    public static <T extends PremiseAssetMap> boolean contains(Collection<T> premiseAssetMaps, final String name) {
        return FilterTools.matchesAny(premiseAssetMaps, new Filter<T>(){
            public boolean accept(T premiseAssetMap) {
                return premiseAssetMap.getAsset().getName().equals(name);
            }
        });
    }
}
