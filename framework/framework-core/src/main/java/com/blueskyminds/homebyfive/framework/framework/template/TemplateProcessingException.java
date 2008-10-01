package com.blueskyminds.homebyfive.framework.framework.template;

/**
 * Indicates that an exception occured while processing a template
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class TemplateProcessingException extends Exception {

    public TemplateProcessingException() {
    }

    public TemplateProcessingException(String message) {
        super(message);
    }

    public TemplateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateProcessingException(Throwable cause) {
        super(cause);
    }
}
