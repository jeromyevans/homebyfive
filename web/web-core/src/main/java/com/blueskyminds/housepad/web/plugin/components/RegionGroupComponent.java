package com.blueskyminds.housepad.web.plugin.components;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date Started: 21/04/2008
 */
@StrutsTag(name = "regionGroup", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.RegionGroupTag", description = "Renders a region group")
public class RegionGroupComponent extends NakedUIBean {

    public static final String TEMPLATE = "regionGroup";

    private String regionGroup;

    public RegionGroupComponent(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (regionGroup != null) {
            addParameter("regionGroup", findValue(regionGroup));
        }
    }

    @StrutsTagAttribute(description="Expression addressing the RegionGroup.", required = true)   
    public void setRegionGroup(String regionGroup) {
        this.regionGroup = regionGroup;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

}