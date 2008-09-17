package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.AddressPath;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface AddressPathDAO {
    AddressPath lookupAddress(String addressString);

    AddressPath persist(AddressPath addressPath);
}
