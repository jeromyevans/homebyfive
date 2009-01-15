package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;
import com.blueskyminds.homebyfive.framework.core.alias.Aliased;
import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.tools.NamedTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.tag.RegionTagMap;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagTools;
import com.blueskyminds.homebyfive.business.tag.TagMap;
import com.blueskyminds.homebyfive.business.party.PartyTagMap;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.jboss.envers.Versioned;
import org.jboss.envers.VersionsJoinTable;
import org.hibernate.annotations.Cascade;

/**
 * A RegionHandle contains essential information about a region within a region graph
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("R")
@Versioned
public abstract class Region extends AbstractEntity implements Named, Aliased, Taggable, Comparable {

    protected String parentPath;
    protected String path;
    protected String key;
    protected String name;
    protected String abbr;
    protected RegionTypes type;
    private Set<RegionAlias> aliases;
    @XStreamOmitField private Set<RegionHierarchy> childRegionMaps;
    @XStreamOmitField protected RegionIndex regionIndex;
    private DomainObjectStatus status;
    private String description;

    /**
      * Tags assigned to this Region
      */
    private Set<RegionTagMap> tagMaps;
    
    protected Region(String name, RegionTypes type) {
        this.name = name;
        this.type = type;
        init();
    }

    /** Create a RegionHandle with the specified name and aliases */ 
    protected Region(String name, RegionTypes type, String...aliases) {
        this.name = name;
        this.type = type;
        init();
        for (String alias : aliases) {
            if (alias != null) {
                this.aliases.add(new RegionAlias(this, alias));
            }
        }
    }

    protected Region() {
        init();
    }

    private void init() {
        aliases = new HashSet<RegionAlias>();
        childRegionMaps = new HashSet<RegionHierarchy>();
        status = DomainObjectStatus.Valid;
        tagMaps = new HashSet<RegionTagMap>();
    }

    /** The unique path of this region's primary parent */
    @Basic
    @Column(name="ParentPath")
    public String getParentPath() {
        return parentPath;
    }

    protected void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
    
    /** The unique path of this region */
    @Basic
    @Column(name="Path")
    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    /** unique key within the parent path */
    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name="Abbr")
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    @Enumerated
    @Column(name="Type")
    public RegionTypes getType() {
        return type;
    }

    public void setType(RegionTypes type) {
        this.type = type;
    }

    /**
     * Aliases for this region.
     *
     * Note: the aliases are specified as lazy-loaded here, but most queries fetch them eagerly as an optimisation
     * Specifying eager here is not an optimisation at it would result in a second select
     *
     * @return
     */
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<RegionAlias> getRegionAliases() {
        return aliases;
    }

    protected void setRegionAliases(Set<RegionAlias> aliases) {
        this.aliases = aliases;
    }

    /**
     * Get the aliases for this region (excluding the actual name)
     *
     * @return
     */
    @Transient
    public String[] getAliases() {
        return NamedTools.toArray(aliases);
    }

    /**
     * Get all names and aliases
     */
    @Transient
    public String[] getNames() {
        return NamedTools.toArray(aliases, name);
    }

    /**
     * True if this region has the specified name or alias
     *
     * @param otherName
     * @return
     */
    public boolean hasName(String otherName) {
        if (name.equals(otherName)) {
            return true;
        }
        for (RegionAlias alias : aliases) {
            if (alias.getName().equals(otherName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add an alias for this region if it doesn't already have it
     *
     * @param name
     */
    public void addAlias(String name) {
        if (!hasName(name)) {
            aliases.add(new RegionAlias(this, name));
        }
    }

    /**
     * Get the children regions.
     * A Region is a child of this region if a Map exists where this is the Parent
     * Child mappings cascade all operations to to the mapping class, and cascade persist to the child.
     * @return
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    protected Set<RegionHierarchy> getChildRegionMaps() {
        return childRegionMaps;
    }

    protected void setChildRegionMaps(Set<RegionHierarchy> childRegions) {
        this.childRegionMaps = childRegions;
    }

    @OneToOne(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public RegionIndex getRegionIndex() {
        return regionIndex;
    }

    public void setRegionIndex(RegionIndex regionIndex) {
        this.regionIndex = regionIndex;
    }    

    @Enumerated
    @Column(name="Status")
    public DomainObjectStatus getStatus() {
        return status;
    }

    public void setStatus(DomainObjectStatus status) {
        this.status = status;
    }

    @Transient
    public abstract Region getParent();

    public abstract void setParent(Region region);

    /**
     * Add a subregion to this region
     *
     * A RegionHierarchy is created with THIS as the PARENT and the other as the CHILD
     * The same map instance is added to this list of children and the other list of parents.
     *
     * After an update like this both the parent and child need to be persisted
     *
     * @param childHandle
     * @return true if added ok
     */
    public boolean addChildRegion(Region childHandle) {
        boolean added = false;

        if (childHandle != null) {
            RegionHierarchy map = new RegionHierarchy(this, childHandle);

            added = doAddChildMap(map);
        }
        return added;
    }

    /**
     * Remove a subregion from this region
     *
     * The RegionHierarchy entry to this child is removed from this and the parent reference is removed from the child
     *
     * After an update like this both the parent and child need to be persisted
     *
     * @param childHandle
     * @return true if found and removed ok
     */
    public boolean removeChildRegion(Region childHandle) {

        boolean removed = false;
        RegionHierarchy childEntryToRemove = null;
        RegionHierarchy parentEntryToRemove = null;

        for (RegionHierarchy map : childRegionMaps) {
            if (map.getChild().equals(childHandle)) {
                childEntryToRemove = map;
                break;
            }
        }

        if (childEntryToRemove != null) {
            removed = childRegionMaps.remove(childEntryToRemove);
        }

        return removed;
    }

    private boolean doAddChildMap(RegionHierarchy map) {
        return childRegionMaps.add(map);
    }

    /**
     * The toString method has been overridden to show the short className, id and hashcode
     *
     * @return string representation of this DomainObject
     */
    public String toString() {
        return getClass().getSimpleName()+"["+getId()+"]"+getName();
    }

    /**
     * Determine if this region has the specified child.
     *
     * @param regionHandle
     * @return true if this does
     */
    public boolean hasChild(Region regionHandle) {
        for (RegionHierarchy map : childRegionMaps) {
            if (map.getChild().equals(regionHandle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the children of the specified type
     *
     * @param type
     * @return
     */
     public Set<Region> getChildren(RegionTypes type) {
        Set<Region> matchedChildren = new HashSet<Region>();
        for (RegionHierarchy map : childRegionMaps) {
            if (type.equals(map.getChild().getType())) {
                matchedChildren.add(map.getChild());
            }
        }
        return matchedChildren;
    }

     /**
     * Permanently merge this region with another one
     *
     * This region inherits the parent of the source
     * This region inherits the children of the source
     * This region inherits the aliases of the source
     *
     * The RegionImplementation is requested to merge 
     * New RegionHierarchy and alias entries are created.
     *
     * The other region is removed from its parent and has its children removed.
     *
      This merge operation cannot be undone.
     *
     * @param anotherRegion
     * @return
     */
    public void mergeWith(Region anotherRegion) {
         Region otherParent = anotherRegion.getParent();
         Set<Region> parentsToAdd = new HashSet<Region>();
         Set<Region> childrenToAdd = new HashSet<Region>();
         Set<String> aliasesToAdd = new HashSet<String>();
         Set<Region> parentsToUpdate = new HashSet<Region>();
         Set<Region> childrenToUpdate = new HashSet<Region>();

         this.name = anotherRegion.getName();
         this.abbr = anotherRegion.getAbbr();
         this.key = anotherRegion.getKey();

         // determine which parents need to be added
         if (!otherParent.hasChild(this)) {
             otherParent.addChildRegion(this);
             setParent(otherParent);
         }

         // determine which children need to be added
         Set<RegionHierarchy> otherChildren = anotherRegion.getChildRegionMaps();
         for (RegionHierarchy regionHierarchy : otherChildren) {
             if (!hasChild(regionHierarchy.getChild())) {
                 childrenToAdd.add(regionHierarchy.getChild());
             }
             childrenToUpdate.add(regionHierarchy.getChild());
         }

         // determine which aliases need to be added
         for (String alias : anotherRegion.getNames()) {
             if (!hasName(alias)) {
                 aliasesToAdd.add(alias);
             }
         }

         for (Region childRegion : childrenToAdd) {
             addChildRegion(childRegion);
         }

         for (String alias : aliasesToAdd) {
             addAlias(alias);
         }

         // remove the other region from its parents
         for (Region parent: parentsToUpdate) {
             parent.removeChildRegion(anotherRegion);
         }

         mergeTags(((Region) anotherRegion).tagMaps);
    }

    public abstract void populateAttributes();    

    /**
     * The tags that have been assigned to this Region
     **/
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<RegionTagMap> getTagMaps() {
        return tagMaps;
    }

    public void setTagMaps(Set<RegionTagMap> tagMaps) {
        this.tagMaps = tagMaps;
    }

    @Transient
    public Set<Tag> getTags() {
        return TagTools.extractTags(tagMaps);
    }

    public void addTag(Tag tag) {
        if (!TagTools.contains(tagMaps, tag)) {
            tagMaps.add(new RegionTagMap(this, tag));
        }
    }

    public void removeTag(String tagName) {
        RegionTagMap toRemove = null;
        for (RegionTagMap map : tagMaps) {
            if (map.getTag().getName().equals(tagName)) {
                toRemove = map;
                break;
            }
        }
        if (toRemove != null) {
            tagMaps.remove(toRemove);
        }
    }

    public boolean hasTag(String tagName) {
        for (RegionTagMap tagMap : tagMaps) {
            if (tagMap.getTag().getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTag(Tag tag) {
        for (RegionTagMap tagMap : tagMaps) {
            if (tagMap.getTag().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merge the tags from the other region into this region
     *
     * @param otherTags
     */
    private void mergeTags(Set<RegionTagMap> otherTags) {
        if (otherTags != null) {
            for (RegionTagMap RegionTagMap : otherTags) {
                if (!hasTag(RegionTagMap.getTag())) {
                    // merge
                    addTag(RegionTagMap.getTag());
                }
            }
        }
    }

    @Column(name="Description", length = 20000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PrePersist
    protected void prePersist() {
        populateAttributes();
    }

     /**
     * Gets the set of regions that are ancestors for the current region
     *
     * This method walks the graph so it can be quite slow as ancestors are lazily loaded.
      *
     * Note there is no getDescendants method because that will certainly be too slow.  See the optimized queries
     * in the service interface
     *
     * @return
     */
    @Transient
    public Set<Region> getAncestors() {
        Set<Region> ancestors = new HashSet<Region>();
        visitAncestors(ancestors);
        return ancestors;
    }

    /** Recursive method to visit all the ancestors of a region once */
    private void visitAncestors(Set<Region> ancestors) {
        Region parent = getParent();
        if (parent != null) {
            if (!ancestors.contains(parent)) {
                ancestors.add(parent);
                parent.visitAncestors(ancestors);
            }
        }
    }

    public int compareTo(Object o) {
        if (o != null) {
            if (o instanceof Region) {
                if (name != null) {
                    return name.compareTo(((Region) o).name);
                }
            }
        }
        return 0;
    }
 
}
