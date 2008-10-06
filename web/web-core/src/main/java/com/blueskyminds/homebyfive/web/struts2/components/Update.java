package com.blueskyminds.homebyfive.web.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

/**
 * Date Started: 28/04/2008
 * <p/>
 * History:
 */
@StrutsTag(name = "update", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.UpdateTag", description = "A form that renders a RESTful update URL for the action")
public class Update extends RESTfulComponent implements ClientHints {

    public static final String OPEN_TEMPLATE = "update";
    public static final String TEMPLATE = OPEN_TEMPLATE +"-close";
   
    public Update(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
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