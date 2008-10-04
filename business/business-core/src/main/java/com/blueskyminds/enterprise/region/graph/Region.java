package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.framework.core.alias.Aliased;
import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.tools.NamedTools;
import com.blueskyminds.enterprise.region.RegionTypes;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A RegionHandle contains essential information about a region within a region graph and is a proxy (loosely speaking)
 * to the actual Region entity
 *
 * There's a different RegionHandle for each type of Region implementation.
 *
 * The primary goal of this design is FAST lookup of regions irrespective of type.
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
public abstract class Region extends AbstractEntity implements Named, Aliased {

    private String name;
    private String abbreviation;    
    private RegionTypes type;
    private Set<RegionAlias> aliases;
    private Set<RegionHierarchy> parentRegions;
    private Set<RegionHierarchy> childRegions;
    private DomainObjectStatus status;

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
            this.aliases.add(new RegionAlias(this, alias));
        }
    }

    protected Region() {
        init();
    }

    private void init() {
        aliases = new HashSet<RegionAlias>();
        parentRegions = new HashSet<RegionHierarchy>();
        childRegions = new HashSet<RegionHierarchy>();
        status = DomainObjectStatus.Valid;
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
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
    @OneToMany(mappedBy = "regionHandle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    @Transient
//    protected abstract Region getRegionTarget();
    
    /**
     * Get the parent regions.
     * A Region is a parent of this region if a Map exists where this is the Child
     * @return
     */
    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
    protected Set<RegionHierarchy> getParentRegionMaps() {
        return parentRegions;
    }

    protected void setParentRegionMaps(Set<RegionHierarchy> parentRegions) {
        this.parentRegions = parentRegions;
    }

    /**
     * Get the children regions.
     * A Region is a child of this region if a Map exists where this is the Parent
     * @return
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected Set<RegionHierarchy> getChildRegionMaps() {
        return childRegions;
    }

    protected void setChildRegionMaps(Set<RegionHierarchy> childRegions) {
        this.childRegions = childRegions;
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

        for (RegionHierarchy map : childRegions) {
            if (map.getChild().equals(childHandle)) {
                childEntryToRemove = map;
                break;
            }
        }

        if (childEntryToRemove != null) {
            removed = childRegions.remove(childEntryToRemove);
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

        for (RegionHierarchy map : parentRegions) {
            if (map.getParent().equals(parentHandle)) {
                parentEntryToRemove = map;
                break;
            }
        }

        if (parentEntryToRemove != null) {
            removed = parentRegions.remove(parentEntryToRemove);
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
        return parentRegions.add(map);
    }

    private boolean doAddChildMap(RegionHierarchy map) {
        return childRegions.add(map);
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
        for (RegionHierarchy map : childRegions) {
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
        for (RegionHierarchy map : parentRegions) {
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
        for (RegionHierarchy map : parentRegions) {
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
        for (RegionHierarchy map : childRegions) {
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
     }

}