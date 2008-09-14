package com.blueskyminds.framework.guice.providers;

import com.blueskyminds.framework.template.TemplateManager;
import com.blueskyminds.framework.template.FreemarkerTemplateManager;
import com.google.inject.Provider;

/**
 * Provides a freemarker Template manager configured for the template directory of this application
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class TemplateManagerProvider implements Provider<TemplateManager> {

    public TemplateManager get() {
        return new FreemarkerTemplateManager("templates");
    }
}
