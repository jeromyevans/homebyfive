package com.blueskyminds.homebyfive.web.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

/**
 * Date Started: 28/05/2008
 * <p/>
 * History:
 */
@StrutsTag(name = "index", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.IndexTag", description = "A form that renders a RESTful index URL for the action")
public class Index extends RESTfulComponent implements ClientHints{

    public static final String OPEN_TEMPLATE = "index";
    public static final String TEMPLATE = OPEN_TEMPLATE +"-close";


    public Index(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
    }

    public String getDefaultOpenTemplate() {
        return OPEN_TEMPLATE;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

}