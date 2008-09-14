package com.blueskyminds.housepad.web.plugin.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.blueskyminds.housepad.core.region.indexes.CountryIndex;
import com.google.inject.Inject;

import java.util.Map;

/**
 * Date Started: 25/02/2008
 * <p/>
 * History:
 */
public class CountryAwareInterceptor implements Interceptor {

    private CountryIndex countryIndex;

    /**
     * Called to let an interceptor clean up any resources it has allocated.
     */
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called after an interceptor is created, but before any requests are processed using
     * {@link #intercept(com.opensymphony.xwork2.ActionInvocation) intercept} , giving
     * the Interceptor a chance to initialize any needed resources.
     */
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Allows the Interceptor to do some processing on the request before and/or after the rest of the processing of the
     * request by the {@link com.opensymphony.xwork2.ActionInvocation} or to short-circuit the processing and just return a String return code.
     *
     * @param invocation the action invocation
     * @return the return code, either returned from {@link com.opensymphony.xwork2.ActionInvocation#invoke()}, or from the interceptor itself.
     * @throws Exception any system-level error, as defined in {@link com.opensymphony.xwork2.Action#execute()}.
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getProxy().getAction();
        Map parameters = invocation.getInvocationContext().getParameters();
//        if (action instanceof CountryAware) {
//            CountryAware countryAware = (CountryAware) action;
//            String country = countryAware.getCountry();
//
//            countryIndex.getCountry()
//
//            ((CountryAware) action).setCountry();
//        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Inject
    public void setCountryIndex(CountryIndex countryIndex) {
        this.countryIndex = countryIndex;
    }
}
