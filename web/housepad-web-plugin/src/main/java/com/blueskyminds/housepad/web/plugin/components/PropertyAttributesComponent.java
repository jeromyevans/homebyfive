package com.blueskyminds.housepad.web.plugin.components;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Date Started: 25/04/2008
 */
@StrutsTag(name = "propertyAttributes", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.PropertyAttributesTag", description = "Renders attributes for a Property")
public class PropertyAttributesComponent extends NakedUIBean {

    public static final String TEMPLATE = "propertyAttributes";

    private String attributes;
    private String showLabelsInViewMode;
    private String propertyURI;
//    private String propertyTypesMap;

    public PropertyAttributesComponent(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (attributes != null) {
            addParameter("attributes", findValue(attributes));
        }
        if (showLabelsInViewMode != null) {
            addParameter("showLabelsInViewMode", findValue(showLabelsInViewMode, Boolean.class));
        } else {
            addParameter("showLabelsInViewMode", true);
        }
        addParameter("propertyURI", findValue(propertyURI));
//        addParameter("propertyTypesMap", findValue(propertyTypesMap, Map.class));
    }

    @StrutsTagAttribute(description="Expression addressing the PropertyAttributes object.", required = true)
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @StrutsTagAttribute(description="If false, labels will not be included in View mode.", required = false, type="Boolean", defaultValue = "true")
    public void setShowLabelsInViewMode(String showLabelsInViewMode) {
        this.showLabelsInViewMode = showLabelsInViewMode;
    }

    @StrutsTagAttribute(description="Expression providing the base URI of the property", required = true)
    public void setPropertyURI(String propertyURI) {
        this.propertyURI = propertyURI;
    }

//    @StrutsTagAttribute(description="Expression to the Map providing Property Types", required = true)
//    public void setPropertyTypesMap(String propertyTypesMap) {
//        this.propertyTypesMap = propertyTypesMap;
//    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

}