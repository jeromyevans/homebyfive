package com.blueskyminds.homebyfive.framework.core.tools;

import com.blueskyminds.homebyfive.framework.core.reflection.PropertyDescriptor;
import com.blueskyminds.homebyfive.framework.core.reflection.MergeablePropertyFilter;

import java.util.List;
import java.util.LinkedList;

/**
 * Helpful static methods for working with reflection
 *
 * Date Started: 9/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class ReflectionTools {

    /**
     * Pre-initialise a single instance of a filter for locating the mergeable properties in a subclass.
     * It's stateless and may be reused a lot.
     */
    private static final MergeablePropertyFilter MERGEABLE_PROPERTY_FILTER = new MergeablePropertyFilter();

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an instance of the object with the specified class name
     * The class must have a PUBLIC default no-parameter constructor
     *
     * @param className     fully qualified classname that can be located by the ClassLoader
     * @return instance of the specified class using the default constructor
     * @throws ReflectionException is not found, can't instantiate or can't access the class/object
     **/
    public static Object createInstanceOf(String className) throws ReflectionException {
        if (className != null) {
            try {
                Class clazz = Class.forName(className);
                return createInstanceOf(clazz);
            } catch (ClassNotFoundException e) {
                throw new ReflectionException("ReflectionTools error creating an instance of "+className+":", e);
            }
        } else {
            throw new ReflectionException("Attempted to instantiate a class with null name");
        }
    }

      /**
     * Create an instance of the object with the specified class name
     * The class must have a PUBLIC default no-parameter constructor
     *
     * @return instance of the specified class using the default constructor
     * @throws ReflectionException is not found, can't instantiate or can't access the class/object
     **/
    public static <T> T createInstanceOf(Class<T> clazz) throws ReflectionException {
        T object = null;
        if (clazz != null) {
            try {
                object  = (T) clazz.newInstance();
            } catch (IllegalAccessException e) {
                throw new ReflectionException("ReflectionTools error creating an instance of "+clazz.getName()+":", e);
            } catch (InstantiationException e) {
                throw new ReflectionException("ReflectionTools error creating an instance of "+clazz.getName()+":", e);
            }
        } else {
            throw new ReflectionException("Attempted to instantiate a class with null name");
        }
        return object;
    }

    /**
     * Recursive method to visit all the interfaces of a class (and its superclasses and super-interfaces)
     * Always adds itself to the list
     * Object.class is not visited
     *
     * @param thisClass      the current class (added to the list)
     * @param classes
     */
    private static void recurseInterfaces(Class thisClass, List<Class> classes) {
        classes.add(thisClass);

        // recurse the interfaces and all the interface superclasses
        for (Class thisInterface : thisClass.getInterfaces()) {
            recurseInterfaces(thisInterface, classes);
        }

        // recurse the superclass if it's not Object
        Class superClass = thisClass.getSuperclass();
        if ((superClass != null) && (!Object.class.equals(superClass))) {
            recurseInterfaces(superClass, classes);
        }
    }

    /**
     * Recursive method to visit all the interfaces of a class (and its superclasses and super-interfaces) if they
     * haven't already been visited.
     *
     * Always adds itself to the list if not already present
     *
     * @param thisClass      the current class (added to the list)
     * @param classes
     */
    private static void recurseUniqueInterfaces(Class thisClass, List<Class> classes) {
        if (!classes.contains(thisClass)) {
            classes.add(thisClass);

            Class[] interfaces = thisClass.getInterfaces();
            for (Class thisInterface : interfaces) {
                recurseUniqueInterfaces(thisInterface, classes);
            }

            Class superClass = thisClass.getSuperclass();
            if ((superClass != null) && (!Object.class.equals(superClass))) {
                recurseUniqueInterfaces(superClass, classes);
            }
        }
    }

    /**
     * List all the interfaces realized by the specified object, its superclasses and its interfaces
     *
     * The list is ordered to allow precendence to be applied:
     *   object's class
     *   object's interfaces
     *   the interface's superclasses (interaces)
     *   object's superclass
     *   superclass' interfaces
     *   superclass' interface's superclasses (interfaces)
     *   super-superclass
     *
     * The Object base class is excluded.
     *
     * @param object
     * @param unique    if false, an interace can be listed multiple times if its realized by multiple classes.  If true
     *                  each interface will be listed only the first time it's encountered
     * @return
     */
    public static List<Class> listInterfaces(Object object, boolean unique) {
        List<Class> classes = new LinkedList<Class>();
        if (object != null) {
            Class thisClass = object.getClass();
            if (!unique) {
                recurseInterfaces(thisClass, classes);
            } else {
                recurseUniqueInterfaces(thisClass, classes);
            }
        }
        return classes;
    }

    /**
     * Realizes the visit(Class) method called by vistInterfaces for all encountered classes/interfaces
     */
    public static interface ClassVisitor {

        /**
        * Called when a new interface/class is encountered
        *
        * @param       aClass the encountered class/interface
        * @return true if the recursion should continue, false to stop recursion immediately
        * */
        boolean visit(Class aClass);
    }

    /**
     * Visit all the interfaces realized by the specified object, its superclasses and its interfaces
     *
     * Visitation is performed in the following order:
     *   object's class
     *   object's interfaces
     *   the interface's superclasses (interfaces)
     *   object's superclass
     *   superclass' interfaces
     *   superclass' interface's superclasses (interfaces)
     *   super-superclass
     *   and so on
     *
     * The Object base class is base excluded.
     *
     * @param object
     * @param unique    if false, an interace can be visited multiple times if its realized by multiple classes/interfaces.  If true
     *                  each interface will be visited only the first time it's encountered
     * @param visitor   this vistor is called for each class/interface encountered
     * @return true if all classes/interfaces were visited, false if it was exited early as specified by a ClassVisitor result
     */
    public static boolean visitInterfaces(Object object, boolean unique, ClassVisitor visitor) {
        boolean completed;
        List<Class> classesVisited = new LinkedList<Class>();

        if (object != null) {
            Class thisClass = object.getClass();
            if (!unique) {
                completed = visitInterfaces(thisClass, visitor);
            } else {
                completed = visitUniqueInterfaces(thisClass, visitor, classesVisited);
            }
        } else {
            completed = true;
        }

        return completed;
    }

    /**
     * Recursive method to visit all the interfaces of a class (and its superclasses and super-interfaces)
     * Always visits itself
     * Object.class is not visited
     *
     * @param thisClass      the current class to visit
     * @param visitor        this vistor is called for each class/interface encountered
     * @return true if recursion can continue, false if it should be aborted
     */
    private static boolean visitInterfaces(Class thisClass, ClassVisitor visitor) {
        boolean okayToContinue;

        okayToContinue = visitor.visit(thisClass);

        if (okayToContinue) {
            // recurse the interfaces and all the interface superclasses
            Class[] interfaces = thisClass.getInterfaces();
            int index = 0;
            while ((index < interfaces.length) && (okayToContinue)) {
                okayToContinue = visitInterfaces(interfaces[index++], visitor);
            }

            if (okayToContinue) {
                // recurse the superclass if it's not Object
                Class superClass = thisClass.getSuperclass();
                if ((superClass != null) && (!Object.class.equals(superClass))) {
                    okayToContinue = visitInterfaces(superClass, visitor);
                }
            }
        }
        return okayToContinue;
    }

    /**
     * Recursive method to visit all the interfaces of a class (and its superclasses and super-interfaces) if they
     * haven't already been visited.
     *
     * Always visits itself if it hasn't already been visited
     *
     * @param thisClass         the current class to visit (if not already done so)
     * @param classesVisited    classes already visited
     * @param visitor        this visitor is called for each class/interface encountered
     * @return true if recursion can continue, false if it should be aborted
     */
    private static boolean visitUniqueInterfaces(Class thisClass, ClassVisitor visitor, List<Class> classesVisited) {
        boolean okayToContinue = true;

        if (!classesVisited.contains(thisClass)) {
            classesVisited.add(thisClass);
            okayToContinue = visitor.visit(thisClass);

            if (okayToContinue) {
                Class[] interfaces = thisClass.getInterfaces();
                int index = 0;
                while ((index < interfaces.length) && (okayToContinue)) {
                    okayToContinue = visitUniqueInterfaces(interfaces[index++], visitor, classesVisited);
                }

                if (okayToContinue) {
                    Class superClass = thisClass.getSuperclass();
                    if ((superClass != null) && (!Object.class.equals(superClass))) {
                        okayToContinue = visitUniqueInterfaces(superClass, visitor, classesVisited);
                    }
                }
            }
        }
        return okayToContinue;
    }

    /**
     * Merges the simple properties of the other DomainObject into this DomainObject
     *
     * Simple properties are those that are not collections, are not transient and do not carry the
     *  IgnoreMerge annotation
     *
     * Null properties of the other DomainObject are ignored.  Existing values of this domain object are overridden.
     *
     * @param other
     */
    @SuppressWarnings({"unchecked"})
    public static void updateSimpleProperties(Object target, Object other) {
        List<PropertyDescriptor> propertyDescriptors = PropertyDescriptor.getProperties(target.getClass(), MERGEABLE_PROPERTY_FILTER);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object otherValue = propertyDescriptor.getValue(other);

            if (otherValue != null) {
                propertyDescriptor.setValue(target, otherValue);
            }
        }
    }

     /**
     * Merges the simple properties of the other DomainObject into this DomainObject
     *
     * Simple properties are those that are not collections, are not transient and do not carry the
     *  IgnoreMerge annotation
     *
     * A merge is performed only if this DomainObject has a null value for the property.
     * For an marge that overwrites existing properties see {@link com.blueskyminds.homebyfive.framework.core.AbstractDomainObject#updateSimpleProperties}
     *
     * @param other
     */
    @SuppressWarnings({"unchecked"})
    public static void mergeSimpleProperties(Object target, Object other) {
        List<PropertyDescriptor> propertyDescriptors = PropertyDescriptor.getProperties(target.getClass(), MERGEABLE_PROPERTY_FILTER);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object thisValue = propertyDescriptor.getValue(target);
            if (thisValue == null) {
                Object otherValue = propertyDescriptor.getValue(other);

                if (otherValue != null) {
                    propertyDescriptor.setValue(target, otherValue);
                }
            }
        }
    }
}
