package com.blueskyminds.housepad.web.plugin.views.jsp.ui;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.housepad.web.plugin.components.Create;
import com.blueskyminds.housepad.web.plugin.components.Update;
import com.blueskyminds.framework.tools.FileTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A specialisation of a FormTag for RESTful URLs
 *
 * Date Started: 28/04/2008
 * <p/>
 * History:
 */
public class UpdateTag extends RESTfulTag {  

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Update(stack, req, res);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

}