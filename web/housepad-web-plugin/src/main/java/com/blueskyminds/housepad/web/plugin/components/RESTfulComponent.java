package com.blueskyminds.housepad.web.plugin.components;

import org.apache.struts2.dispatcher.RequestMap;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.blueskyminds.framework.tools.FileTools;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Common behaviour for the RESTful components
 * <p/>
 * Date Started: 28/05/2008
 */
public abstract class RESTfulComponent extends NakedClosingUIBean {

    private String resourceURI;
    private String hints;
    private String editMode;
    private String contentType;
    private String handler;

    protected RESTfulComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    /**
     * Obtain the resource URI from the containing path
     * @return
     */
    protected StringBuilder buildResourceURI() {
        StringBuilder uri = new StringBuilder();

        if (resourceURI == null) {
            // use the containing folder URI
            RequestMap requestMap = (RequestMap) stack.getContext().get("request");
            String forwardURI = (String) requestMap.get("javax.servlet.forward.request_uri");
            if ((forwardURI != null) && (forwardURI.length() > 0)) {
                if (isRESTSubcommand(forwardURI)) {
                    uri.append(FileTools.containingFolder(forwardURI, false));
                } else {
                   uri.append(forwardURI);
                }
            } else {
                if (isRESTSubcommand(forwardURI)) {
                    uri.append(FileTools.containingFolder(request.getRequestURI(), false));
                } else {
                    uri.append(request.getRequestURI());
                }
            }
        } else {
            uri.append(resourceURI);
        }

        return uri;
    }

    private boolean isRESTSubcommand(String forwardURI) {
        return (forwardURI.endsWith("new")) || (forwardURI.endsWith("edit"));
    }

    protected void setupDefaultRESTParams() {
        StringBuilder uri = buildResourceURI();
        if (contentType != null) {
            String contentTypeEvaluated = findString(contentType);
            if (contentTypeEvaluated != null && contentTypeEvaluated.length() > 0) {
                uri.append("."+contentTypeEvaluated);
            } else {
                uri.append("."+contentType);
            }
        }

        if (hints != null) {
            String hintsEvaluated = findString(hints);
            if (hintsEvaluated != null && hintsEvaluated.length() > 0) {
                uri.append(hintsEvaluated);
            }
        }

        addParameter("resourceURI", uri.toString());

        if (editMode != null) {
            Boolean editModeEvaluated = (Boolean) findValue(editMode, Boolean.class);
            if (editModeEvaluated != null) {
                addParameter("editMode", editModeEvaluated);
            }
        }

        if (handler != null) {
            String handlerEvaluated = findString(handler);
            if (handlerEvaluated != null && handlerEvaluated.length() > 0) {
                addParameter("handler", handler);
            }
        }
    }

    public void evaluateParams() {
        super.evaluateParams();
        setupDefaultRESTParams();
    }

    @StrutsTagAttribute(description="The URI of the resource.", required = false)
    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    @StrutsTagAttribute(description="Hints for the client about how to request and what to do with the result", required = false)
    public void setHints(String hints) {
        this.hints = hints;
    }

    @StrutsTagAttribute(description="Override the edit mode. default=false", required = false)
    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    @StrutsTagAttribute(description="Content type of the response [json|xml]", required = false)
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @StrutsTagAttribute(description="Name of the client-side handler", required = false)
    public void setHandler(String handler) {
        this.handler = handler;
    }
}
