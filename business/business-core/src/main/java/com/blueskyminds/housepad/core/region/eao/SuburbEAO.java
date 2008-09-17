package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;

import java.util.Set;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface SuburbEAO {
    Set<SuburbBean> listSuburbs(String parentPath);

    Set<SuburbBean> listSuburbsInPostCode(String postCodePath);

    SuburbBean lookupSuburb(String path);

    SuburbBean lookupSuburb(SuburbHandle suburbHandle);
}
