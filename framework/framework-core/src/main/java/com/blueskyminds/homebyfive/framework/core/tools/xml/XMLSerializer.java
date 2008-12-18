package com.blueskyminds.homebyfive.framework.core.tools.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.Mapper;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;

/**
 * Setup XStream for serialization of entities
 *
 * Date Started: 29/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class XMLSerializer<T> {

    private XStream xStream;

    /** Initialize the XStream instance for serialization/deserialization */
    public XStream setupXStream() {
        if (this.xStream == null) {
            this.xStream = new XStream();
            xStream.autodetectAnnotations(true);

            xStream.addDefaultImplementation(PersistentList.class, java.util.List.class);
            xStream.addDefaultImplementation(PersistentBag.class, java.util.List.class);
            xStream.addDefaultImplementation(PersistentSet.class, java.util.Set.class);
            xStream.addDefaultImplementation(PersistentMap.class, java.util.Map.class);
            Mapper mapper = xStream.getMapper();
            xStream.registerConverter(new HibernateCollectionConverter(mapper));
            xStream.registerConverter(new HibernateMapConverter(mapper));
        }
        return xStream;
    }

    public String serialize(T model) {
        XStream xStream = setupXStream();
        return xStream.toXML(model);
    }

    public void serialize(T model, Writer out) {
        XStream xStream = setupXStream();
        xStream.toXML(model, out);
    }

    public T deserialize(String response) {
        XStream xStream = setupXStream();
        return (T) xStream.fromXML(response);
    }

    public T deserialize(Reader in, Object target) {
        XStream xStream = setupXStream();
        return (T) xStream.fromXML(in, target);
    }
}
