package com.blueskyminds.homebyfive.framework.framework;

import javax.persistence.*;
import java.io.PrintStream;

/**
 * The base entity implementation with that uses an Long identity
 * <p/>
 * NOTE: the id is used to generate the hashcode.  As the id is only assigned once the Entity has been
 * persisted its possible for the hashcode to change.  If the Entity is used an a HashSet prior to being
 * persisted the HashSet contract is broken.  This means the usual methods (contains, remove etc) will no longer
 * locate the Entity in the hashset - take care.
 * <p/>
 * Date Started: 17/06/2007 (refactored from DomainObject)
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@MappedSuperclass
public class AbstractEntity implements HasIdentity, Printable {

    /**
     * The unique identifier assigned to this domain object.
     */
    private Long id;

    public AbstractEntity() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * returns the unique identifier assigned to this domain object
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    @IgnoreMerge
    public Long getId() {
        return id;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Private method to set the identity.  For use only in ORM and Test
     */
    protected void setId(Long id) {
        this.id = id;
    }

    /**
     * Identifies whether the ID has been set for this entity
     * @return
     */
    @Transient
    public boolean isIdSet() {
        return id != null;
    }


    /**
     * Returns an unique identity for the domain object dervived from the simple class name and the
     * identity
     *
     * @return a unique identifier for the domain object
     */
    @Transient
    public String getIdentityName() {
        return getClass().getSimpleName() + "[" + getId() + "]";
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Return an IdentityRef for the domain object.
     */
    @Transient
    public IdentityRef getIdentity() {
        return new IdentityRef(getClass(), getId());
    }

    public void print(PrintStream out) {
        out.println(getIdentityName());
    }

    /**
     * NOTE: equals uses the Id of the entity if defined.  Two instances with the same Identity are considered
     *  equal.
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasIdentity that = (HasIdentity) o;
        boolean result = false;

        if (id != null) {
            if (id.equals(that.getId())) {
                result = true;
            }
        } else {
            if (that.getId() == null) {
                // both instance have a null id - the only way they can be the equal is if they are the same instance
                // (which was already tested above using == , but test it here again anyway)
                if (getClass().equals(that.getClass())) {
                    result = super.equals(o);
                }
            }
        }

        return result;
    }


    /**
     * The id is used to generate the hashcode.  As the id is only assigned after the Entity has been
     *  persisted its possible for the hashcode to change.  If the Entity is used an a HashSet prior to being
     *  persisted the HashSet contract is broken.  This means the usual methods (contains, remove etc) will no longer
     *  locate the Entity in the hashset - take care.
     * @return
     */
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * The toString method has been overridden to show the short className, id and hashcode
     *
     * @return string representation of this DomainObject
     */
    public String toString() {
        return getClass().getName() + "[" + id + "]@" + Integer.toHexString(hashCode());
    }

    /**
     * Convenient instance of implementation
     * @param clazz     the class in question
     * @return  true if this is assignable from the specified class
     **/
    public boolean isInstance(Class clazz) {
        return this.getClass().isAssignableFrom(clazz);
    }

    /**
     * Returns a holder containing the base object
     * When this method is called through a proxy this method returns the real target instance, allowing
     * proxies to be bypassed.
     *
     * Use sparingly!
     *
     * Reference:
     * http://forum.hibernate.org/viewtopic.php?t=947035
     *
     * @return
     */
    public AbstractEntityHolder unproxy() {
        return new AbstractEntityHolder(this);
    }

    /**
     * Returns a holder containing this if this instance is the specified class.
     * When this method is called through a proxy this method returns the real target instance, allowing
     * proxies to be bypassed.
     *
     * Use sparingly!
     *
     * Reference:
     * http://forum.hibernate.org/viewtopic.php?t=947035
     *
     * @return
     */
    public AbstractEntity unproxyIfInstance(Class clazz) {
        return isInstance(clazz) ? this : null;
    }
}
