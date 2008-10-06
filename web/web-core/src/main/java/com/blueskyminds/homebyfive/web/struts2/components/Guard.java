package com.blueskyminds.homebyfive.web.struts2.components;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTag;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.framework.core.security.PrincipalRoles;

import java.io.Writer;
import java.security.Principal;

/**
 *  Only evaluates the body only if the principal has the specified role
 */
@StrutsTag(name = "guard", tldTagClass = "ccom.blueskyminds.homebyfive.web.struts2.views.jsp.ui.GuardTag", description = "Renders the body only if the principal has the specified role")
public class Guard extends Component {

    private String role;

    public Guard(ValueStack stack) {
        super(stack);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean start(Writer writer) {
        //stack.get
        Principal principal = (Principal) findValue("principal");
        if (principal != null) {
            if (principal instanceof PrincipalRoles) {
                return ((PrincipalRoles) principal).hasRole(role);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
