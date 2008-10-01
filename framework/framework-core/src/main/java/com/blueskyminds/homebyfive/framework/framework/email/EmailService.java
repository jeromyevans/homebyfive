package com.blueskyminds.homebyfive.framework.framework.email;

import java.util.List;
import java.util.Map;

/**
 * Provides a simple mechanism to send emails
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public interface EmailService {

    /**
     * Send a message
     * (no attachments)
     */
    void send(String from, String to, String subject, String content, String mimeType) throws EmailerException;

    /**
     * Send a message
     * (no attachments)
     */
    void send(String from, String to[], String subject, String content, String mimeType) throws EmailerException;

    /**
     * Send a message
     * (no attachments)
     */
    void send(String from, String to, String[] cc, String subject, String content, String mimeType) throws EmailerException;

    /**
     * Send a message
     * (no attachments)
     */
    void send(String from, String[] to, String[] cc, String subject, String content, String mimeType) throws EmailerException;


    /**
     * Lookup a template by its key
     *
     * @param key
     * @return
     */
    EmailTemplate lookupTemplate(String key);

    /**
     * List email templates in the specified category
     *
     * @param category
     * @return
     */
    List<EmailTemplate> listTemplates(String category);

    void send(String from, String to, EmailTemplate template, Map<String, Object> context) throws EmailerException;
}
