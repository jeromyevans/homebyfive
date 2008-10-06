package com.blueskyminds.business.user.model.token;

import java.util.UUID;

/**
 * Provides tokens for applications
 *
 * Date Started: 17/05/2008
 */
public class TokenProvider {

    public static String nextToken() {
        return UUID.randomUUID().toString();
    }
}
