package com.blueskyminds.homebyfive.web.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 *
 * A specialization of the UI Bean that handles open and closing tag templates
 *
 * Date Started: 11/12/2007
 * <p/>
 * History:
 */
public abstract class NakedClosingUIBean extends NakedUIBean {

    private static final Log LOG = LogFactory.getLog(NakedClosingUIBean.class);

    public NakedClosingUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    private String openTemplate;

    public abstract String getDefaultOpenTemplate();

    @StrutsTagAttribute(description="Set template to use for opening the rendered html.")
    public void setOpenTemplate(String openTemplate) {
        this.openTemplate = openTemplate;
    }

    public boolean start(Writer writer) {
        boolean result = super.start(writer);
        try {
            evaluateParams();

            mergeTemplate(writer, buildTemplateName(openTemplate, getDefaultOpenTemplate()));
        } catch (Exception e) {
            LOG.error("Could not open template", e);
            e.printStackTrace();
        }

        return result;
    }
}
