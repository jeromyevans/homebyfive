package com.blueskyminds.homebyfive.web.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

/**
 * Date Started: 16/05/2008
 * <p/>
 * History:
 */
@StrutsTag(name = "destroy", tldTagClass = "com.blueskyminds.homebyfive.web.struts2.views.jsp.ui.DestroyTag", description = "A form that renders a RESTful destory (delete) URL for the action")
public class Destroy extends RESTfulComponent implements ClientHints {

    public static final String OPEN_TEMPLATE = "destroy";
    public static final String TEMPLATE = OPEN_TEMPLATE +"-close";

    public Destroy(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
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