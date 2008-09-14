package com.blueskyminds.framework.security;

import java.security.Principal;

/**
 * A Principal with roles
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
public interface PrincipalRoles extends Principal {

    String[] getRoles();

    boolean hasRole(String roleName);
}
