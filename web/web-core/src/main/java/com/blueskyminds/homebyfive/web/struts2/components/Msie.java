package com.blueskyminds.homebyfive.web.struts2.components;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.apache.commons.lang.StringUtils;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;

import javax.servlet.http.HttpServletRequest;
import java.io.Writer;

/**
 * Evaluates the body if the UserAgent is msie
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
@StrutsTag(name = "msie", tldTagClass = "com.blueskyminds.housepad.web.plugin.views.jsp.ui.MsieTag", description = "Evalutes the body only in msie user agents")
public class Msie extends Component {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String MSIE = "MSIE";

    private HttpServletRequest request;
    private Float aboveVersion;
    private Float belowVersion;
    private String version;


    public Msie(ValueStack stack,HttpServletRequest request) {
        super(stack);
        this.request = request;
    }

    protected Msie() {
        super(null);
    }

    public boolean start(Writer writer) {
        boolean proceed = true;
        String userAgent = request.getHeader(USER_AGENT_HEADER);
        proceed = userAgentOkay(userAgent, version, aboveVersion, belowVersion);
        return proceed;
    }

    /**
     * Examines the User-Agent against browser type and versions
     *
     * @param userAgent
     * @return
     */
    public static boolean userAgentOkay(String userAgent, String version, Float aboveVersion, Float belowVersion) {
        boolean proceed = false;
        
        if (userAgent != null && userAgent.indexOf(MSIE) >= 0) {
            String versionString  = StringUtils.trim(StringUtils.substringBefore(StringUtils.substringAfter(userAgent, MSIE), ";"));

            if ((aboveVersion != null) || (belowVersion != null)) {
                float userAgentVersion = StringTools.extractFloat(versionString, 0.0f);

                if (aboveVersion != null) {
                    // above the specified version
                    proceed = userAgentVersion >= aboveVersion;

                    if (proceed && belowVersion != null) {
                        // above and below
                        proceed = userAgentVersion <= belowVersion;
                    }
                } else {
                    // below the specified version
                    proceed = userAgentVersion <= belowVersion;                    
                }
            } else {
                if (version != null) {
                    // string comparison - starts with
                    proceed = versionString.startsWith(version);
                } else {
                    // no version restrictions
                    proceed = true;
                }
            }
        }
        return proceed;
    }

    @StrutsTagAttribute(description="Match versions only above or equal to this version (float).", required = false)
    public void setAboveVersion(Float aboveVersion) {
        this.aboveVersion = aboveVersion;
    }

    @StrutsTagAttribute(description="Match versions only below or equal to this version (float).", required = false)
    public void setBelowVersion(Float belowVersion) {
        this.belowVersion = belowVersion;
    }

    @StrutsTagAttribute(description="Match only a version string starting with this version.", required = false)
    public void setVersion(String version) {
        this.version = version;
    }
}
