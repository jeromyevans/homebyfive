package com.blueskyminds.housepad.web.plugin.views.jsp.ui;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.housepad.web.plugin.components.Create;
import com.blueskyminds.housepad.web.plugin.components.RESTfulComponent;
import com.blueskyminds.framework.tools.FileTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A specialisation of a FormTag for RESTful URLs
 *
 * Date Started: 26/02/2008
 * <p/>
 * History:
 */
public abstract class RESTfulTag extends NakedUIBeanTag {

    private String resourceURI;
    private String hints;
    private String editMode;
    private String contentType;
    private String handler;

    @Override
    protected void populateParams() {
        super.populateParams();

        RESTfulComponent component = (RESTfulComponent) this.component;

        // set parameters
        if ((resourceURI != null) && (resourceURI.length() > 0)) {
            component.setResourceURI(resourceURI);
        }

        if ((hints != null) && (hints.length() > 0)) {
            component.setHints(hints);
        }

        if ((editMode != null) && (editMode.length() > 0)) {
            component.setEditMode(editMode);
        }

        if ((contentType != null) && (contentType.length() > 0)) {
            component.setContentType(contentType);
        }

        if ((handler != null) && (handler.length() > 0)) {
            component.setHandler(handler);
        }
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
}