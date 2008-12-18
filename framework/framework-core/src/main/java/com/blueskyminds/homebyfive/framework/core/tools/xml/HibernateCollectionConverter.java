package com.blueskyminds.homebyfive.framework.core.tools.xml;

import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentSet;

/**
 * Convert hibernate collections as plain old java collections.  Needs to be registered with XStream and default
 * implementation overridden.
 *
 * Date Started: 17/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class HibernateCollectionConverter extends CollectionConverter {

    public HibernateCollectionConverter(Mapper mapper) {
        super(mapper);
    }

    public boolean canConvert(Class type) {
        return super.canConvert(type) || type == PersistentBag.class || type == PersistentList.class || type == PersistentSet.class;
    }
}
