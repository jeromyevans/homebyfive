package com.blueskyminds.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionContext;

import java.util.Map;

/**
 * Stores tokens in the SessionMap provided by the ThreadLocal ActionContext
 *
 * Date Started: 6/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SessionMapTokenRegistry implements TokenRegistry {

    public void put(String token, Object key) {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap != null) {
            sessionMap.put(token, key);
        }
    }

    public Object get(String token) {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap != null && (sessionMap.containsKey(token))) {
            return sessionMap.get(token);
        } else {
            return null;
        }
    }

    public void release(String token) {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap != null) {
            sessionMap.remove(token);
        }
    }
}
