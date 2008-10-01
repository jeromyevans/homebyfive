package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.homebyfive.framework.framework.patterns.PatternMatcherException;

import java.util.List;

/**
 * Can parse a plain text address into its components
 *
 * Date Started: 22/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface AddressParser {

    /**
     * Parse a plain-text address into its components
     *
     * @param plainTextAddress
     * @return
     */
    Address parseAddress(String plainTextAddress) throws PatternMatcherException;

     /**
     * Parse a plain-text address into its components and return 0 or more candidates
     *
     * @param plainTextAddress
     * @return
     */
    List<Address> parseAddress(String plainTextAddress, int maxCandidates) throws PatternMatcherException;
}
