package com.blueskyminds.homebyfive.web.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 10/05/2008
 */
public class CompositeTokenInspector implements TokenInspector {

    private static final Log LOG = LogFactory.getLog(CompositeTokenInspector.class);

    private List<TokenInspector> tokenInspectors;
    private Container container;

    public CompositeTokenInspector() {
        tokenInspectors = new LinkedList<TokenInspector>();
    }

    /**
     * Read the token from the ActionInvocation
     * @param actionInvocation
     * @return
     */
    public String read(ActionInvocation actionInvocation) {
        String token = null;
        for (TokenInspector tokenInspector : tokenInspectors) {
            token = tokenInspector.read(actionInvocation);
            if (token != null) {
                break;
            }
        }
        return token;
    }

    /**
     * Inject the list of TokenInspector's to include in this composite
     * @param list
     */
    @Inject("securityplugin.compositeTokenInspector")
    public void setTokenInspectors(String list) {
         if (list != null) {
             String[] arr = list.split(",");
             for (String name : arr) {
                 Object obj = container.getInstance(TokenInspector.class, name.trim());
                 if (obj != null) {
                     tokenInspectors.add((TokenInspector) obj);
                 } else {
                     LOG.error("The Container could not provide a TokenInspector named "+name);
                 }
             }
         }
     }

    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }
}
