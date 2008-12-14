package com.blueskyminds.homebyfive.framework.core.tools;

import com.thoughtworks.xstream.XStream;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

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
            //this.xStream.p
//        xStream.setMode(XStream.NO_REFERENCES);
//        xStream.addImplicitCollection(Addresses.class, "list");
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
