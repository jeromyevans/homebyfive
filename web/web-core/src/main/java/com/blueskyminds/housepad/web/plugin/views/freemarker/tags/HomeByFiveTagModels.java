package com.blueskyminds.housepad.web.plugin.views.freemarker.tags;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A bean that provides access to each FreeMarker Tag Model
 * The property name matches the tag name (FreeMarker directive name)
 *
 * Date Started: 29/04/2008
 */
public class HomeByFiveTagModels {

    private ValueStack stack;
    private HttpServletRequest req;
    private HttpServletResponse res;

    public HomeByFiveTagModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }

    public AreaTagModel getArea() {
        return new AreaTagModel(stack, req, res);
    }

}
