package com.blueskyminds.homebyfive.web.struts2.views.freemarker.tags;

import org.apache.struts2.views.freemarker.tags.TagModel;
import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.web.struts2.components.AreaComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date Started: 29/04/2008
 */
public class AreaTagModel extends TagModel {

    public AreaTagModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    @Override
    protected Component getBean() {
        return new AreaComponent(stack, req, res);
    }
}
