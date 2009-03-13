package com.blueskyminds.homebyfive.framework.core.guice.providers;

/**
 * A Guice provider that looks up a class of the specified type with the given JNDI name
 *
 * Uses the Context identified by a Key, otherwise any Context instance injected
 *
 * Date Started: 8/08/2007
 * <p/>
 * History:
 */

import com.google.inject.*;

import javax.naming.*;

public class JNDIProvider<T> implements Provider<T> {

    @Inject Injector injector;
    private final String name;
    private final Class<T> type;
    private Key<Context> contextKey;

    public JNDIProvider(Class<T> type, Key<Context> contextKey, String name) {
        this.name = name;
        this.type = type;
        this.contextKey = contextKey;
    }

     public JNDIProvider(Class<T> type, String name) {
        this.name = name;
        this.type = type;
    }

    public T get() {
        try {
            Context context;
            if (contextKey != null) {
                context = injector.getInstance(contextKey);
            } else {
                context = injector.getInstance(Context.class);
            }
            return type.cast(context.lookup(name));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JNDI provider for the given type, key and name
     */
    public static <T> Provider<T> fromJndi(Class<T> type, String name) {
        return new JNDIProvider<T>(type, name);
    }

    /**
     * Creates a JNDI provider for the given type, key and name
     * @param key identifies which Context to use 
     */
    public static <T> Provider<T> fromJndi(Class<T> type, Key<Context> key, String name) {
        return new JNDIProvider<T>(type, key, name);
    }
}
