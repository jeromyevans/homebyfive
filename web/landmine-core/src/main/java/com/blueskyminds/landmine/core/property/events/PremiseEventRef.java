package com.blueskyminds.landmine.core.property.events;

import java.util.Date;

/**
 * Date Started: 22/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PremiseEventRef {

    private Date date;
    private String path;
    private String type;
    private String name;   

    public PremiseEventRef(String path, String type, Date date, String name) {
        this.path = path;
        this.type = type;
        this.date = date;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
