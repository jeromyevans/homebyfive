package com.blueskyminds.homebyfive.framework.framework;

import com.blueskyminds.homebyfive.framework.framework.test.BaseTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests some of the functionality of the core (abstract) domain object
 *
 * Date Started: 8/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestDomainObject extends BaseTestCase {

    private static final Log LOG = LogFactory.getLog(TestDomainObject.class);

    public TestDomainObject(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestDomainObject with default attributes
     */
    private void init() {
    }


    // ------------------------------------------------------------------------------------------------------

    public void testMergingDomainObjects() throws Exception {
        DomainObjectExample object1 = new DomainObjectExample("Jeromy", null, null, "Evans");
        
        DomainObjectExample object2 = new DomainObjectExample(null, "Robert", null, null);

        object1.mergeWith(object2);

        assertEquals("Robert", object1.getB());
    }
}
