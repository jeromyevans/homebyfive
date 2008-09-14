package com.blueskyminds.housepad.web.plugin.views;

import org.apache.struts2.views.TagLibrary;
import com.opensymphony.xwork2.util.ValueStack;
import com.blueskyminds.housepad.web.plugin.views.freemarker.tags.HousePadTagModels;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Arrays;

/**
 * A TagLibrary bean registered with the Struts2 container is automatically available to all other tags and pages.
 *
 * Date Started: 29/04/2008
 */
public class HousePadTagLibrary implements TagLibrary {

    public Object getFreemarkerModels(ValueStack stack, HttpServletRequest req,HttpServletResponse res) {
        return new HousePadTagModels(stack, req, res);
    }

    /**
     * NOTE: there is no velocity support
     *
     * @return
     */
    public List<Class> getVelocityDirectiveClasses() {
        Class[] directives = new Class[]{
        //        AreaDirective.class,                
        };
        return Arrays.asList(directives);
    }

}
