package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.Score;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;
import com.blueskyminds.homebyfive.business.region.service.StreetService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.blueskyminds.homebyfive.business.address.*;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;

import java.util.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A plain text address parser that uses a depth-first algorithm to find the best-matched address.  This algorithm
 * signficantly reduces the number of possible combinations of addresses compared to the default PatternMatcher
 * as it only allows valid sequences of pattern bins.
 *
 * Date Started: 16/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DepthFirstAddressParser implements AddressParser {

    private static final Log LOG = LogFactory.getLog(DepthFirstAddressParser.class);

    private AddressService addressService;
    private AddressDAO addressDAO;
    private SubstitutionService substitutionService;
    private BinCache binCache;

    private PhraseToBinAllocationSequence bestSequence;

    private final Country countryHandle;
    private final Suburb suburbHandle;
    private StateService stateService;
    private SuburbService suburbService;
    private PostalCodeService postalCodeService;
    private StreetService streetService;

    public DepthFirstAddressParser(AddressService addressService, AddressDAO addressDAO, SubstitutionService substitutionService, Country countryHandle, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService, StreetService streetService) {
        this.addressService = addressService;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;
        this.countryHandle = countryHandle;
        this.suburbHandle = null;
        this.stateService = stateService;
        this.suburbService = suburbService;
        this.postalCodeService = postalCodeService;
        this.streetService = streetService;
    }

    public DepthFirstAddressParser(AddressService addressService, AddressDAO addressDAO, SubstitutionService substitutionService, Suburb suburbHandle, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService, StreetService streetService) {
        this.addressService = addressService;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;
        this.countryHandle = null;
        this.suburbHandle = suburbHandle;
        this.stateService = stateService;
        this.suburbService = suburbService;
        this.postalCodeService = postalCodeService;
        this.streetService = streetService;
    }

    public List<Address> parseAddress(String plainTextAddress, int maxCandidates) {


        LOG.info("Parsing "+plainTextAddress);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if (countryHandle != null) {
            binCache = new BinCache(countryHandle, stateService, substitutionService);
        } else {
            binCache = new BinCache(suburbHandle, stateService, substitutionService);
        }

        binCache.preload();

        stopWatch.stop();
        if (LOG.isDebugEnabled()) {
            LOG.debug("init:"+stopWatch.toString());
        }
        stopWatch.reset();
        stopWatch.start();

        // split it into phrases
        PhraseList phraseList = new PhraseList(plainTextAddress);

        Chain initialChain = createInitialStateChain(phraseList);

        List<PhraseToBinAllocationSequence> sequences = initialChain.processNext();

        stopWatch.stop();
        if (LOG.isDebugEnabled()) {
            LOG.debug("parse:"+stopWatch.toString());
            LOG.debug(sequences.size()+" sequences created");
        }
        stopWatch.reset();
        stopWatch.start();
        List<CandidateAllocation> bestCandidates = scoreSequences(phraseList, sequences);
        List<Address> addresses = new LinkedList<Address>();
        if (bestCandidates.size() > 0) {
            // get the first valid address
            for (CandidateAllocation candidate : bestCandidates) {
                // convert the candidate into an address
                Address address = extractAddress(candidate, plainTextAddress);
                if (address != null) {
                    addresses.add(address);
                    if (addresses.size() >= maxCandidates) {
                        break;
                    }
                }
            }
        }

        stopWatch.stop();
        if (LOG.isDebugEnabled()) {
            LOG.debug("match:"+stopWatch.toString());
        }
        LOG.info("   parsing "+plainTextAddress+" required: "+stopWatch.toString());

        return addresses;
    }

    public Address parseAddress(String inputString) {
        List<Address> candidates = parseAddress(inputString, 1);
        if (candidates.size() > 0) {
            return candidates.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Convert the CandidateAllocation into an Address instance
     * @param candidate
     * @param sourceText   the source (for debugging)
     * @return
     */
    private Address extractAddress(CandidateAllocation candidate, String sourceText) {
        StreetAddress streetAddress = null;
        String streetName = null;

        // extract all of the components
        String unitNumber = candidate.extractValue(UnitNumberBin.class);
        String streetNumber = candidate.extractValue(StreetNumberBin.class);
        String lotNumber = candidate.extractValue(LotNumberBin.class);
        StreetType streetType = candidate.extractMetadata(StreetType.class, StreetTypeBin.class);

        State state = candidate.extractMetadata(State.class, StateBin.class);

        StreetSection streetSection = candidate.extractMetadata(StreetSection.class, StreetSectionBin.class);
        if (streetSection == null) {
            streetSection = StreetSection.NA;
        }

        streetName = WordUtils.capitalizeFully(candidate.extractValue(GreedyStreetNameBin.class));
        String suburbName = WordUtils.capitalizeFully(candidate.extractValue(GreedySuburbNameBin.class));
        String postCodeName = candidate.extractValue(GreedyPostCodeBin.class);

        //String postCodeName = candidate.extractValue(PostCodeBin.class);
        Suburb suburb = null;
        PostalCode postCode = null;
        Street street = null;

        if (suburbHandle == null) {
            if (StringUtils.isNotBlank(suburbName)) {
                List<Suburb> suburbCandidates;
                if (state != null) {
                    suburbCandidates = suburbService.find(suburbName, state);
                } else {
                    suburbCandidates = suburbService.find(suburbName, Countries.AU);
                }
                if (suburbCandidates.size() > 0 && postCodeName != null && state != null) {
                    // confirm that the suburb is consistent with the postcode if present
                    postCode = postalCodeService.lookup(postCodeName, state);
                    if (postCode != null) {
                        boolean okay = false;
                        for (Suburb suburbCandidate : suburbCandidates) {
                            // check if the post code is a a parent
                            if (postCode.hasChild(suburbCandidate)) {
                                okay = true;
                                suburb = suburbCandidate;
                                break;
                            }
                        }
                    } else {
                        // no post code - use the best suburb match
                        suburb = suburbCandidates.iterator().next();
                    }
                } else {
                    if (suburbCandidates.size() > 0) {
                        // no post code - use the best suburb match
                        suburb = suburbCandidates.iterator().next();
                    }
                }
            }
        } else {
            // suburb is predetermined
            suburb = suburbHandle;
        }

        if (suburb != null) {
            if (streetName != null) {
                // lookup the street
                List<Street> streetCandidates = streetService.find(streetName, streetType, streetSection, suburb);

                if (streetCandidates.size() > 0) {
                    street = streetCandidates.iterator().next();
                } else {
                    // the street doesn't exist - create a new instance
                    street = new Street(streetName, streetType, streetSection);
                }
            }

            if (street != null) {
                if (unitNumber == null) {
                    if (lotNumber == null) {
                        // create the street address
                        streetAddress = new StreetAddress(streetNumber, street, suburb, postCode);
                        ((StreetAddress) streetAddress).setSourceText(sourceText);
                    } else {
                        // create the lot address
                        streetAddress = new LotAddress(lotNumber, streetNumber, street, suburb, postCode);
                        ((LotAddress) streetAddress).setSourceText(sourceText);
                    }
                } else {
                    // create the unit address
                    streetAddress = new UnitAddress(unitNumber, streetNumber, street, suburb, postCode);
                    ((UnitAddress) streetAddress).setSourceText(sourceText);
                }
            }
        }

        return streetAddress;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Iterates through all of the candidates and scores them using the scoring strategy */
    private List<CandidateAllocation> scoreSequences(PhraseList phraseList, List<PhraseToBinAllocationSequence> sequences) {
        // getScore and sort the candidates.
        Score bestScore = new Score(Integer.MIN_VALUE);
        Score thisScore;
        ScoringStrategy scoringStrategy = new GreedyAddressScoringStrategy();
        
        List<CandidateAllocation> bestCandidates = new LinkedList<CandidateAllocation>();

        // calculate the scores for all candidates and remember those with the best score
        for (PhraseToBinAllocationSequence sequence : sequences) {
            CandidateAllocation allocation = new CandidateAllocation(sequence, scoringStrategy);
            thisScore = allocation.calculateScore(phraseList.getNoOfWords());

            if (thisScore.compareTo(bestScore) > 0) {
                // new best score - create a new list of the best candidates
                bestCandidates.clear();
                bestCandidates.add(allocation);
                // remember the best score
                bestScore = thisScore;
            } else {
                if (thisScore.compareTo(bestScore) == 0) {
                    // remember this is one of the equal-best
                    bestCandidates.add(allocation);
                }
            }
        }

        return bestCandidates;
    }
    

    private Chain createInitialStateChain(PhraseList phraseList) {
        Chain initial = new Chain();
        initial.phraseList = phraseList;
        initial.sequence = new PhraseToBinAllocationSequence((PhraseToBinAllocation) null);
        
        if (countryHandle != null) {
            initial.binsAllowed.addAll(Arrays.asList(BinType.valuesInCountryContext()));
        } else {
            initial.binsAllowed.addAll(Arrays.asList(BinType.valuesInSuburbContext()));
        }
        bestSequence = new PhraseToBinAllocationSequence((PhraseToBinAllocation) null);
        return initial;
    }

    private class Chain {

        private PhraseList phraseList;
        private List<BinType> binsAllowed;
        private PhraseToBinAllocationSequence sequence;
        private List<BinType> binsChecked;
        
        private Chain(PhraseList phraseList, List<BinType> binsAllowed, List<BinType> binsChecked, PhraseToBinAllocationSequence sequence) {
            this.phraseList = phraseList;
            this.sequence = sequence;
            this.binsAllowed = new ArrayList<BinType>(binsAllowed);            
            this.binsChecked = new ArrayList<BinType>(binsChecked);
        }

        private Chain() {
            binsAllowed = new LinkedList<BinType>();
            binsChecked = new LinkedList<BinType>();
        }


        public List<PhraseToBinAllocationSequence> processNext() {
//            String prefix = StringTools.fillRepeat("   ", sequence.size());

            List<PhraseToBinAllocationSequence> sequencesCreated = new LinkedList<PhraseToBinAllocationSequence>();

            if (binsAllowed.size() > 0) {

                for (BinType binType : binsAllowed) {
 //                   LOG.info(prefix+binType+" "+phraseList.getLongestPhrase());

                    binsChecked.add(binType);

                    OrderedBin bin = binCache.getBin(binType);
                    if (bin != null) {

                        Collection<PhraseToBinAllocation> allocations = bin.matchPhrasesCompatibleWith(phraseList, sequence);

                        for (PhraseToBinAllocation allocation : allocations) {

                            PhraseToBinAllocationSequence nextSequence = sequence.cloneAndExtend(allocation);
                          //  LOG.info(prefix+allocation.getBin().getClass().getSimpleName()+" "+allocation.getPhrase());

                            PhraseList leftPhrases;
                            PhraseList rightPhrases;

                            if (allocation.isExclusive()) {
                                // split the phrases at this position, removing the phrase
                                leftPhrases = phraseList.trimRight(allocation.getPhrase());
                                rightPhrases = phraseList.trimLeft(allocation.getPhrase());
                            } else {
                                leftPhrases = phraseList.trimRightKeeping(allocation.getPhrase());
                                rightPhrases = phraseList.trimLeftKeeping(allocation.getPhrase());
                            }

                            List<PhraseToBinAllocationSequence> leftSequences = null;
                            if (leftPhrases.size() > 0) {
                                List<BinType> leftBins = intersection(bin.getBinTypesAllowedLeft());
                                if (leftBins.size() > 0) {
                                    Chain leftBranch = new Chain(leftPhrases, leftBins, binsChecked, nextSequence);
                                    leftSequences = leftBranch.processNext();
                                }
                            }

                            boolean leftEmpty = false;

                            // now we have searched left, continue the sequences searching right
                            // if there's no left sequences, continue frmo the current sequence only
                            if (leftSequences == null || leftSequences.size() == 0) {
                                leftSequences = new LinkedList<PhraseToBinAllocationSequence>();
                                leftSequences.add(nextSequence);
                                leftEmpty = true;
                            }

                            List<PhraseToBinAllocationSequence> rightSequences = new LinkedList<PhraseToBinAllocationSequence>();
                            // now continue the left sequences to the right
                            if (rightPhrases.size() > 0) {
                                List<BinType> rightBins = intersection(bin.getBinTypesAllowedRight());
                                if (rightBins.size() > 0) {
                                    for (PhraseToBinAllocationSequence leftSequence : leftSequences) {
                                        Chain rightBranch = new Chain(rightPhrases, rightBins, binsChecked, leftSequence);
                                        List<PhraseToBinAllocationSequence> sequencesContinued = rightBranch.processNext();
                                        if (sequencesContinued.size() > 0) {
                                            sequencesCreated.addAll(sequencesContinued);
                                        } else {
                                            // no sequences were continued but we can retain the left sequence
                                            sequencesCreated.add(leftSequence);                                            
                                        }
                                    }
                                } else {
                                    sequencesCreated.addAll(leftSequences);

                                }
                            } else {
                                sequencesCreated.addAll(leftSequences);

                            }

                            // for now, let's assume the best sequence is the longest
                            if (nextSequence.size() > bestSequence.size()) {
                                bestSequence = nextSequence;
                            }
                        }
                    }
                }
            }
            return sequencesCreated;
        }

        /**
         * Calculate the intersection of the array of bin types with the current list of allowed in types
         *
         * The BinType's already checked are never included
         *
         * @param other
         * @return
         */
        private List<BinType> intersection(BinType[] other) {
            List<BinType> intersection = new LinkedList<BinType>();
            for (BinType binType : binsAllowed) {
                if (!binsChecked.contains(binType)) {
                    if (ArrayUtils.contains(other, binType)) {
                        intersection.add(binType);
                    }
                }
            }
            return intersection;
        }
        
    }    
   

}
