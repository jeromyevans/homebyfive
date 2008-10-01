package com.blueskyminds.homebyfive.framework.core.email.dao;

import com.blueskyminds.homebyfive.framework.core.email.EmailTemplate;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Implements access to EmailTemplates
 *
 * Date Started: 14/05/2008
 */
public class EmailTemplateDAO extends AbstractDAO<EmailTemplate> {

    private static final String QUERY_EMAIL_TEMPLATES_BY_CATEGORY = "framework.emailTemplates.byCategory";
    private static final String QUERY_EMAIL_TEMPLATE_BY_KEY = "framework.emailTemplate.byKey";

    private static final String PARAM_CATEGORY = "category";
    private static final String PARAM_KEY = "keyValue";

    public EmailTemplateDAO(EntityManager em) {
        super(em, EmailTemplate.class);
    }

    public List<EmailTemplate> listTemplates(String category) {
        Query query = em.createNamedQuery(QUERY_EMAIL_TEMPLATES_BY_CATEGORY);
        query.setParameter(PARAM_CATEGORY, category);
        return query.getResultList();
    }

     /**
     * Lookup an EmailTemplate by its key
     * @param key
     * @return
     */
    public EmailTemplate lookupTemplate(String key) {
        Query query = em.createNamedQuery(QUERY_EMAIL_TEMPLATE_BY_KEY);
        query.setParameter(PARAM_KEY, key);
        return firstIn(query.getResultList());
    }

}
