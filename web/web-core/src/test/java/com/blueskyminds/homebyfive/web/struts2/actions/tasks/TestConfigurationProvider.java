package com.blueskyminds.homebyfive.web.struts2.actions.tasks;

import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.util.location.LocatableProperties;
import com.opensymphony.xwork2.util.ValueStackFactory;
import com.opensymphony.xwork2.util.PatternMatcher;
import com.opensymphony.xwork2.util.WildcardHelper;
import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.reflection.ReflectionProvider;
import com.opensymphony.xwork2.util.reflection.ReflectionContextFactory;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.DefaultActionProxyFactory;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.DefaultTextProvider;
import com.opensymphony.xwork2.validator.*;
import com.opensymphony.xwork2.ognl.*;
import com.opensymphony.xwork2.ognl.accessor.*;
import com.opensymphony.xwork2.conversion.ObjectTypeDeterminer;
import com.opensymphony.xwork2.conversion.NullHandler;
import com.opensymphony.xwork2.conversion.impl.DefaultObjectTypeDeterminer;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.conversion.impl.InstantiatingNullHandler;
import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;
import ognl.PropertyAccessor;
import ognl.MethodAccessor;

import java.util.*;

import org.apache.struts2.views.TagLibrary;
import org.apache.struts2.views.DefaultTagLibrary;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;

/**
 * Date Started: 20/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestConfigurationProvider implements ConfigurationProvider {

    public void destroy() {
    }

    public void init(Configuration configuration) throws ConfigurationException {
    }

    public void loadPackages() throws ConfigurationException {
    }

    public boolean needsReload() {
        return false;
    }

    public void register(ContainerBuilder builder, LocatableProperties props)
            throws ConfigurationException {

        builder.factory(com.opensymphony.xwork2.ObjectFactory.class)
               .factory(ActionProxyFactory.class, DefaultActionProxyFactory.class, Scope.SINGLETON)
               .factory(ObjectTypeDeterminer.class, DefaultObjectTypeDeterminer.class, Scope.SINGLETON)
               .factory(XWorkConverter.class, Scope.SINGLETON)
               .factory(ValueStackFactory.class, OgnlValueStackFactory.class, Scope.SINGLETON)
               .factory(ValidatorFactory.class, DefaultValidatorFactory.class, Scope.SINGLETON)
               .factory(ValidatorFileParser.class, DefaultValidatorFileParser.class, Scope.SINGLETON)
               .factory(PatternMatcher.class, WildcardHelper.class, Scope.SINGLETON)
               .factory(ReflectionProvider.class, OgnlReflectionProvider.class, Scope.SINGLETON)
               .factory(ReflectionContextFactory.class, OgnlReflectionContextFactory.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, CompoundRoot.class.getName(), CompoundRootAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, Object.class.getName(), ObjectAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, Iterator.class.getName(), XWorkIteratorPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, Enumeration.class.getName(), XWorkEnumerationAccessor.class, Scope.SINGLETON)

               // silly workarounds for ognl since there is no way to flush its caches
               .factory(PropertyAccessor.class, List.class.getName(), XWorkListPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, ArrayList.class.getName(), XWorkListPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, HashSet.class.getName(), XWorkCollectionPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, Set.class.getName(), XWorkCollectionPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, HashMap.class.getName(), XWorkMapPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, Map.class.getName(), XWorkMapPropertyAccessor.class, Scope.SINGLETON)

               .factory(PropertyAccessor.class, Collection.class.getName(), XWorkCollectionPropertyAccessor.class, Scope.SINGLETON)
               .factory(PropertyAccessor.class, ObjectProxy.class.getName(), ObjectProxyPropertyAccessor.class, Scope.SINGLETON)
               .factory(MethodAccessor.class, Object.class.getName(), XWorkMethodAccessor.class, Scope.SINGLETON)
               .factory(MethodAccessor.class, CompoundRoot.class.getName(), CompoundRootAccessor.class, Scope.SINGLETON)
               .factory(NullHandler.class, Object.class.getName(), InstantiatingNullHandler.class, Scope.SINGLETON)
               .factory(ActionValidatorManager.class, AnnotationActionValidatorManager.class, Scope.SINGLETON)
               .factory(ActionValidatorManager.class, "no-annotations", DefaultActionValidatorManager.class, Scope.SINGLETON)
               .factory(TextProvider.class, "system", DefaultTextProvider.class, Scope.SINGLETON)
               .factory(OgnlUtil.class, Scope.SINGLETON)
               .factory(XWorkBasicConverter.class, Scope.SINGLETON)
               .factory(TagLibrary.class, "s", DefaultTagLibrary.class, Scope.SINGLETON)
               .factory(ActionMapper.class, "default", DefaultActionMapper.class, Scope.SINGLETON);
        props.setProperty("devMode", Boolean.FALSE.toString());
        props.setProperty("struts.enable.DynamicMethodInvocation", Boolean.FALSE.toString());
        props.setProperty("struts.enable.SlashesInActionNames", Boolean.FALSE.toString());
        props.setProperty("struts.mapper.alwaysSelectFullNamespace", Boolean.TRUE.toString());
        props.setProperty("struts.action.extension", "action");
    }
}
