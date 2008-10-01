package com.blueskyminds.housepad.web.plugin.components;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.framework.core.measurement.Area;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date Started: 25/04/2008
 */
@StrutsTag(name = "area", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.AreaTag", description = "Renders control to edit an Area")
public class AreaComponent extends NakedUIBean {

    public static final String TEMPLATE = "area";

    private String showLabelsInViewMode;
    private String unitsMap;

    public AreaComponent(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (showLabelsInViewMode != null) {
            addParameter("showLabelsInViewMode", findValue(showLabelsInViewMode, Boolean.class));
        } else {
            addParameter("showLabelsInViewMode", true);
        }

        if (this.name != null) {
            Area area = (Area) findValue(this.name, Area.class);
            if (area != null) {
                addParameter("nameValue", area);
            }
        }

        //addParameter("area", findValue(name, Area.class));
        //addParameter("area", findValue(name, Area.class));
        //addParameter("unitsMap", findValue(unitsMap, Map.class));
        addParameter("xUnitsMap", unitsMap);
    }

    @StrutsTagAttribute(description="Expression identifying the Area object.", required = true)
    public void setName(String name) {
        this.name = name;
    }

    @StrutsTagAttribute(description="If false, labels will not be included in View mode.", required = false, type="Boolean", defaultValue = "true")
    public void setShowLabelsInViewMode(String showLabelsInViewMode) {
        this.showLabelsInViewMode = showLabelsInViewMode;
    }

    @StrutsTagAttribute(description="Expression to the Map providing Units of Area", required = true)
    public void setUnitsMap(String unitsMap) {
        this.unitsMap = unitsMap;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

}