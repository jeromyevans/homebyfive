package com.blueskyminds.homebyfive.web.struts2.views.jsp.ui;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.web.struts2.components.Index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A specialisation of a FormTag for RESTful URLs
 *
 * Date Started: 28/05/2008
 * <p/>
 * History:
 */
public class IndexTag extends RESTfulTag {

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Index(stack, req, res);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

}