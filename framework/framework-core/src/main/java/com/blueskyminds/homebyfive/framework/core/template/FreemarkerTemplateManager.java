package com.blueskyminds.homebyfive.framework.core.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.StringTemplateLoader;

import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Provides a standard mechanism to access FreeMarker templates
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class FreemarkerTemplateManager implements TemplateManager {

    private static final String BODY = "Body Template";

    private Configuration configuration;

    public FreemarkerTemplateManager(String templateBasePath) {
        this.configuration = new Configuration();
        configuration.setTemplateLoader(new FreemarkerTemplateLoader(templateBasePath));
    }

    public FreemarkerTemplateManager() {
    }

    /**
     * Render an output using the named template and given context
     *
     * @param templateName
     * @param context
     * @return
     */
    public String process(String templateName, Map<String, Object> context) throws TemplateProcessingException {
        try {
            Template template = configuration.getTemplate(templateName);
            if (template != null) {
                StringWriter writer = new StringWriter();
                template.process(context, writer);
                return writer.toString();
            } else {
                throw new TemplateProcessingException("Could not locate template '"+templateName+"'");
            }
        } catch(IOException e) {
            throw new TemplateProcessingException("Failed to process template '"+templateName+"'", e);
        } catch (TemplateException e) {
            throw new TemplateProcessingException("Failed to process template '"+templateName+"'", e);
        }
    }

    /**
     * Render an output using the named template and given context
     *
     * @param bodyTemplate
     * @param context
     * @return
     */
    public String merge(String bodyTemplate, Map<String, Object> context) throws TemplateProcessingException {

        String result = null;
        StringWriter writer = new StringWriter();
        Template template;
        Configuration cfg = new Configuration();

        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(BODY, bodyTemplate);  // name's significant for error messages
        cfg.setTemplateLoader(templateLoader);

        try {
            template = cfg.getTemplate(BODY);
            template.process(context, writer);
            result = writer.toString();
        } catch (TemplateException e) {
            throw new TemplateProcessingException("Failed to process template", e);
        } catch (IOException e) {
            throw new TemplateProcessingException("Failed to process template", e);
        }
        return result;

    }
}
