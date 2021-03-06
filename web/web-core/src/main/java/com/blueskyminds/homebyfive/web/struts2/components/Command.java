package com.blueskyminds.homebyfive.web.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.apache.struts2.views.annotations.StrutsTag;

/**
 * Date Started: 7/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@StrutsTag(name = "guard", tldTagClass = "ccom.blueskyminds.homebyfive.web.struts2.views.jsp.ui.CommandTag", description = "A command?")
public class Command extends NakedUIBean {

    public static final String TEMPLATE = "command";

    private String role;

    public Command(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();

        if (role != null) {
            String roleEvaluated = findString(role);
            if (roleEvaluated != null && roleEvaluated.length() > 0) {
                addParameter("role", roleEvaluated);
            }
        }

    }

    @StrutsTagAttribute(description="The role the Principal must have to enable the command", required = false)    
    public void setRole(String role) {
        this.role = role;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }
}
