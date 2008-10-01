package com.blueskyminds.homebyfive.framework.framework.template;

import java.util.Map;

/**
 * Simple interface to a template mechanism
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public interface TemplateManager {

    /**
     * Render an output using the named template and given context
     *
     * @param templateName
     * @param context
     * @return
     */
    String process(String templateName, Map<String, Object> context) throws TemplateProcessingException;

    /**
     * Render an output using the named template and given context
     *
     * @param bodyTemplate
     * @param context
     * @return
     */
    String merge(String bodyTemplate, Map<String, Object> context) throws TemplateProcessingException;
}
