package com.blueskyminds.homebyfive.web.struts2.components;

import org.apache.struts2.views.annotations.StrutsTag;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A div with decorations
 *
 * Date Started: 11 Dec 2007
 */
@StrutsTag(name = "div", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.DivTag", description = "Renders a plain old div that's decorated by the current hteme")
public class Div extends NakedClosingUIBean {

    public static final String OPEN_TEMPLATE = "div";
    public static final String TEMPLATE = OPEN_TEMPLATE +"-close";

    public Div(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
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
