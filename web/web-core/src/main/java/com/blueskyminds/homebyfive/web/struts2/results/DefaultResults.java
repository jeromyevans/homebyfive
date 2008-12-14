package com.blueskyminds.homebyfive.web.struts2.results;

/**
 * Date Started: 14/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DefaultResults {
    
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static CustomHttpHeaders success() {
        return new CustomHttpHeaders(SUCCESS).withStatus(200);
    }

    public static CustomHttpHeaders created() {
        return new CustomHttpHeaders(SUCCESS).withStatus(201);
    }

    public static CustomHttpHeaders error() {
        return new CustomHttpHeaders(ERROR);
    }

    public static CustomHttpHeaders errorNotFound() {
        return new CustomHttpHeaders(ERROR).withStatus(404);
    }

    public static CustomHttpHeaders errorInvalidRequest() {
        return new CustomHttpHeaders(ERROR).withStatus(400);
    }

    public static CustomHttpHeaders errorConflict() {
        return new CustomHttpHeaders(ERROR).withStatus(409);
    }

    public static CustomHttpHeaders errorInternal() {
        return new CustomHttpHeaders(ERROR).withStatus(500);
    }
}
