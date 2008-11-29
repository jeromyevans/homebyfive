package com.blueskyminds.homebyfive.web.struts2.test;

import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.config.impl.MockConfiguration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.mock.MockActionInvocation;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.logging.jdk.JdkLoggerFactory;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.mockobjects.servlet.MockHttpServletResponse;

import java.util.logging.*;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.util.StrutsTestCaseHelper;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.ServletActionContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * Base test case for JUnit testing Struts.
 */
public abstract class StrutsTestCase extends XWorkTestCase {

    protected ValueStack stack;
    protected MockServletContext servletContext;

    /**
     * Sets up the configuration settings, XWork configuration, and
     * message resources
     */
    protected void setUp(ConfigurationProvider configurationProvider) throws Exception {
        //super.setUp();
       configurationManager = new ConfigurationManager();
       configurationManager.addContainerProvider(configurationProvider);
       configuration = configurationManager.getConfiguration();
       container = configuration.getContainer();

       // Reset the value stack
       stack = container.getInstance(ValueStackFactory.class).createValueStack();
       stack.getContext().put(ActionContext.CONTAINER, container);
       ActionContext.setContext(new ActionContext(stack.getContext()));

       // clear out localization
       LocalizedTextUtil.reset();
      
        actionProxyFactory = container.getInstance(ActionProxyFactory.class);
        servletContext = new MockServletContext();

//        Dispatcher du = initDispatcher(servletContext, null);
//        configurationManager = du.getConfigurationManager();
//        configuration = configurationManager.getConfiguration();
//        container = configuration.getContainer();
    }

//     public Dispatcher initDispatcher(ServletContext ctx, Map<String,String> params) {
//        if (params == null) {
//            params = new HashMap<String,String>();
//        }
//        Dispatcher du = new Dispatcher(ctx, params);
//        du.setConfigurationManager(configurationManager);
//        du.init();
//        Dispatcher.setInstance(du);

        // Reset the value stack
//        stack = du.getContainer().getInstance(ValueStackFactory.class).createValueStack();
//        stack.getContext().put(ActionContext.CONTAINER, du.getContainer());
//        ActionContext.setContext(new ActionContext(stack.getContext()));

//        return du;
//    }

    protected void tearDown() throws Exception {
        super.tearDown();
        StrutsTestCaseHelper.tearDown();
    }

    protected MockActionInvocation prepareInvocation(ActionContext context) {
        MockActionInvocation invocation = new MockActionInvocation();
        invocation.setStack(stack);
        invocation.setInvocationContext(context);
        return invocation;
    }

    protected ActionContext prepareActionContext(HttpServletRequest request, HttpServletResponse response) {
        ActionContext context = new ActionContext(stack.getContext());
        context.put(StrutsStatics.HTTP_RESPONSE, response);
        context.put(StrutsStatics.HTTP_REQUEST, request);
        context.put(StrutsStatics.SERVLET_CONTEXT, servletContext);
        ServletActionContext.setServletContext(servletContext);
        ServletActionContext.setRequest(request);
        ServletActionContext.setResponse(response);

        return context;
    }

    protected HttpServletResponse prepareResponse(Writer writer) {

        StrutsMockHttpServletResponse response = new StrutsMockHttpServletResponse();
        response.setWriter(new PrintWriter(writer));

        return response;
    }

    static {
        ConsoleHandler handler = new ConsoleHandler();
        final SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                StringBuilder sb = new StringBuilder();
                sb.append(record.getLevel());
                sb.append(':');
                for (int x=9-record.getLevel().toString().length(); x>0; x--) {
                    sb.append(' ');
                }
                sb.append('[');
                sb.append(df.format(new Date(record.getMillis())));
                sb.append("] ");
                sb.append(formatMessage(record));
                sb.append('\n');
                return sb.toString();
            }
        };
        handler.setFormatter(formatter);
        Logger logger = Logger.getLogger("");
        if (logger.getHandlers().length > 0)
            logger.removeHandler(logger.getHandlers ()[0]);
        logger.addHandler(handler);
        logger.setLevel(Level.WARNING);
        LoggerFactory.setLoggerFactory(new JdkLoggerFactory());
    }


}

