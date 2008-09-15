package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.Bin;
import com.blueskyminds.framework.patterns.PhraseToBinAllocation;
import com.blueskyminds.framework.patterns.PhraseList;
import com.blueskyminds.framework.patterns.PhraseToBinAllocationSequence;

import java.util.List;

/**
 * An extension to the Bin that recognizes the order of Bins
 *
 * Date Started: 16/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface OrderedBin {

    BinType[] getBinTypesAllowedLeft();
    BinType[] getBinTypesAllowedRight();

    /**
     * Lists any of the phrases that match this bin and are compatible with the sequence.  The state of the bin is not affected.
     *
     * @return the list of allocations
     **/
    List<PhraseToBinAllocation> matchPhrasesCompatibleWith(PhraseList phraseList, PhraseToBinAllocationSequence sequence);

}
