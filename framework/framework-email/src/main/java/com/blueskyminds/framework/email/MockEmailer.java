package com.blueskyminds.framework.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.framework.tools.DebugTools;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * Emailer for unit testing
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class MockEmailer implements EmailService {

    private static final Log LOG = LogFactory.getLog(MockEmailer.class);

    /**
     * Send a message
     * (no attachments)
     */
    public void send(String from, String to, String subject, String content, String mimeType) throws EmailerException {
        send(from, to, null, subject, content, mimeType);
    }

    /**
     * Send a message
     * (no attachmentts)
     */
    public void send(String from, String to[], String subject, String content, String mimeType) throws EmailerException {
        send(from, to, null, subject, content, mimeType);
    }

    /**
     * Send a message
     * (no attachments)
     */
    public void send(String from, String to, String[] cc, String subject, String content, String mimeType) throws EmailerException {
        send(from, new String[]{to}, cc, subject, content, mimeType);
    }

    /**
     * Send a message
     * (no attachments)
     */
    public void send(String from, String[] to, String[] cc, String subject, String content, String mimeType) throws EmailerException {
        LOG.info("Mock sending email message: ");
        LOG.info("From: "+from);
        LOG.info("To: "+ DebugTools.toString(to, true));
        LOG.info("Cc: "+ DebugTools.toString(cc, true));
        LOG.info("Subject: "+ subject);
        LOG.info("Mime type: "+ mimeType);
        LOG.info("Content:");
        LOG.info(content);
    }

    public void send(String from, String to, EmailTemplate template, Map<String, Object> context) throws EmailerException {
        send(from, to, null, template.getSubject(), template.getContent(), template.getMimeType());
    }

    public EmailTemplate lookupTemplate(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EmailTemplate> listTemplates(String category) {
        return new LinkedList<EmailTemplate>();
    }
}
