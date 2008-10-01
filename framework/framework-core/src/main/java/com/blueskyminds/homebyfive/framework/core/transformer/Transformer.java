package com.blueskyminds.homebyfive.framework.core.transformer;

/**
 * Transforms an object of class F into an object of class T
 *
 * Date Started: 12/01/2007
 * <p/>
 * Parameters:
 *  F from
 *  T to
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Transformer<F, T> {
    T transform(F fromObject);
}
