package com.blueskyminds.framework;

import com.blueskyminds.framework.DomainObject;

import java.io.Serializable;

/**
 * Represents the unqiue identity if a DomainObject, which is characterised by its Class and Id.
 *
 * The identity is:
 *    it's class; and
 *    it's id
 *
 * The identity is serializable
 *
 * Date Started: 8/09/2006
 *
 * History:
 *   17 Jun 2007 - changed to use Identity instead of DomainObject
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class IdentityRef implements HasIdentity, Serializable {

    private Class<? extends HasIdentity> clazz;
    private Long id;

    // ------------------------------------------------------------------------------------------------------

    public IdentityRef(Class<? extends HasIdentity> clazz, Long id) {
        this.clazz = clazz;
        this.id = id;
    }

    // ------------------------------------------------------------------------------------------------------

    public Class<? extends HasIdentity> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends HasIdentity> clazz) {
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the drived name for the identity */
    public String getName() {
        return clazz.getSimpleName()+"["+id+"]";
    }

}
