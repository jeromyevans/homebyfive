package com.blueskyminds.homebyfive.framework.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.matcher.Matcher;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.jpa.JpaUnit;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.Properties;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * An extension to the Guice AbstractModule that supports injection into method interceptors.
 * Relevant for Guice 1.0 but likely to be superseded in Guice 1.1
 * <p/>
 * Based on the code at: http://tembrel.blogspot.com/2007/09/injecting-method-interceptors-in-guice.html
 * <p/>
 * Injection of the Injector into this module itself is used to trigger injection into the method interceptors. 
 * Date Started: 24/10/2007
 * <p/>
 * History:
 */
public abstract class ExtendedGuiceModule extends AbstractModule {

    private static final Log LOG = LogFactory.getLog(ExtendedGuiceModule.class);

    private static final String PERSISTENCE_UNIT_PROPERTY = "jpa.persistenceUnit";

    private final Set<Object> toBeInjected = new HashSet<Object>();

    private boolean selfInjectionInitialized = false;

    /**
     * Overridden version of bindInterceptor that,
     * in addition to the standard behavior,
     * arranges for field and method injection of
     * each MethodInterceptor in {@code interceptors}.
     */
    @Override
    protected void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptor... interceptors) {
        registerForInjection(interceptors);
        super.bindInterceptor(classMatcher, methodMatcher, interceptors);
    }

    /**
     * Arranges for this module and each of the given
     * objects (if any) to be field and method injected
     * when the Injector is created. It is safe to call
     * this method more than once, and it is safe
     * to call it more than once on the same object(s).
     */
    protected <T> void registerForInjection(T... objects) {
        ensureSelfInjection();
        if (objects != null) {
            for (T object : objects) {
                if (object != null) {
                    toBeInjected.add(object);
                }
            }
        }
    }

    @Inject
    private void injectRegisteredObjects(Injector injector) {
        for (Object injectee : toBeInjected) {
            injector.injectMembers(injectee);
        }
    }

    private void ensureSelfInjection() {
        if (!selfInjectionInitialized) {
            bind(ExtendedGuiceModule.class)
                    //.annotatedWith(createUniqueAnnotation())
                    .toInstance(this);
            selfInjectionInitialized = true;
        }
    }


    /**
     * Bind the properties to constants using Names.named(key,value)
     *
     * @param properties
     */
    protected void bindConstants(Properties properties) {
        for (Map.Entry entry : properties.entrySet()) {
            bindConstant().annotatedWith(Names.named((String) entry.getKey())).to((String) entry.getValue());
        }
    }

    /**
     * Create the InitialContext using the supplied properties
     *
     * @param properties  env for the InitialContext, unless null
     */
    protected void bindInitialContext(Properties properties) {
        try {
            if (properties != null) {
                bind(Context.class).toInstance(new InitialContext(properties));
            } else {
                bind(Context.class).toInstance(new InitialContext());
            }
        } catch (NamingException e) {
            throw new RuntimeException("Error creating the JNDI InitialContext.  Initialisation has failed", e);
        }
    }

    protected void bindWarpPersist(UnitOfWork unitOfWork, Properties properties) {
        String persistenceUnit = (String) properties.get(PERSISTENCE_UNIT_PROPERTY);
        LOG.info("   Setting up warp-persist across "+unitOfWork+" and persistance unit: "+ persistenceUnit);
        install(PersistenceService.usingJpa().across(UnitOfWork.TRANSACTION).buildModule());
        bindConstant().annotatedWith(JpaUnit.class).to(persistenceUnit);
    }
}
