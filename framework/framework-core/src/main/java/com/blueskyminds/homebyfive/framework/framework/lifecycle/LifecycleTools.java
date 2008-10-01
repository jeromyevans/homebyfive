package com.blueskyminds.homebyfive.framework.framework.lifecycle;

/**
 * Methods to assist with the life-cycle of objects
 *
 * Date Started: 10/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class LifecycleTools {

    /**
     * Determines if the specified class implements the Preparable interface
     * @param clazz     class to check
     * @return true it it does
     **/
    public static boolean isPreparable(Class clazz) {
        if (clazz != null) {
            return (Preparable.class.isAssignableFrom(clazz));
        } else {
            return false;
        }
    }

    /**
     * Determines if the specified object is of a class that implements the Preparable interface
     * @param object    object to check
     * @return true it it does
     **/
    public static boolean isPreparable(Object object) {
        if (object != null) {
            return isPreparable(object.getClass());
        } else {
            return false;
        }
    }

    /**
     * Determines if the class specified by name implements the Preparable interface.
     * The current classloader is used to find the class
     *
     * @param clazzName fully qualified class name
     * @return true it it does
     *
     * */
    public static boolean isPreparable(String clazzName) {
        if (clazzName != null) {
            try {
                Class clazz = Class.forName(clazzName);
                return isPreparable(clazz);
            } catch(ClassNotFoundException e) {
                // ignored
            } catch(LinkageError e) {
                // ignred
            }
        }
        return false;
    }      

}
