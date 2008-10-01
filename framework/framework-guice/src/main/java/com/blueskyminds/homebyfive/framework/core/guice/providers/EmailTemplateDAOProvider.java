package com.blueskyminds.homebyfive.framework.core.guice.providers;

import com.blueskyminds.homebyfive.framework.core.email.dao.EmailTemplateDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Provides a SubstitutionDAO with created with the current EntityManager
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public class EmailTemplateDAOProvider implements Provider<EmailTemplateDAO> {

    private Provider<EntityManager> em;

    public EmailTemplateDAO get() {
        return new EmailTemplateDAO(em.get());
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }
}