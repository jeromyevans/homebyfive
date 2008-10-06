package com.blueskyminds.homebyfive.web.core.decorators;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Date Started: 2/03/2008
 * <p/>
 * History:
 */
public class XMLHttpRequestDecorator extends AbstractDecoratorMapper {

    private static final String PATTERN = "XMLHttpRequest";

    /**
     * Delegate to parent.
     */
    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (PATTERN.equals(xRequestedWith)) {
            return null;
        } else {
            return super.getDecorator(request, page);
        }
    }
}
