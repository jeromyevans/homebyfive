package com.blueskyminds.homebyfive.business.user.model;

import java.security.Principal;

/**
 * Identifies an object that is owned by the Principal
 *
 * Date Started: 19/04/2008
 */
public interface Owned {

    Principal getOwnerPrincipal();
}
