package com.blueskyminds.struts2.securityplugin.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.blueskyminds.struts2.securityplugin.signature.SignatureHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Enforces that the request carries a current valid signature
 *
 * Date Started: 9/05/2008
 */
public class CheckSignatureInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(CheckSignatureInterceptor.class);

    private SignatureHandler signatureHandler;

    public String intercept(ActionInvocation invocation) throws Exception {
        String signature = signatureHandler.read(invocation);
        if (signature != null) {
            if (signatureHandler.accept(signature, invocation)) {
                return invocation.invoke();
            } else {
                LOG.debug("Invalid signature ignored");
            }
        } else {
            LOG.debug("Unsigned request ignored");
        }
        return "rejectSignature";
    }

    public void destroy() {
    }

    public void init() {
    }

    @Inject
    public void setSignatureHandler(SignatureHandler signatureHandler) {
        this.signatureHandler = signatureHandler;
    }
}
