package com.blueskyminds.framework.template;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.framework.tools.LoggerTools;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;
import java.util.HashMap;


/**
 * Tests reading a freemarker template and rendering the output
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class TestTemplate extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestTemplate.class);

    protected void setUp() throws Exception {
        super.setUp();
        LoggerTools.configure();
    }

    public void testFreemarkerTemplate() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new FreemarkerTemplateLoader("templates"));
        Template template = cfg.getTemplate("contactrequest.ftl");
        StringWriter writer = new StringWriter();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "Jeromy Evans");
        context.put("email", "jeromy.evans@blueskyminds.com.au");
        context.put("message", "This is a test message");
        template.process(context, writer);
        LOG.info(writer.toString());
    }
}
