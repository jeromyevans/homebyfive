package com.blueskyminds.homebyfive.web.struts2.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Provides standard properties for JSON result actions
 * <p/>
 * Date Started: 22/10/2007
 * <p/>
 * History:
 */
public class JSONActionSupport {

    public static final String SUCCESS = ActionSupport.SUCCESS;

    /**
     * Includes the exception message as a property (if any)
     * @return
     */
    public String getExceptionMessage() {
        ActionContext context = ActionContext.getContext();
        Object value = context.getValueStack(). findValue("exception.message");
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /** 
     * Includes the exception stack as a property (if any)
     * @return
     */
    public String getExceptionStack() {
        ActionContext context = ActionContext.getContext();
        Object value = context.getValueStack().findValue("exceptionStack");
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
