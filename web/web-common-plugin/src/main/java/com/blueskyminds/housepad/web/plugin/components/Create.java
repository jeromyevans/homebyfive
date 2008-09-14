package com.blueskyminds.housepad.web.plugin.components;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

/**
 * Date Started: 26/02/2008
 * <p/>
 * History:
 */
@StrutsTag(name = "create", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.CreateTag", description = "A form that renders a RESTful create URL for the action")
public class Create extends RESTfulComponent implements ClientHints {

    public static final String OPEN_TEMPLATE = "create";
    public static final String TEMPLATE = OPEN_TEMPLATE +"-close";

    public Create(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
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
