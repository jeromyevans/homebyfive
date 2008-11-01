package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.framework.core.alias.Aliased;
import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.tools.NamedTools;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.tag.RegionTagMap;
import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagTools;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

import org.jboss.envers.Versioned;

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
public abstract class Region extends AbstractEntity implements Named, Aliased, Taggable {

    protected String parentPath;
    protected String path;
    protected String key;
    protected String name;
    protected String abbr;
    protected RegionTypes type;
    private Set<RegionAlias> aliases;
    @XStreamOmitField private Set<RegionHierarchy> parentRegionMaps;
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
        parentRegionMaps = new HashSet<RegionHierarchy>();
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
     * Specifing eager here is not an optimisation at it would result in a second select
     *
     * @return
     */
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected Set<RegionAlias> getRegionAliases() {
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

    /** Get the region referenced by this handle */
//    @Transient
//    public final Region getRegion() {
//        Region region = getRegionTarget();
//        if (region != null) {
//            injectRegionHandle(region);
//        }
//        return region;
//    }
//    protected abstract Region getRegionTarget();
    
    /**
     * Get the parent regions.
     * A Region is a parent of this region if a Map exists where this is the Child
     * @return
     */
    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
    public Set<RegionHierarchy> getParentRegionMaps() {
        return parentRegionMaps;
    }

    public void setParentRegionMaps(Set<RegionHierarchy> parentRegions) {
        this.parentRegionMaps = parentRegions;
    }

    /**
     * Get the children regions.
     * A Region is a child of this region if a Map exists where this is the Parent
     * @return
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected Set<RegionHierarchy> getChildRegionMaps() {
        return childRegionMaps;
    }

    protected void setChildRegionMaps(Set<RegionHierarchy> childRegions) {
        this.childRegionMaps = childRegions;
    }

    @OneToOne(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
            if (added) {
                childHandle.doAddParentMap(map);
            }
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

        for (RegionHierarchy map : childHandle.getParentRegionMaps()) {
            if (map.getParent().equals(this)) {
                parentEntryToRemove = map;
                break;
            }
        }

        if (parentEntryToRemove != null) {
            childHandle.getParentRegionMaps().remove(parentEntryToRemove);
        }

        return removed;
    }

    /**
     * Remove a parent from this region
     *
     * The parent's RegionHierarchy entry to this child is removed from this and the parent reference is removed from is
     *
     * After an update like this both the parent and child need to be persisted
     *
     * @param parentHandle
     * @return true if found and removed ok
     */
    public boolean removeParentRegion(Region parentHandle) {

        boolean removed = false;
        RegionHierarchy childEntryToRemove = null;
        RegionHierarchy parentEntryToRemove = null;

        for (RegionHierarchy map : parentRegionMaps) {
            if (map.getParent().equals(parentHandle)) {
                parentEntryToRemove = map;
                break;
            }
        }

        if (parentEntryToRemove != null) {
            removed = parentRegionMaps.remove(parentEntryToRemove);
        }

        for (RegionHierarchy map : parentHandle.getChildRegionMaps()) {
            if (map.getChild().equals(this)) {
                childEntryToRemove = map;
                break;
            }
        }

        if (childEntryToRemove!= null) {
            parentHandle.getChildRegionMaps().remove(childEntryToRemove);
        }

        return removed;
    }

    private boolean doAddParentMap(RegionHierarchy map) {
        return parentRegionMaps.add(map);
    }

    private boolean doAddChildMap(RegionHierarchy map) {
        return childRegionMaps.add(map);
    }

    /**
     * Add the other region as a parent of this region.
     *
     * A RegionHierarchy is created with THIS as the CHILD and the other as the PARENT
     * The same map instance is added to this list of parents and the other list of children.
     *
     * After an update like this both the parent and child need to be persisted
     *
     * @param parentHandle
     * @return true if added ok
     */
    public boolean addParentRegion(Region parentHandle) {

        boolean added = false;

        if (parentHandle != null) {
            RegionHierarchy map = new RegionHierarchy(parentHandle, this);

            added = doAddParentMap(map);
            if (added) {
                parentHandle.doAddChildMap(map);
            }
        }
        return added;                
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
     * Determine if this region has the specified parent.
     *
     * @param regionHandle
     * @return true if this does
     */
    public boolean hasParent(Region regionHandle) {
        for (RegionHierarchy map : parentRegionMaps) {
            if (map.getParent().equals(regionHandle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the parent instance of the specified type
     *
     * @param type
     * @return
     */
     public Region getParent(RegionTypes type) {
        for (RegionHierarchy map : parentRegionMaps) {
            if (type.equals(map.getParent().getType())) {
                return map.getParent();
            }
        }
        return null;
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
     * This region inherits the parents of the source
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
         Set<RegionHierarchy> otherParents = anotherRegion.getParentRegionMaps();
         Set<Region> parentsToAdd = new HashSet<Region>();
         Set<Region> childrenToAdd = new HashSet<Region>();
         Set<String> aliasesToAdd = new HashSet<String>();
         Set<Region> parentsToUpdate = new HashSet<Region>();
         Set<Region> childrenToUpdate = new HashSet<Region>();

         this.name = anotherRegion.getName();
         this.abbr = anotherRegion.getAbbr();
         this.key = anotherRegion.getKey();

         // determine which parents need to be added
         for (RegionHierarchy regionHierarchy : otherParents) {
             if (!hasParent(regionHierarchy.getParent())) {
                 parentsToAdd.add(regionHierarchy.getParent());
             }
             parentsToUpdate.add(regionHierarchy.getParent());
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

         // apply the changes to this regionHandle
         for (Region parentRegion : parentsToAdd) {
             addParentRegion(parentRegion);
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

         // remove the other region's children
         for (Region child : childrenToUpdate) {
             child.removeParentRegion(anotherRegion);
         }

         mergeTags(((Region) anotherRegion).tagMaps);
    }

    public abstract void populateAttributes();    

    /**
     * The tags that have been assigned to this Region
     **/
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
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

    @Column(name="Description", length = 32768)
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
}
