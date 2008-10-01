package com.blueskyminds.homebyfive.framework.core.guice.providers;

import com.blueskyminds.homebyfive.framework.core.template.TemplateManager;
import com.blueskyminds.homebyfive.framework.core.template.FreemarkerTemplateManager;
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
