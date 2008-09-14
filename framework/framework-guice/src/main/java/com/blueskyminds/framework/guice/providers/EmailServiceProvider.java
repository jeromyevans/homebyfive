package com.blueskyminds.framework.guice.providers;

import com.google.inject.Provider;
import com.google.inject.Inject;
import com.blueskyminds.framework.email.EmailService;
import com.blueskyminds.framework.email.BlockingEmailer;
import com.blueskyminds.framework.email.MockEmailer;
import com.blueskyminds.framework.email.dao.EmailTemplateDAO;
import com.blueskyminds.framework.template.TemplateManager;

import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides a simple blocking email service using the injected MailSession
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class EmailServiceProvider implements Provider<EmailService> {

    private static final Log LOG = LogFactory.getLog(EmailServiceProvider.class);

    private Session mailSession;
    private TemplateManager templateManager;
    private EmailTemplateDAO emailTemplateDAO;

    public EmailServiceProvider() {
    }

    public EmailService get() {
        if (mailSession != null) {
            BlockingEmailer emailService =  new BlockingEmailer(mailSession);
            emailService.setEmailTemplateDAO(emailTemplateDAO);
            emailService.setTemplateManager(templateManager);
            return emailService;
        } else {
            LOG.info("MailSession is not available.  EmailService will be mocked.");            
            return new MockEmailer();
        }
    }

    @Inject(optional = true)
    public void setMailSession(Session mailSession) {
        this.mailSession = mailSession;
    }

    @Inject(optional = true)
    public void setTemplateManager(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    @Inject(optional = true)
    public void setEmailTemplateDAO(EmailTemplateDAO emailTemplateDAO) {
        this.emailTemplateDAO = emailTemplateDAO;
    }
}
