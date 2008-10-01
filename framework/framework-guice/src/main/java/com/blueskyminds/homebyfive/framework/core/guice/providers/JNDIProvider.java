package com.blueskyminds.homebyfive.framework.core.guice.providers;

/**
 * A Guice provider that looks up a class of the specified type with the given JNDI name
 *
 * Date Started: 8/08/2007
 * <p/>
 * History:
 */

import com.google.inject.*;

import javax.naming.*;

public class JNDIProvider<T> implements Provider<T> {

    @Inject
    private Context context;
    private final String name;
    private final Class<T> type;

    public JNDIProvider(Class<T> type, String name) {
        this.name = name;
        this.type = type;
    }

    public T get() {
        try {
            return type.cast(context.lookup(name));
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a JNDI provider for the given
     * type and name.
     */
    public static <T> Provider<T> fromJndi(Class<T> type, String name) {
        return new JNDIProvider<T>(type, name);
    }
}
