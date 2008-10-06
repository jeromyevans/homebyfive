package com.blueskyminds.homebyfive.web.struts2.securityplugin.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.AnnotationUtils;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.struts2.StrutsStatics;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Checks the RolesAllowed annotation of Actions
 *
 * Date Started: 9/05/2008
 */
public class RolesAllowedInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(RolesAllowedInterceptor.class);

    private static final String UNAUTHORIZED_RESULT = "unauthorized";

    /**
     * Checks the target action for the RolesAllowed annotation. If present on the method or class, the
     * invocation proceeds only if the HttpServletRequest indicates that the principal has that role.
     *
     * @param invocation
     * @return
     * @throws Exception
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        boolean proceed = false;
        Object action = invocation.getAction();
        if (action != null) {
            boolean permitAll = false;
            String[] rolesAllowed = null;
            Class actionClass = action.getClass();
            String methodName = invocation.getProxy().getMethod();

            // check the method-level annotation.  It takes precedence over the class-level annotation
            RolesAllowed annotation = getMethodAnnotation(actionClass, methodName, RolesAllowed.class);
            if (annotation != null) {
                rolesAllowed = annotation.value();
            }

            if (rolesAllowed == null) {
                // check if the DenyAll annotation is present
                if (methodHasAnnotation(actionClass, methodName, DenyAll.class)) {
                    rolesAllowed = new String[0];   // non-null empty array === permit none
                }
            }

            if (rolesAllowed == null) {
                // check if the PermitAll annotation is present
                if (methodHasAnnotation(actionClass, methodName, PermitAll.class)) {
                    rolesAllowed = new String[0];
                    permitAll = true;
                }
            }

            if (rolesAllowed == null && !permitAll) {
                // check the class-level annotation on the action
                if (actionClass.isAnnotationPresent(RolesAllowed.class)) {
                    rolesAllowed = ((RolesAllowed) actionClass.getAnnotation(RolesAllowed.class)).value();
                }

                if (rolesAllowed == null) {
                    if (actionClass.isAnnotationPresent(DenyAll.class)) {
                        rolesAllowed = new String[0];    // non-null empty array === permit none
                    }
                }

                if (rolesAllowed == null) {
                    if (actionClass.isAnnotationPresent(PermitAll.class)) {
                        permitAll = true;
                    }
                }
            }

            if (rolesAllowed != null && !permitAll) {
                // controlled access with zero or more roles in effect
                HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);

                // check that the Principal has the required role
                for (String role : rolesAllowed) {
                    if (request.isUserInRole(role)) {
                        proceed = true;
                        break;
                    }
                }
            } else {
                // rolesAllowed is null (uncontrolled) or permitAll is true
                proceed = true;
            }

            if (proceed) {
                LOG.info("Action invocation granted ("+action.getClass().getSimpleName()+"."+methodName+")");
                return invocation.invoke();
            } else {
                LOG.info("Action invocation denied ("+action.getClass().getSimpleName()+"."+methodName+")");
            }
        }

        return UNAUTHORIZED_RESULT;
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

    public void destroy() {
    }

    public void init() {
    }

}
