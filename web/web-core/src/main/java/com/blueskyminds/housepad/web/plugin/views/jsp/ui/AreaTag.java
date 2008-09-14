package com.blueskyminds.housepad.web.plugin.views.jsp.ui;
        
import com.blueskyminds.housepad.web.plugin.components.PropertyAttributesComponent;
import com.blueskyminds.housepad.web.plugin.components.AreaComponent;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renders property attributes
 *
 * Date Started: 25/04/2008
 * <p/>
 * History:
 */
public class AreaTag extends NakedUIBeanTag {

    private String name;
    private String showLabelsInViewMode;
    private String unitsMap;

    @Override
    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new AreaComponent(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        AreaComponent component = (AreaComponent) this.component;
        component.setName(name);
        component.setShowLabelsInViewMode(showLabelsInViewMode);
        component.setUnitsMap(unitsMap);
        component.setKey(key);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShowLabelsInViewMode(String showLabelsInViewMode) {
        this.showLabelsInViewMode = showLabelsInViewMode;
    }

    public void setUnitsMap(String unitsMap) {
        this.unitsMap = unitsMap;
    }
}