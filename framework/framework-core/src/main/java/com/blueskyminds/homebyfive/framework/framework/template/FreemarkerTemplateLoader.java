package com.blueskyminds.homebyfive.framework.framework.template;

import com.blueskyminds.homebyfive.framework.framework.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.framework.tools.FileTools;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.net.URI;

import freemarker.cache.TemplateLoader;

/**
 * Loads freemarker templates from the classpath
 *
 * Does not attempt to cache the content of the template - the cache is left to freemarker
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class FreemarkerTemplateLoader implements TemplateLoader {

    private String basePath;
    private Map<String, TemplateLocation> templateLocations;

    public FreemarkerTemplateLoader(String basePath) {
        this.basePath = basePath;
        templateLocations= new HashMap<String, TemplateLocation>();
    }

    /**
     * Locates the named template in the classpath
     *
     * @param name
     * @return
     * @throws java.io.IOException
     */
    public Object findTemplateSource(String name) throws IOException {
        if (templateLocations.get(name) == null) {
            URI url = ResourceTools.locateResource(FileTools.concatenatePaths(basePath, name, '/'));
            if (url != null) {
                TemplateLocation templateLocation = new TemplateLocation(name, url, System.currentTimeMillis());
                templateLocations.put(name, templateLocation);
                return templateLocation;                
            } else {
                return null;
            }
        } else {
            return templateLocations.get(name);
        }
    }

    public long getLastModified(Object templateLocation) {
        return ((TemplateLocation) templateLocation).getLastUpdated();
    }

    public Reader getReader(Object templateLocation, String string) throws IOException {
        return new InputStreamReader(((TemplateLocation) templateLocation).getUri().toURL().openStream());
    }

    public void closeTemplateSource(Object object) throws IOException {
        // doesn't need to be closed.  The freemarker framework will close the reader
    }
}
