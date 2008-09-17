package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.housepad.core.region.model.StateBean;
import com.blueskyminds.enterprise.region.state.StateHandle;

import java.util.Set;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface StateEAO {
    Set<StateBean> listStates(String parentPath);

    StateBean lookupState(String path);

    StateBean lookupState(StateHandle stateHandle);
}
