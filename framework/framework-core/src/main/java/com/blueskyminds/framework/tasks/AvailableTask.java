package com.blueskyminds.framework.tasks;

import com.blueskyminds.framework.reflection.PropertyDescriptor;
import com.blueskyminds.framework.reflection.SimplePropertyFilter;
import com.blueskyminds.framework.reflection.PropertySettersFilter;
import com.blueskyminds.framework.tools.filters.FilterTools;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharUtils;

/**
 * Identifies a task that's available for activation
 *
 * Date Started: 24/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AvailableTask {

    private String key;
    private Class<? extends AsynchronousTask> taskClass;
    private TaskInitializer initializer;
    private Map<String, Object> params;

    public AvailableTask(String key, Class<? extends AsynchronousTask> taskClass) {
        this.key = key;
        this.taskClass = taskClass;
        params = new HashMap<String, Object>();
    }

    public String getKey() {
        return key;
    }

    public Class<? extends AsynchronousTask> getTaskClass() {
        return taskClass;
    }

    public AvailableTask withParameter(String name, Object value) {
        params.put(name, value);
        return this;
    }    

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, Object> getAvailableParams() {
        List<String> properties = new LinkedList<String>();
        Method[] methods = taskClass.getMethods();
        List<Method> filteredMethods = FilterTools.getMatching(methods, new PropertySettersFilter());
        Map<String, Object> availableParams = new HashMap<String, Object>();

        // add all the predefined params
        availableParams.putAll(params);

        // plus all of the public setters
        for (Method setter : filteredMethods) {
            String name = StringUtils.substringAfter(setter.getName(), "set");
            if (name.length() > 0) {
                // lowercase first letter
                name = Character.toLowerCase(name.charAt(0)) + StringUtils.substring(name, 1);
                if (!availableParams.containsKey(name)) {
                    availableParams.put(name, null);
                }
            }

        }
        return availableParams;
    }
}
