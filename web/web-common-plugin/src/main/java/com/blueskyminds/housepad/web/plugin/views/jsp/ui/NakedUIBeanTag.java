package com.blueskyminds.housepad.web.plugin.views.jsp.ui;

import com.blueskyminds.housepad.web.plugin.ThemedTag;
import com.blueskyminds.housepad.web.plugin.components.NakedUIBean;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Date Started: 11/12/2007
 * <p/>
 * History:
 */
public abstract class NakedUIBeanTag extends ComponentTagSupport implements DynamicAttributes, ThemedTag {

    protected String id;    
    protected String key;
    protected String cssClass;
    protected String cssStyle;

    protected String template;
    protected String theme;
    protected String templateDir;

    protected Map<String,Object> dynamicAttributes = new HashMap<String,Object>();

    protected void populateParams() {
        super.populateParams();

        NakedUIBean uiBean = (NakedUIBean) component;
        uiBean.setId(id);
        uiBean.setKey(key);

        uiBean.setCssClass(cssClass);
        uiBean.setCssStyle(cssStyle);

        uiBean.setTemplate(template);
        uiBean.setTheme(theme);
        uiBean.setTemplateDir(templateDir);

        uiBean.setDynamicAttributes(dynamicAttributes);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        dynamicAttributes.put(localName, value);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }
}
