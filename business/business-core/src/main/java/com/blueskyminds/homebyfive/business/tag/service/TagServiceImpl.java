package com.blueskyminds.homebyfive.business.tag.service;

import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * Date Started: 6/08/2007
 * <p/>
 * History:
 */
public class TagServiceImpl implements TagService {

    private EntityManager em;
    private TagDAO tagDAO;

    @Inject
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public TagServiceImpl() {
    }

    /**
     * Lookup the tag with the specified exact name, or create it if it doesn't exist
     *
     * @return
     */
    public Tag lookupOrCreateTag(String exactName) {
        Tag tag = tagDAO.lookupTagByName(exactName);
        if (tag == null) {
            // create it
            tag = new Tag(exactName);
            tagDAO.persist(tag);
        }
        return tag;
    }

    /** Create a reference to the tag with the specified name.  It will be created if it doesn't exist */
    public Tag create(String name) {
        return lookupOrCreateTag(name);
    }

    public Tag lookupTag(String key) {
        return tagDAO.lookupTag(key);
    }

    public List<Tag> autocomplete(String key) {
        return tagDAO.autocomplete(key);
    }

    public Set<Tag> listTags() {
        return new HashSet<Tag>(tagDAO.findAll());
    }    

}
