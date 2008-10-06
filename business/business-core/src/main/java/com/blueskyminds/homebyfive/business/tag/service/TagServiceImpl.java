package com.blueskyminds.homebyfive.business.tag.service;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;

/**
 * Date Started: 6/08/2007
 * <p/>
 * History:
 */
public class TagServiceImpl implements TagService {

    private EntityManager em;


    public TagServiceImpl(EntityManager em) {
        this.em = em;
    }

    public TagServiceImpl() {
    }

    /**
     * Lookup the tag with the specified exact name, or create it if it doesn't exist
     *
     * @return
     */
    public Tag lookupOrCreateTag(String exactName) {
        Tag tag = new TagDAO(em).lookupTagByName(exactName);
        if (tag == null) {
            // create it
            tag = new Tag(exactName);
            em.persist(tag);
        }
        return tag;
    }

    public Tag lookupTag(String key) {
        return new TagDAO(em).lookupTag(key);
    }

    public Set<Tag> listTags() {
        return new HashSet<Tag>(new TagDAO(em).findAll());
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
