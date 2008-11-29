package com.blueskyminds.homebyfive.web.struts2.actions.tasks;

import org.apache.struts2.views.freemarker.FreemarkerResult;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.util.StrutsTestCaseHelper;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockHttpServletRequest;
import freemarker.template.Template;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.mock.MockActionInvocation;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.blueskyminds.homebyfive.web.struts2.test.StrutsTestCase;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Date Started: 20/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestAvailableController extends StrutsTestCase {

    private static final Log LOG = LogFactory.getLog(TestAvailableController.class);

    private ActionContext context;
    private MockActionInvocation invocation;

    private HttpServletResponse response;
    private HttpServletRequest request;

    private StringWriter stringWriter;
    private FreemarkerManager mgr;


    /**
     * Sets up the configuration settings, XWork configuration, and
     * message resources
     */
    protected void setUp() throws Exception {
        super.setUp(new TestConfigurationProvider());        

        stringWriter = new StringWriter();        
        response = prepareResponse(stringWriter);
        request = new MockHttpServletRequest();


        context = prepareActionContext(request, response);
        
        servletContext.setAttribute(FreemarkerManager.CONFIG_SERVLET_CONTEXT_KEY, null);


        mgr = new FreemarkerManager();
        mgr.setEncoding("UTF-8");
        mgr.setContainer(container);

        invocation = prepareInvocation(context);

//        servletContext.setRealPath(new File(FreeMarkerResultTest.class.getResource(
//					"someFreeMarkerFile.ftl").toURI()).toURL().getFile());
    }

    protected void tearDown() throws Exception {
        invocation = null;
        context = null;
        request = null;
        response = null;
        stringWriter = null;
        mgr = null;

        super.tearDown();
        StrutsTestCaseHelper.tearDown();
    }
    
    public void testTemplate() throws Exception {
        AvailableController availableController = new AvailableController();

        invocation.setAction(availableController);
        
        servletContext.setAttribute(FreemarkerManager.CONFIG_SERVLET_CONTEXT_KEY, null);
//        servletContext.setRealPath(new File(FreeMarkerResultTest.class.getResource(
//					"someFreeMarkerFile.ftl").toURI()).toURL().getFile());
        ActionContext.getContext().getValueStack().push(availableController);

        FreemarkerResult result = new FreemarkerResult();
        result.setLocation("/tasks/available-success.ftl");
        result.setFreemarkerManager(mgr);
        result.setWriteIfCompleted(true);

        try {
            result.execute(invocation);
        } catch (Exception e) {
            LOG.error(e);
            fail();
        }

        LOG.info(stringWriter.toString());
    }
}
