package com.blueskyminds.housepad.core.region.reference;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * A reference to a Region by it's ID with useful metadata (type, name, abbr, misc attributes)
 *
 * Date Started: 25/10/2007
 * <p/>
 * History:
 */
public class RegionRef implements Serializable {
    
    private Long id;
    private String path;
    private String name;
    private String abbr;
    private RegionRefType type;
    private Map<String, String> attributes;

    public RegionRef(String path, String name, String abbr, RegionRefType type) {
        this.path = path;
        this.type = type;
        this.name = name;
        this.abbr = abbr;
        attributes = new HashMap<String, String>();
    }

    public RegionRef(Long id, String path, String name, String abbr, RegionRefType type) {
        this(path, name, abbr, type);
        this.id = id;        
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    public Integer getType() {
        return type.ordinal();
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return getName();
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    /** Additional attributes describing the region */
    public Map<String, String> getAttributes() {
        return attributes;
    }

}
