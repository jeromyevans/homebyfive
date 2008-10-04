package com.blueskyminds.homebyfive.web.struts2;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import java.io.Reader;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.struts2.rest.handler.ContentTypeHandler;

/**
 * Date Started: 21/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class JsonLibContentTypeHandler implements ContentTypeHandler {



    public void toObject(Reader in, Object target) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            sb.append(buffer, 0, len);
        }

        JsonConfig config = prepareJsonConfig();
        if (target != null && sb.length() > 0 && sb.charAt(0) == '[') {
            JSONArray jsonArray = JSONArray.fromObject(sb.toString());
            if (target.getClass().isArray()) {
                JSONArray.toArray(jsonArray, target, config);
            } else {
                JSONArray.toList(jsonArray, target, config);
            }

        } else {
            JSONObject jsonObject = JSONObject.fromObject(sb.toString());
            JSONObject.toBean(jsonObject, target, config);
        }
    }

    private JsonConfig prepareJsonConfig() {
        JsonConfig config = new JsonConfig();

        // enable support for java.sql.Date
        config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());
        
        return config;
    }

    public String fromObject(Object obj, String resultCode, Writer stream) throws IOException {
        if (obj != null) {
            if (isArray(obj)) {
                JSONArray jsonArray = JSONArray.fromObject(obj, prepareJsonConfig());
                stream.write(jsonArray.toString());
            } else {
                JSONObject jsonObject = JSONObject.fromObject(obj, prepareJsonConfig());
                stream.write(jsonObject.toString());
            }
        }
        return null;


    }

    private boolean isArray(Object obj) {
        return obj instanceof Collection || obj.getClass().isArray();
    }

    public String getContentType() {
        return "text/javascript";
    }

    public String getExtension() {
        return "json";
    }
}
