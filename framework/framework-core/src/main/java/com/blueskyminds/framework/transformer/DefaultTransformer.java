package com.blueskyminds.framework.transformer;

/**
 * The DefaultTransformer performs no transformation - it returns the input
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DefaultTransformer<F> implements Transformer<F, F> {

    public F transform(F fromObject) {
        return fromObject;
    }

    // ------------------------------------------------------------------------------------------------------
}
