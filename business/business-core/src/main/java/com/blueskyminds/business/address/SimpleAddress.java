package com.blueskyminds.business.address;

/**
 * Date Started: 4/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface SimpleAddress {

    String getAddress1();

    String getAddress2();

    String getSuburb();

    String getState();

    String getPostCode();

    String getCountryISO3Code();
}
