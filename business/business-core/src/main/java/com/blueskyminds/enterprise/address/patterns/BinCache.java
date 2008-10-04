package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains a map of recently used Bin's for the DepthFirstAddressParser
 *
 * Date Started: 16/07/2008
 * <p/>
 */
public class BinCache {

    private static final Log LOG = LogFactory.getLog(BinCache.class);

    private final Country countryHandle;
    private AddressDAO addressDAO;    
    private SubstitutionService substitutionService;

    private final Map<Region, StateBin> stateBinCache;
    private final Map<BinType, OrderedBin> binMap;

    public BinCache(Country countryHandle, AddressDAO addressDAO, SubstitutionService substitutionService) {
        this.countryHandle = countryHandle;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;

        stateBinCache = new HashMap<Region, StateBin>();
        binMap = new HashMap<BinType, OrderedBin>();
    }

    public BinCache(Suburb suburbHandle, AddressDAO addressDAO, SubstitutionService substitutionService) {
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;

        stateBinCache = new HashMap<Region, StateBin>();
        binMap = new HashMap<BinType, OrderedBin>();
        countryHandle = null;
    }

    public StateBin getStateBin(Country aus) {
        try {
            StateBin stateBin = stateBinCache.get(aus);
            if (stateBin == null) {
                stateBin = new StateBin(aus, addressDAO);
                stateBinCache.put(aus, stateBin);
            }
            return stateBin;
        } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
            return null;
        }
    }

    protected OrderedBin createBin(BinType binType) {
        OrderedBin bin = null;
        try {
            switch (binType) {
                case LotNumberBin:
                    bin = new LotNumberBin(substitutionService);
                    break;
                case PostCodeBin:
                    bin = new GreedyPostCodeBin();
                    break;
                case StateBin:
                    bin = new StateBin(countryHandle, addressDAO);
                    break;
                case StreetNameBin:
                    bin = new GreedyStreetNameBin();
                    break;
                case StreetNumberBin:
                    bin = new StreetNumberBin(substitutionService);
                    break;
                case StreetSectionBin:
                    bin = new StreetSectionBin(substitutionService);
                    break;
                case StreetTypeBin:
                    bin = new StreetTypeBin(substitutionService);
                    break;
                case SuburbNameBin:
                    bin = new GreedySuburbNameBin();
                    break;
                case UnitNumberBin:
                    bin = new UnitNumberBin(substitutionService);
                    break;
            }
        } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
        }
        return bin;
    }

    /**
     * Pre-initialize relevant bins
     */
    public void preload() {
        if (countryHandle != null) {
            for (BinType binType : BinType.values()) {
                getBin(binType);
            }
        } else {
            for (BinType binType : BinType.valuesInSuburbContext()) {
                getBin(binType);
            }
        }
    }

    public OrderedBin getBin(BinType binType) {
        OrderedBin bin = binMap.get(binType);
        if (bin == null) {
            bin = createBin(binType);
            if (bin  != null) {
                binMap.put(binType, bin);
            }
        }
        return bin;
    }
}
