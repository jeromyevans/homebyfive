package com.blueskyminds.homebyfive.web.core.decorators;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import com.blueskyminds.homebyfive.web.core.ajax.AJAXTools;

import javax.servlet.http.HttpServletRequest;

/**
 * Permits decoration only of non-ajax requests
 *
 * Date Started: 2/03/2008
 * <p/>
 * History:
 */
public class XMLHttpRequestDecorator extends AbstractDecoratorMapper {

    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        if (AJAXTools.isAJAXRequest(request)) {
            return null;
        } else {
            return super.getDecorator(request, page);
        }
    }
}
