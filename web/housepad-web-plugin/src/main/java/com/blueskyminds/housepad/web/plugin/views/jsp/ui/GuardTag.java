package com.blueskyminds.housepad.web.plugin.views.jsp.ui;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.housepad.web.plugin.components.Guard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class GuardTag extends ComponentTagSupport {

    private String role;

    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new Guard(valueStack);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        Guard guard = (Guard) component;
        guard.setRole(role);
    }

    public void setRole(String role) {
        this.role = role;
    }
}

