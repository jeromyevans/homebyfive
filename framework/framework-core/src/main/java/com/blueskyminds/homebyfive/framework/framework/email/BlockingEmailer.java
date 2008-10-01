package com.blueskyminds.homebyfive.framework.framework.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.*;
import java.util.*;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.blueskyminds.homebyfive.framework.framework.email.dao.EmailTemplateDAO;
import com.blueskyminds.homebyfive.framework.framework.template.TemplateManager;
import com.blueskyminds.homebyfive.framework.framework.template.TemplateProcessingException;

/**
 * Simple blocking mail sender implementation using SMTP
 * <p/>
 * Date Started: 3/08/2007
 * <p/>
 * History:
 */
public class BlockingEmailer implements EmailService {

    private static final Log LOG = LogFactory.getLog(BlockingEmailer.class);

    private Session mailSession;
    private EmailTemplateDAO emailTemplateDAO;
    private TemplateManager templateManager;


    /**
     * Initialise the emailer using the default mail session.
     * <p/>
     * Note that the default session is not available in J2EE containers
     *
     * @param smtpServer
     * @param mailUser
     * @param mailPassword
     */
    public BlockingEmailer(String smtpServer, String mailUser, String mailPassword) {
        // Get the default Session using Properties Object
        Properties props = new Properties();

        // Puts the SMTP server name to properties object
        //properites.put("mail.smtp.host", smtpServer);
        props.setProperty("mail.transport.protocol", "smtp");

        if (StringUtils.isNotBlank(smtpServer)) {
            props.setProperty("mail.host", smtpServer);
        }
        if (StringUtils.isNotBlank(mailUser)) {
            props.setProperty("mail.user", mailUser);
        }
        if (StringUtils.isNotBlank(mailPassword)) {
            props.setProperty("mail.password", mailPassword);
        }

        this.mailSession = Session.getDefaultInstance(props, new SMTPAuthenticator(mailUser, mailPassword));
    }

    /**
     * Initialise the emailer using the specified mail session.
     *
     * @param mailSession session to use
     */
    public BlockingEmailer(Session mailSession) {
        this.mailSession = mailSession;
    }
    
    /**
     * Send a message
     * (no attachments)
     */
    public void send(String from, String to, String subject, String content, String mimeType) throws EmailerException {
        send(from, to, null, subject, content, mimeType);
    }

    /**
     * Send a message
     * (no attachments)
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
        if (mailSession != null) {
            try {
                Transport transport = mailSession.getTransport();

                //mailSession.setDebug(true); // Enable the debug mode

                MimeMessage message = new MimeMessage(mailSession);
                message.setSubject(subject);
                if (from != null) {
                    message.setFrom(new InternetAddress(from));
                    if (to == null) {
                        if ((cc != null) && (cc.length > 0)) {
                            to = new String[]{cc[0]};  // use the first CC...suppose this is okay...
                        }
                    }
                    if ((to != null) && (to.length > 0)) {
                        for (String toAddress : to) {
                            if (StringUtils.isNotBlank(toAddress)) {
                                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
                            }
                        }

                        if (cc != null) {
                            for (String ccAddress : cc) {
                                if (StringUtils.isNotBlank(ccAddress)) {
                                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
                                }
                            }
                        }

                        // Create the Multipart and its parts to it
                        Multipart multipart = new MimeMultipart();

                        // Create and fill the first text/html part
                        MimeBodyPart textPart = new MimeBodyPart();
                        textPart.setContent(content, mimeType);

                        multipart.addBodyPart(textPart);

                        // Add the Multipart to the message
                        message.setContent(multipart);

                        // Set the Date: header
                        message.setSentDate(new Date());

                        // Send the message
                        transport.connect();
                        Transport.send(message);
                        transport.close();
                    } else {
                        throw new EmailerException("Could not send email because the TO address is not defined and there are not CC addresses");
                    }
                } else {
                    throw new EmailerException("Could not send email because the FROM address is not defined.");
                }
            } catch (MessagingException e) {
                throw new EmailerException("Error when trying to send an email", e);
            }
        } else {
            throw new EmailerException("MailSession is null.  It needs to be provided");
        }
    }

    public EmailTemplate lookupTemplate(String key) {
        if (emailTemplateDAO != null) {
            return emailTemplateDAO.lookupTemplate(key);
        }
        return null;
    }

    public List<EmailTemplate> listTemplates(String category) {
        if (emailTemplateDAO != null) {
            return emailTemplateDAO.listTemplates(category);
        } else {
            return new LinkedList<EmailTemplate>();
        }
    }

    public void send(String from, String to, EmailTemplate template, Map<String, Object> context) throws EmailerException {
        if ((template != null) && (templateManager != null)) {
            String subject = null;
            try {
                subject = templateManager.merge(template.getSubject(), context);
                String content = templateManager.merge(template.getContent(), context);
                send(from, to, subject, content, template.getMimeType());
            } catch (TemplateProcessingException e) {
                throw new EmailerException("Unable to send email due to template processing error", e);
            }
        } else {
            if (templateManager != null) {
                throw new EmailerException("EmailTemplate is undefined");
            } else {
                throw new EmailerException("EmailTemplate processing is not available");
            }
        }
    }

    private class SMTPAuthenticator extends Authenticator {
        private String username;
        private String password;

        public SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            Base64Encoder enc = new Base64Encoder();
            return new PasswordAuthentication(enc.encode(username.getBytes()),
                    enc.encode(password.getBytes()));
        }
    }

    public void setEmailTemplateDAO(EmailTemplateDAO emailTemplateDAO) {
        this.emailTemplateDAO = emailTemplateDAO;
    }

    public void setTemplateManager(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }
}
