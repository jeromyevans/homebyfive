package com.blueskyminds.homebyfive.web.struts2.test;

import com.mockobjects.servlet.MockHttpSession;

import java.util.Hashtable;
import java.util.Enumeration;

/*
 * $Id: StrutsMockHttpSession.java 651946 2008-04-27 13:41:38Z apetrelli $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


/**
 * StrutsMockHttpSession
 *
 */
public class StrutsMockHttpSession extends MockHttpSession {

    Hashtable attributes = new Hashtable();


    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    public Enumeration getAttributeNames() {
        return attributes.keys();
    }

    public void setExpectedAttribute(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void setExpectedRemoveAttribute(String s) {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    public void setupGetAttribute(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void setupGetAttributeNames(Enumeration enumeration) {
        throw new UnsupportedOperationException();
    }
}
