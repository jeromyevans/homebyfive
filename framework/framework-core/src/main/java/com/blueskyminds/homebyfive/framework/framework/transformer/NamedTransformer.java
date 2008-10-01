package com.blueskyminds.homebyfive.framework.framework.transformer;

import com.blueskyminds.homebyfive.framework.framework.tools.Named;

/**
 *
 * Gets the name from an NamedObject and returns it as a string
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class NamedTransformer<N extends Named> implements Transformer<N, String> {
    public String transform(Named fromObject) {
        return fromObject.getName();
    }
}
