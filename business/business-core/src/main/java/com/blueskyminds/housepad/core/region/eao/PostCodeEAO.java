package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.model.PostCodeBean;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;

import java.util.Set;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface PostCodeEAO {
    Set<PostCodeBean> listPostCodes(String parentPath);

    PostCodeBean lookupPostCode(String path);

    PostCodeBean lookupPostCode(PostCodeHandle postCodeHandle);
}
