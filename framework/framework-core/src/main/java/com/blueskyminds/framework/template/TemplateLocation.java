package com.blueskyminds.framework.template;

import java.net.URI;

/**
 * Identifies the location of a template identified by its name
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class TemplateLocation {

    private String name;
    private URI uri;
    private long lastUpdated;

    public TemplateLocation(String name, URI uri, long lastUpdated) {
        this.name = name;
        this.uri = uri;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public URI getUri() {
        return uri;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
