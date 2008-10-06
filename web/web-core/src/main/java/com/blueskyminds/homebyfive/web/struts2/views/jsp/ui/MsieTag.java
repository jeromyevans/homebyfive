package com.blueskyminds.homebyfive.web.struts2.views.jsp.ui;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import org.apache.commons.lang.StringUtils;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.web.struts2.components.Msie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class MsieTag extends ComponentTagSupport {

    private String aboveVersion;
    private String belowVersion;
    private String version;

    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new Msie(valueStack, httpServletRequest);
    }


    @Override
    protected void populateParams() {
        super.populateParams();

        Msie msie = (Msie) component;
        msie.setVersion(version);
        if (StringUtils.isNotBlank(aboveVersion)) {
            msie.setAboveVersion(Float.parseFloat(aboveVersion));
        }
        if (StringUtils.isNotBlank(belowVersion)) {
            msie.setBelowVersion(Float.parseFloat(belowVersion));
        }
    }


    public void setAboveVersion(String aboveVersion) {
        this.aboveVersion = aboveVersion;
    }

    public void setBelowVersion(String belowVersion) {
        this.belowVersion = belowVersion;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}