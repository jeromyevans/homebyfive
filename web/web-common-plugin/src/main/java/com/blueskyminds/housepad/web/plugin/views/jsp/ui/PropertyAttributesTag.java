package com.blueskyminds.housepad.web.plugin.views.jsp.ui;
        
import com.blueskyminds.housepad.web.plugin.components.Div;
import com.blueskyminds.housepad.web.plugin.components.RegionGroupComponent;
import com.blueskyminds.housepad.web.plugin.components.PropertyAttributesComponent;
import com.blueskyminds.housepad.core.region.group.RegionGroup;

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
public class PropertyAttributesTag extends NakedUIBeanTag {

    private String attributes;
    private String showLabelsInViewMode;
    private String propertyURI;
//    private String propertyTypesMap;

    @Override
    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new PropertyAttributesComponent(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        PropertyAttributesComponent component = (PropertyAttributesComponent) this.component;
        component.setAttributes(attributes);
        component.setShowLabelsInViewMode(showLabelsInViewMode);
        component.setPropertyURI(propertyURI);
//        component.setPropertyTypesMap(propertyTypesMap);
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public void setShowLabelsInViewMode(String showLabelsInViewMode) {
        this.showLabelsInViewMode = showLabelsInViewMode;
    }

    public void setPropertyURI(String propertyURI) {
        this.propertyURI = propertyURI;
    }

//    public void setPropertyTypesMap(String propertyTypesMap) {
//        this.propertyTypesMap = propertyTypesMap;
//    }
}