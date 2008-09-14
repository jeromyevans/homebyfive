package com.blueskyminds.enterprise.region.state;

/**
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class StateFactory {

    public StateHandle createState(String fullName, String abbreviation) {
        State state = new State(fullName, abbreviation);
        StateHandle stateHandle = new StateHandle(fullName, state, abbreviation);
        return stateHandle;
    }
}
