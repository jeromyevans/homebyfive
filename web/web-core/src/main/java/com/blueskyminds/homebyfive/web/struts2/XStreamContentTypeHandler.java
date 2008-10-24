package com.blueskyminds.homebyfive.web.struts2;

import org.apache.struts2.rest.handler.ContentTypeHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Writer;
import java.io.IOException;
import java.io.Reader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.BaseException;

/**
 * Date Started: 24/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class XStreamContentTypeHandler implements ContentTypeHandler {

    private static final Log LOG = LogFactory.getLog(XStreamContentTypeHandler.class);

    public String fromObject(Object obj, String resultCode, Writer out) throws IOException {
        if (obj != null) {
            XStream xstream = createXStream();
            xstream.toXML(obj, out);
        }
        return null;
    }

    public void toObject(Reader in, Object target) {
        XStream xstream = createXStream();
        try {
            xstream.fromXML(in, target);
        } catch (BaseException e) {
            LOG.error(e);
        }
    }

    protected XStream createXStream() {
        return new XStream();
    }

    public String getContentType() {
        return "application/xml";
    }

    public String getExtension() {
        return "xml";
    }
}
