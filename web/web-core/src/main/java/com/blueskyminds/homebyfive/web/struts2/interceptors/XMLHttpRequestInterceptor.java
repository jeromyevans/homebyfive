package com.blueskyminds.homebyfive.web.struts2.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.AnnotationUtils;
import com.blueskyminds.homebyfive.web.core.annotations.*;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Intercepts requests to see if they originate from an XMLHttpRequest.  If it does and the target
 *  action is XMLHttpRequestAware, the corresponding flag of the action is set.
 *
 * If the action is annotated with XMLHttpRequestOnly then the invocation will only proceed if the
 * request carries the appropriate header.
 *
 * If the action is annotated with DenyXMLHttpRequests then the invocation will only proceed
 *
 * Date Started: 21/04/2008
 */
public class XMLHttpRequestInterceptor implements Interceptor {

    private static final String PATTERN = "XMLHttpRequest";
    private static final String HEADER = "X-Requested-With";

    public void destroy() {
    }

    public void init() {
    }

    /**
     * Set the XMLHttpRequest property of an XMLHttpRequestAware action
     *
     * @param invocation
     * @return
     * @throws Exception
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        Class actionClass = action.getClass();
        String methodName = invocation.getProxy().getMethod();

        Boolean allow = null;
        boolean xmlHttpRequest = false;

        // check the request header
        HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
        String xRequestedWith = request.getHeader(HEADER);
        xmlHttpRequest = PATTERN.equals(xRequestedWith);

        // set the property of the action is interested
        if (action instanceof XMLHttpRequestAware) {
            ((XMLHttpRequestAware) action).setXMLHttpRequest(xmlHttpRequest);
        }

        if (xmlHttpRequest) {
            // check the method-level annotation.
            if (methodHasAnnotation(actionClass, methodName, XMLHttpRequestOnly.class)) {
                allow = true;
            }

            if (allow == null){
                if (methodHasAnnotation(actionClass, methodName, NonXMLHttpRequestOnly.class)) {
                    allow = false;
                }
            }

            if (allow == null) {
               // check the class-level annotation on the action
                if (actionClass.isAnnotationPresent(XMLHttpRequestOnly.class)) {
                    allow = true;
                }
            }

            if (allow == null) {
                // check the class-level annotation on the action
                if (actionClass.isAnnotationPresent(NonXMLHttpRequestOnly.class)) {
                    allow = false;
                }
            }

            if (allow == null) {
                // no annotations present
                allow = true;
            }
        } else {
            // check the method-level annotation.
            if (methodHasAnnotation(actionClass, methodName, NonXMLHttpRequestOnly.class)) {
                allow = true;
            }

            if (allow == null){
                if (methodHasAnnotation(actionClass, methodName, XMLHttpRequestOnly.class)) {
                    allow = false;
                }
            }

            if (allow == null) {
               // check the class-level annotation on the action
                if (actionClass.isAnnotationPresent(NonXMLHttpRequestOnly.class)) {
                    allow = true;
                }
            }

            if (allow == null) {
                // check the class-level annotation on the action
                if (actionClass.isAnnotationPresent(XMLHttpRequestOnly.class)) {
                    allow = false;
                }
            }

            if (allow == null) {
                // no annotations present
                allow = true;
            }
        }

        if (allow) {
            return invocation.invoke();
        } else {
            if (xmlHttpRequest) {
                return "xmlHttpRequestNotPermitted";
            } else {
                return "xmlHttpRequestOnly";
            }
        }
    }

    /**
     * Checks for annotations on a method, pushing through proxies
     *
     * @param clazz
     * @param methodName
     * @param annotation
     * @return
     */
    private boolean methodHasAnnotation(Class clazz, String methodName, Class<? extends Annotation> annotation) {
        Collection<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethods(clazz, annotation);
        for (Method method : annotatedMethods) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the named method annotation if present, pushing through proxies
     *
     * @param clazz
     * @param methodName
     * @param annotation
     * @return
     */
    private <T extends Annotation> T getMethodAnnotation(Class clazz, String methodName, Class<T> annotation) {
        Collection<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethods(clazz, annotation);
        for (Method method : annotatedMethods) {
            if (method.getName().equals(methodName)) {
                return method.getAnnotation(annotation);
            }
        }
        return null;
    }
}
