package com.blueskyminds.landmine.core.property.assets;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.tools.Named;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * An asset can be associated with a property.
 *
 * Assets include those items that form part of the property but are not part of the structure.
 * eg.
 *  dishwasher
 *  shed
 *  air conditioner
 *
 * Date Started: 2/05/2008
 */
@Entity
@Table(name="PremiseAsset")
public class PremiseAsset extends AbstractDomainObject implements Named, Comparable {

    private String name;
    private String description;
    private String key;

    public PremiseAsset(String name, String description) {
        this.key = KeyGenerator.generateId(name);
        this.name = name;
        this.description = description;
    }

    public PremiseAsset() {
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    public int compareTo(Object o) {
        if (o instanceof PremiseAsset) {
            PremiseAsset other = (PremiseAsset) o;
            return this.name.compareToIgnoreCase(other.getName());
        }
        return 0;  
    }

}
