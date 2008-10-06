package com.blueskyminds.homebyfive.web.struts2.security.token;

/**
 * Registry of current authenticated tokens
 *
 * Date Started: 6/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface TokenRegistry {

    void put(String token, Object key);

    Object get(String token);

    void release(String token);
}
