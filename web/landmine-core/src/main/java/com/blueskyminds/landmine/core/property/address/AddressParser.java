package com.blueskyminds.landmine.core.property.address;

import com.blueskyminds.housepad.core.region.AddressPath;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;

import java.util.List;

/**
 * Date Started: 4/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface AddressParser {

    /**
     * Convert a batch of unparsed addresses into address paths
     *
     * @param rawAddresses
     * @return
     * @throws AddressProcessingException
     */
    List<AddressPath> parseAddresses(List<AddressPath> rawAddresses) throws AddressProcessingException;
}
