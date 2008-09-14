package com.blueskyminds.housepad.web.plugin;

/**
 * Identifies a Tag that supports the Struts2 theme engine
 *
 * Date Started: 11/12/2007
 * <p/>
 * History:
 */
public interface ThemedTag {

    void setTemplate(String template);

    void setTheme(String theme);

    void setTemplateDir(String templateDir);
}
