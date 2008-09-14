package com.blueskyminds.housepad.web.plugin.components;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTagSkipInheritance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A base class for UI Bean that removes the useless attributes inherited from the base class annotations
 *
 * A NakedUIBean starts with the following attributes:
 *   id
 *   cssClass
 *   key
 *   theme
 *   templateDir
 *   template
 *
 * Date Started: 11/12/2007
 * <p/>
 * History:
 */
public abstract class NakedUIBean extends UIBean {

    protected NakedUIBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTitle(String s) {
        super.setTitle(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setDisabled(String s) {
        super.setDisabled(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setLabel(String s) {
        super.setLabel(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setLabelSeparator(String s) {
        super.setLabelSeparator(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setLabelposition(String s) {
        super.setLabelposition(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setRequiredposition(String s) {
        super.setRequiredposition(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setName(String s) {
        super.setName(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setRequired(String s) {
        super.setRequired(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTabindex(String s) {
        super.setTabindex(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setValue(String s) {
        super.setValue(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setAccesskey(String s) {
        super.setAccesskey(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTooltip(String s) {
        super.setTooltip(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTooltipConfig(String s) {
        super.setTooltipConfig(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setJavascriptTooltip(String s) {
        super.setJavascriptTooltip(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTooltipCssClass(String s) {
        super.setTooltipCssClass(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTooltipDelay(String s) {
        super.setTooltipDelay(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setTooltipIconPath(String s) {
        super.setTooltipIconPath(s);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnclick(String string) {
        super.setOnclick(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOndblclick(String string) {
        super.setOndblclick(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnmousedown(String string) {
        super.setOnmousedown(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnmouseup(String string) {
        super.setOnmouseup(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnmouseover(String string) {
        super.setOnmouseover(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnmousemove(String string) {
        super.setOnmousemove(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnmouseout(String string) {
        super.setOnmouseout(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnfocus(String string) {
        super.setOnfocus(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnblur(String string) {
        super.setOnblur(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnkeypress(String string) {
        super.setOnkeypress(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnkeydown(String string) {
        super.setOnkeydown(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnkeyup(String string) {
        super.setOnkeyup(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnselect(String string) {
        super.setOnselect(string);
    }

    @Override
    @StrutsTagSkipInheritance
    public void setOnchange(String string) {
        super.setOnchange(string);
    }

    /**
     * Evaluates the OGNL expression
     *
     * @param expr OGNL expression.
     * @return the String value found.
     */
    public String evaluateExpression(String expr) {
        return super.findString(expr);
    }

}
