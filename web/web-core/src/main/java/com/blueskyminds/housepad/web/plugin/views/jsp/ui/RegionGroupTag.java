package com.blueskyminds.housepad.web.plugin.views.jsp.ui;
        
import com.blueskyminds.housepad.web.plugin.components.Div;
import com.blueskyminds.housepad.web.plugin.components.RegionGroupComponent;
import com.blueskyminds.housepad.core.region.group.RegionGroup;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renders a region group
 *
 * Date Started: 21/04/2008
 * <p/>
 * History:
 */
public class RegionGroupTag extends NakedUIBeanTag {

    private String regionGroup;

    @Override
    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new RegionGroupComponent(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        RegionGroupComponent regionGroupComponent = (RegionGroupComponent) this.component;
        regionGroupComponent.setRegionGroup(regionGroup);
    }

    public void setRegionGroup(String regionGroup) {
        this.regionGroup = regionGroup;
    }
}