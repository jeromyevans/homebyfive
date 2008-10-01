package com.blueskyminds.enterprise.tag;

import com.blueskyminds.homebyfive.framework.framework.AbstractEntity;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.Column;
import javax.persistence.Basic;
import javax.persistence.Table;
import javax.persistence.Entity;

/**
 * A tag is used for classification and grouping
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="Tag")
public class Tag extends AbstractEntity implements Comparable<Tag> {

    private String name;
    private String key;

    public Tag(String name) {
        this.name = name;
        this.key = KeyGenerator.generateId(name);
    }

    /** Default constructor for ORM */
    public Tag() {
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Unique Key that can be used in URIs */
    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int compareTo(Tag o) {
        return this.getName().compareToIgnoreCase(((Tag) o).getName());
    }

}
