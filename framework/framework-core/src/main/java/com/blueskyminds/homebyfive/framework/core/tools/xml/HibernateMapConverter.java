package com.blueskyminds.homebyfive.framework.core.tools.xml;

import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.hibernate.collection.PersistentMap;

/**
 * Convert Hibernate maps as plain old java maps.  Needs to be registered with XStream and default
 * implementation overridden.
 *
 * Date Started: 17/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class HibernateMapConverter extends MapConverter {

    public HibernateMapConverter(Mapper mapper) {
        super(mapper);
    }

    public boolean canConvert(Class type) {
        return super.canConvert(type) || type == PersistentMap.class;
    }
}
