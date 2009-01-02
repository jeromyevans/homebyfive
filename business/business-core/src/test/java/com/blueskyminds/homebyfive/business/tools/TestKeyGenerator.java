package com.blueskyminds.homebyfive.business.tools;

import junit.framework.TestCase;

/**
 * Date Started: 30/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestKeyGenerator extends TestCase {

    public void testGenerateId() {
        assertEquals("neutral+bay", KeyGenerator.generateId("Neutral Bay"));
        assertEquals("oconner", KeyGenerator.generateId("O'Conner"));
        assertEquals("ryde", KeyGenerator.generateId("Ryde"));
        assertEquals("key-west", KeyGenerator.generateId("Key-west"));
        assertEquals("north+key-west", KeyGenerator.generateId("North Key-west"));
        assertEquals("2", KeyGenerator.generateId("02"));
        assertEquals("northwest", KeyGenerator.generateId("north/west"));
    }
    
}
