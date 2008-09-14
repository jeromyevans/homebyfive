package com.blueskyminds.housepad.web.plugin.views.jsp.ui;
        
import com.blueskyminds.housepad.web.plugin.components.Div;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A plain old html div that can be decorated 
 *
 * Date Started: 11/12/2007
 * <p/>
 * History:
 */
public class DivTag extends NakedUIBeanTag {

    @Override
    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new Div(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        Div div = (Div) this.component;
        // set parameters
    }

}
