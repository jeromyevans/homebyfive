package com.blueskyminds.homebyfive.business.user.services;

import com.blueskyminds.homebyfive.framework.core.events.EventRegistry;
import com.blueskyminds.homebyfive.framework.core.events.EventHandler;
import com.blueskyminds.homebyfive.business.user.services.UserEvents;
import com.blueskyminds.homebyfive.business.user.services.UserService;
import com.blueskyminds.homebyfive.business.user.model.users.UserProfile;
import com.blueskyminds.homebyfive.framework.core.email.EmailService;
import com.blueskyminds.homebyfive.framework.core.email.EmailTemplate;
import com.blueskyminds.homebyfive.framework.core.email.EmailerException;
import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Listens for when a new account is registered and sends them a verification email
 *
 * Date Started: 14/05/2008
 */
public class VerificationEmailer implements EventHandler {

    private static final String VERIFICATION_EMAIL_TEMPLATE = "VerificationEmailTemplate";

    private static final Log LOG = LogFactory.getLog(VerificationEmailer.class);

    private EventRegistry eventRegistry;
    private EmailService emailService;
    private UserService userService;

    /**
     * Fired when a new account is registered
     *
     * @param s
     * @param userId
     */
    @Transactional
    public void fire(String s, Object userId) {
        String systemAddress = "accounts@housepad.com.au";  // todo

        if (emailService != null) {
            UserProfile userProfile = userService.lookupUserProfile((Long) userId);
            EmailTemplate template = emailService.lookupTemplate(VERIFICATION_EMAIL_TEMPLATE);
            if (template != null && userProfile != null) {
                try {
                    emailService.send(systemAddress, userProfile.getPrimaryPersonalEmail(), template, new HashMap<String, Object>());
                } catch (EmailerException e) {
                    LOG.error(e);
                    eventRegistry.fire(UserEvents.VERIFICATION_EMAIL_FAILED, userId);
                }
            } else {
                LOG.error("Verification EmailTemplate is not defined");
                eventRegistry.fire(UserEvents.VERIFICATION_EMAIL_FAILED, userId);
            }
        } else {
            LOG.error("Verification EmailService is not available");
            eventRegistry.fire(UserEvents.VERIFICATION_EMAIL_FAILED, userId);
        }
    }

    @Inject
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEventRegistry(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }
}
