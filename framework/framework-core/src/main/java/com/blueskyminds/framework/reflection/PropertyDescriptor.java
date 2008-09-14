package com.blueskyminds.framework.reflection;

import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.tools.filters.Filter;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to access and describe properties
 *
 * Date Started: 8/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertyDescriptor {

    private static final Log LOG = LogFactory.getLog(PropertyDescriptor.class);

    private String name;
    private Method getter;
    private Method setter;
    
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";

    public PropertyDescriptor(String name, Method getter, Method setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertyDescriptor with default attributes
     */
    private void init() {
    }

    public String getName() {
        return name;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Gets the properties of the specified class, filtered using the given method filter
     *
     * @param clazz
     * @param filter
     * @return
     */
    public static List<PropertyDescriptor> getProperties(Class clazz, Filter<Method> filter) {
        List<PropertyDescriptor> propertyDescriptors = new LinkedList<PropertyDescriptor>();

        Method[] methods = clazz.getMethods();
        List<Method> filteredMethods = FilterTools.getMatching(methods, filter);

        for (Method method : filteredMethods) {
            Class returnType = method.getReturnType();
            if (Void.class.equals(returnType)) {

            } else {

            }
            String name = StringUtils.substringAfter(method.getName(), GET_PREFIX);

            try {
                Method setter = clazz.getMethod(SET_PREFIX +name, returnType);
                propertyDescriptors.add(new PropertyDescriptor(name, method, setter));
            } catch(NoSuchMethodException e) {
                // ignore - it's not a property or is not visible
            } catch(SecurityException e) {
                // ignore - it's not a property or is not visible
            }
        }

        return propertyDescriptors;
    }

    /**  Get the value of a property for the target.
     * If the target doesn't have the properties null will be returned */
    public Object getValue(Object target) {
        try {
            return getter.invoke(target);
        } catch (IllegalAccessException e) {
            LOG.debug(e);
            e.printStackTrace();
            // ignored
        } catch (IllegalArgumentException e) {
            // ignored
            LOG.debug(e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // ignored
            LOG.debug(e);
            e.printStackTrace();
        }
        return null;
    }

    /**  Get the value of a property for the target.
     * If the target doesn't have the property the call will be ignored */
    public void setValue(Object target, Object value) {
        try {
            setter.invoke(target, value);
        } catch (IllegalAccessException e) {
            // ignored
            LOG.debug(e);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // ignored
            LOG.debug(e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // ignored
            LOG.debug(e);
            e.printStackTrace();
        }
    }

    public Class<?> getReturnType() {
        return getter.getReturnType();
    }
}
