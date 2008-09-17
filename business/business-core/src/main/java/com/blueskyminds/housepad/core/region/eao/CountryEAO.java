package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.enterprise.region.country.CountryHandle;

import java.util.Set;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface CountryEAO {
    Set<CountryBean> listCountries();

    CountryBean lookupCountry(String path);

    CountryBean lookupCountry(CountryHandle countryHandle);
}
