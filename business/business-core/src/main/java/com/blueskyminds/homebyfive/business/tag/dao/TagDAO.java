package com.blueskyminds.homebyfive.business.tag.dao;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 6/08/2007
 * <p/>
 * History:
 */
public class TagDAO extends AbstractDAO<Tag> {

    private static final String QUERY_TAG_BY_NAME = "tag.byName";
    private static final String QUERY_TAG_BY_KEY = "tag.byKey";
    private static final String QUERY_TAG_BY_KEY_AUTOCOMPLETE = "tag.autocomplete";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_KEY = "keyValue";

    @Inject
    public TagDAO(EntityManager em) {
        super(em, Tag.class);
    }

    /**
     * Lookup the tag with the specified name 
     * Tag names are unique
     **/
    public Tag lookupTagByName(String exactName) {
        List<Tag> results;
        Query query = em.createNamedQuery(QUERY_TAG_BY_NAME);
        query.setParameter(PARAM_NAME, exactName);

        results = (List<Tag>) query.getResultList();
        if (results.size() > 0) {
            // return first result
            return results.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Lookup the tag with the specified key
     **/
    public Tag lookupTag(String key) {
        List<Tag> results;
        Query query = em.createNamedQuery(QUERY_TAG_BY_KEY);
        query.setParameter(PARAM_KEY, key);

        return firstIn(query.getResultList());
    }

    public Tag persist(Tag tag) {
        em.persist(tag);
        return tag;
    }

    public List<Tag> autocomplete(String key) {
        Query query = em.createNamedQuery(QUERY_TAG_BY_KEY_AUTOCOMPLETE);
        query.setParameter(PARAM_KEY, StringUtils.lowerCase(key)+"%");
        return query.getResultList();
    }
}
