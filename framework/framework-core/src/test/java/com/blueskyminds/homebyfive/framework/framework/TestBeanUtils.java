package com.blueskyminds.homebyfive.framework.framework;

import com.blueskyminds.homebyfive.framework.framework.test.BaseTestCase;
import com.blueskyminds.homebyfive.framework.framework.reflection.MergeablePropertyFilter;
import com.blueskyminds.homebyfive.framework.framework.recurrence.Recurrence;
import com.blueskyminds.homebyfive.framework.framework.tools.filters.FilterTools;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestBeanUtils extends BaseTestCase {

    private static final Log LOG = LogFactory.getLog(TestBeanUtils.class);
    private static final String GET_PREFIX = "get";

    public TestBeanUtils(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestBeanUtils with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public void testPropertyUtils() {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(AbstractDomainObject.class);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            LOG.info(propertyDescriptor.getDisplayName());
        }
    }

    public void testReflection() throws Exception {
        Method[] methods = Recurrence.class.getMethods();

        List<Method> properties = FilterTools.getMatching(methods, new MergeablePropertyFilter());

        for (Method method : properties) {
            LOG.info(method.getName());
        }
    }

}
