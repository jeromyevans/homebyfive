package com.blueskyminds.homebyfive.framework.framework.transformer;

/**
 * Transforms a string exactly matching an Enum constant into an instance of the Enum
 *  (ie. basically by called enum.valueOf)
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EnumTransformer<E extends Enum> implements Transformer<String, E>{

    private Class<E> enumClass;

    public EnumTransformer(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public E transform(String enumConstant) {
        E result = null;
        try {
            if (enumConstant != null) {
                result = (E) Enum.valueOf(enumClass, enumConstant);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return result;
    }

    // ------------------------------------------------------------------------------------------------------
}
